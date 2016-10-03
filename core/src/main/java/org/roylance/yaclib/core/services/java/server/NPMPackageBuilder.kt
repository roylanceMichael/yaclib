package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class NPMPackageBuilder(private val dependencies: YaclibModel.AllControllerDependencies,
                        mainDependency: YaclibModel.Dependency,
                        private val thirdPartyDependencies: List<YaclibModel.Dependency>): IBuilder<YaclibModel.File> {
    private val initialTemplate = """{
  "name": "webapp",
  "version": "${mainDependency.majorVersion}.${mainDependency.minorVersion}.0",
  "description": "${mainDependency.group}.${mainDependency.name}",
  "author": "${mainDependency.authorName}",
  "license": "${mainDependency.license}",
  "devDependencies": {
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
  },
  "dependencies": {
    ${this.buildDependencies()}
  }
}

"""
    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileName("package")
                .setFileExtension(YaclibModel.FileExtension.JSON_EXT)
                .setFullDirectoryLocation("src/main/javascript")

        return returnFile.build()
    }

    private fun buildDependencies(): String {
        val workspace = StringBuilder()
        val thirdPartyDependenciesStr = this.thirdPartyDependencies
                .filter { it.type == YaclibModel.DependencyType.TYPESCRIPT }.map {
            TypeScriptUtilities.buildDependency(it)
        }.joinToString()

        workspace.append(thirdPartyDependenciesStr)
        if (this.thirdPartyDependencies.size > 0) {
            workspace.append(",")
        }
        val internalDependencies = this.dependencies.controllerDependenciesList.map { dependency ->
            TypeScriptUtilities.buildDependency(dependency.dependency)
        }.joinToString()

        workspace.append(internalDependencies)

        return workspace.toString()
    }
}