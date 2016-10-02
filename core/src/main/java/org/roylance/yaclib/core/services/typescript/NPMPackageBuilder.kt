package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class NPMPackageBuilder(private val dependency: YaclibModel.Dependency,
                        private val thirdPartyDependencies: List<YaclibModel.Dependency>): IBuilder<YaclibModel.File> {

    private val packageTemplate = """{
  "name": "${this.buildPackageName()}",
  "version": "${dependency.majorVersion}.${dependency.minorVersion}.0",
  "description": "models to interface with the ${dependency.group}.${dependency.name} system",
  "author": "${dependency.authorName}",
  "license": "${dependency.license}",
  ${buildPrivateRegistry()}
  "dependencies": {
${this.buildDependencies()}
  }
}
"""

    private fun buildPackageName(): String {
        if (dependency.hasNpmRepository() &&
                dependency.npmRepository.npmScope.length > 0) {
            return "${dependency.npmRepository.npmScope}/${dependency.group}.${dependency.name}"
        }
        return "${dependency.group}.${dependency.name}"
    }

    private fun buildDependencies(): String {
        return this.thirdPartyDependencies.map {
            "\t${TypeScriptUtilities.buildDependency(it)}"
        }.joinToString()
    }

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(packageTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.JSON_EXT)
                .setFileName("package")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }

    private fun buildPrivateRegistry(): String {
        if (dependency.hasNpmRepository() &&
                dependency.npmRepository.repositoryType == YaclibModel.RepositoryType.PRIVATE_NPM &&
                dependency.npmRepository.registry.length > 0) {
            return """"publishConfig":{"registry":"${dependency.npmRepository.registry}"},"""
        }
        return ""
    }
}