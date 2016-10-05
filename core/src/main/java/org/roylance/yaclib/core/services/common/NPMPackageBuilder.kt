package org.roylance.yaclib.core.services.common

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.NPMUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class NPMPackageBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {
    private val packageTemplate = """{
  "name": "${this.buildPackageName()}",
  "version": "${projectInformation.mainDependency.majorVersion}.${projectInformation.mainDependency.minorVersion}.0",
  "description": "${buildDescription()}",
  "author": "${projectInformation.mainDependency.authorName}",
  "license": "${projectInformation.mainDependency.license}",
  ${buildDevDependenciesKit()}
  ${buildPrivateRegistry()}
  "dependencies": {
${buildDependencies()}
  }
}
"""

    private fun buildDescription(): String {
        if (projectInformation.isServer) {
            return "base template for ${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name}"
        }
        return "models to interface with the ${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name} system"
    }

    private fun buildPackageName(): String {
        if (projectInformation.mainDependency.hasNpmRepository() &&
                projectInformation.mainDependency.npmRepository.npmScope.length > 0) {
            return "${projectInformation.mainDependency.npmRepository.npmScope}/${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name}"
        }
        return "${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name}"
    }

    private fun buildDependencies(): String {
        val workspace = StringBuilder()
        val thirdPartyDependenciesStr = projectInformation.thirdPartyDependenciesList
                .filter { it.type == YaclibModel.DependencyType.TYPESCRIPT }.map {
            TypeScriptUtilities.buildDependency(it)
        }.joinToString()

        workspace.append(thirdPartyDependenciesStr)

        if (projectInformation.isServer) {
            if (projectInformation.thirdPartyDependenciesList.size > 0) {
                workspace.append(",")
            }
            val internalDependencies = projectInformation.controllers.controllerDependenciesList.map { dependency ->
                TypeScriptUtilities.buildDependency(dependency.dependency)
            }.joinToString()
            workspace.append(internalDependencies)
        }

        return workspace.toString()
    }

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(packageTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.JSON_EXT)
                .setFileName(NPMUtilities.PackageNameWithoutExtension)
                .setFullDirectoryLocation(if (projectInformation.isServer) "src/main/javascript" else "")
                .build()

        return returnFile
    }

    private fun buildDevDependenciesKit(): String {
        if (projectInformation.isServer) {
            return """"devDependencies": {
    "del": "^2.2.0",
    "gulp": "^3.9.1",
    "gulp-concat": "^2.6.0",
    "gulp-jshint": "^2.0.0",
    "gulp-load-plugins": "^1.2.0",
    "gulp-rename": "^1.2.2",
    "gulp-sass": "^2.2.0",
    "gulp-sync": "^0.1.4",
    "gulp-tsc": "^1.1.5",
    "gulp-uglify": "^1.5.3"
  },"""
        }
        return ""
    }

    private fun buildPrivateRegistry(): String {
        if (projectInformation.mainDependency.hasNpmRepository() &&
                projectInformation.mainDependency.npmRepository.repositoryType == YaclibModel.RepositoryType.PRIVATE_NPM &&
                projectInformation.mainDependency.npmRepository.registry.length > 0) {
            return """"publishConfig":{"registry":"${projectInformation.mainDependency.npmRepository.registry}"},"""
        }
        return ""
    }
}