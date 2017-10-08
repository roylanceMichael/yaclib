package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class WiringFileBuilder(private val allControllerDependencies: YaclibModel.AllControllerDependencies): IBuilder<YaclibModel.File> {
    private val window = "\$window"
    private val http = "\$http"

    private val initialTemplate = """${CommonTokens.DoNotAlterMessage}
/// <reference path="../node_modules/roylance.common/bytebuffer.d.ts" />
${this.importFactories()}
${this.importDependencies()}
import {${HttpExecuteImplementationBuilder.FileName}} from "./${HttpExecuteImplementationBuilder.FileName}"
import {furtherAngularSetup} from "./FurtherAngularSetup"

declare var angular: any;
const app = angular.module('jsapp', [
    "ngRoute"
]);

app.factory("$HttpExecuteVariableName", function ($window, $http) {
    return new ${HttpExecuteImplementationBuilder.FileName}($http);
});

${this.importFactoryImplementations()}
${this.importControllerImplementations()}

furtherAngularSetup(app);
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileName("Wiring")
                .setFileExtension(YaclibModel.FileExtension.TS_EXT)
                .setFullDirectoryLocation("src/main/javascript/app")

        return returnFile.build()
    }

    private fun importFactories():String {
        val workspace = StringBuilder()

        this.allControllerDependencies.controllerDependenciesList.forEach { dependency ->
            val dependencyTemplate = """import {${dependency.dependency.typescriptModelFile}} from "${this.buildFactoryNodeModulePath(dependency.dependency)}";
"""
            workspace.append(dependencyTemplate)
        }

        return workspace.toString()
    }

    private fun importDependencies():String {
        val workspace = StringBuilder()

        this.allControllerDependencies.controllerDependenciesList.forEach { dependency ->
            dependency.controllers.controllersList.forEach { controller ->
                val controllerTemplate = """import {${controller.name}${CommonTokens.ServiceName}} from "${this.buildControllerServiceNodeModulePath(dependency.dependency, controller)}";
"""
                workspace.append(controllerTemplate)
            }
        }

        return workspace.toString()
    }

    private fun importFactoryImplementations():String {
        val workspace = StringBuilder()

        this.allControllerDependencies.controllerDependenciesList.forEach { dependency ->
            val lowercaseFirstChar = "${dependency.dependency.typescriptModelFile[0].toLowerCase()}${dependency.dependency.typescriptModelFile.substring(1)}"
            val dependencyTemplate = """app.factory("$lowercaseFirstChar", function () {
    return ${dependency.dependency.typescriptModelFile}.${dependency.dependency.group};
});
"""
            workspace.append(dependencyTemplate)
        }

        return workspace.toString()
    }

    private fun importControllerImplementations():String {
        val workspace = StringBuilder()

        this.allControllerDependencies.controllerDependenciesList.forEach { dependency ->
            dependency.controllers.controllersList.forEach { controller ->
                val lowercaseFirstChar = "${controller.name[0].toLowerCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
                val lowercaseFirstCharDependency = "${dependency.dependency.typescriptModelFile[0].toLowerCase()}${dependency.dependency.typescriptModelFile.substring(1)}"

                val template = """app.factory("$lowercaseFirstChar", function($HttpExecuteVariableName:${HttpExecuteImplementationBuilder.FileName}, $lowercaseFirstCharDependency:${dependency.dependency.group}.${dependency.dependency.name}.ProtoBufBuilder) {
    return new ${controller.name}${CommonTokens.ServiceName}($HttpExecuteVariableName, $lowercaseFirstCharDependency)
});
"""
                workspace.append(template)
            }
        }

        return workspace.toString()
    }

    private fun buildFactoryNodeModulePath(dependency: YaclibModel.Dependency):String {
        if (dependency.hasNpmRepository() &&
                dependency.npmRepository.npmScope.isNotEmpty()) {
            return "../${CommonTokens.NodeModules}/${dependency.npmRepository.npmScope}/${dependency.group}.${dependency.name}/${dependency.typescriptModelFile}${CommonTokens.FactoryName}"
        }

        return "../${CommonTokens.NodeModules}/${dependency.group}.${dependency.name}/${dependency.typescriptModelFile}${CommonTokens.FactoryName}"
    }

    private fun buildControllerServiceNodeModulePath(dependency: YaclibModel.Dependency, controller: YaclibModel.Controller):String {
        if (dependency.hasNpmRepository() &&
                dependency.npmRepository.npmScope.isNotEmpty()) {
            return "../${CommonTokens.NodeModules}/${dependency.npmRepository.npmScope}/${dependency.group}.${dependency.name}/${controller.name}${CommonTokens.ServiceName}"
        }

        return "../${CommonTokens.NodeModules}/${dependency.group}.${dependency.name}/${controller.name}${CommonTokens.ServiceName}"
    }

    companion object {
        private const val HttpExecuteVariableName = "httpExecute"
    }
}