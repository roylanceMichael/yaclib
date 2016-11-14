package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.nio.file.Paths
import java.util.*

class SettingsXMLBuilder(private val controllerDependencies: YaclibModel.AllControllerDependencies,
                         private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val initialTemplate = """<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
    <!-- PUT IN ~/.m2/settings.xml -->
    ${buildServers()}
</settings>
"""

    override fun build(): YaclibModel.File {
        val userHome = System.getProperty("user.home")
        val settingsFileFullPath = Paths.get(userHome, ".m2").toString()

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.XML_EXT)
                .setFileName("settings")
                .setIgnoreInitialLocation(true)
                .setFullDirectoryLocation(settingsFileFullPath)
                .build()

        return returnFile
    }

    private fun buildServers():String {
        val workspace = StringBuilder()

        val uniqueRepositories = HashMap<String, String>()
        uniqueRepositories[mainDependency.mavenRepository.url] = buildServer(mainDependency.mavenRepository)

        this.controllerDependencies.controllerDependenciesList.forEach {
            uniqueRepositories[it.dependency.mavenRepository.url] = buildServer(it.dependency.mavenRepository)
        }

        uniqueRepositories.values.forEach {
            workspace.appendln(it)
        }

        if (workspace.isNotEmpty()) {
            return """
        <servers>
                $workspace
        </servers>
"""
        }

        return ""
    }

    private fun buildServer(repository: YaclibModel.Repository): String {
        return """
<server>
    <id>${repository.name}</id>
    <username>${if (repository.repositoryType == YaclibModel.RepositoryType.BINTRAY) BintrayEnvironmentUserName else StandardEnvironmentUserName}</username>
    <password>${if (repository.repositoryType == YaclibModel.RepositoryType.BINTRAY) BintrayEnvironmentKeyName else StandardEnvironmentKeyName}</password>
</server>
"""
    }

    companion object {
        const val BintrayEnvironmentUserName = "\${env.${JavaUtilities.BintrayUserName}}"
        const val BintrayEnvironmentKeyName = "\${env.${JavaUtilities.BintrayKeyName}}"

        const val StandardEnvironmentUserName = "\${env.${JavaUtilities.StandardMavenUserName}}"
        const val StandardEnvironmentKeyName = "\${env.${JavaUtilities.StandardMavenPassword}}"
    }
}