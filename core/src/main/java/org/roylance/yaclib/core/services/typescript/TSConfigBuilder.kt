package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class TSConfigBuilder(
        private val controllers: YaclibModel.AllControllers,
        private val dependency: YaclibModel.Dependency
): IBuilder<YaclibModel.File> {
    private val fileTemplate = """{
    "compilerOptions": {
        "module": "commonjs",
        "noImplicitAny": false,
        "removeComments": true,
        "preserveConstEnums": true,
        "sourceMap": true
    },
    "files": [
${this.buildFiles()}
    ]
}
"""
    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(fileTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.JSON_EXT)
                .setFileName("tsconfig")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }

    private fun buildFiles(): String {
        val workspace = StringBuilder()
        this.controllers.controllersList.forEach { controller ->
            workspace.appendln("\t\"${StringUtilities.convertServiceNameToInterfaceName(controller)}.ts\",")
            workspace.appendln("\t\"${controller.name}${CommonTokens.ServiceName}.ts\",")
        }
        workspace.appendln("\t\"${HttpExecuteServiceBuilder.FileName}.ts\",")
        workspace.appendln("\t\"${this.dependency.typescriptModelFile}.d.ts\",")
        workspace.appendln("\t\"${this.dependency.typescriptModelFile}${CommonTokens.FactoryName}.ts\",")
        workspace.append("\t\"bytebuffer.d.ts\",")
        workspace.append("\t\"long.d.ts\"")
        return workspace.toString()
    }
}