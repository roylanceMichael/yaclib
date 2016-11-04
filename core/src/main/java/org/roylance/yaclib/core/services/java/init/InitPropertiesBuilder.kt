package org.roylance.yaclib.core.services.java.init

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CLITokens
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.io.StringWriter
import java.util.*

class InitPropertiesBuilder(private val mainDependency: YaclibModel.Dependency,
                            private val yaclibDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val properties = Properties()

        properties.setProperty(JavaUtilities.MinorName, mainDependency.minorVersion.toString())
        properties.setProperty(JavaUtilities.MajorName, mainDependency.majorVersion.toString())
        properties.setProperty(CLITokens.YaclibMajorName, yaclibDependency.majorVersion.toString())
        properties.setProperty(CLITokens.YaclibMinorName, yaclibDependency.minorVersion.toString())
        properties.setProperty(CLITokens.GithubRepoName, mainDependency.githubRepo)
        properties.setProperty(CLITokens.LicenseName, mainDependency.license)
        properties.setProperty(CLITokens.RepoUrlName, mainDependency.mavenRepository.url)
        properties.setProperty(CLITokens.RepoNameName, mainDependency.mavenRepository.name)
        properties.setProperty(CLITokens.AuthorName, mainDependency.authorName)

        val stringWriter = StringWriter()
        properties.store(stringWriter, "generated from yaclib on ${Date()}")

        val file = YaclibModel.File.newBuilder()
                .setFileExtension(YaclibModel.FileExtension.PROPERTIES_EXT)
                .setFileToWrite(stringWriter.toString().trim())
                .setFileName(JavaUtilities.PropertiesFileNameWithoutExtension)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("")
                .build()

        return file
    }
}