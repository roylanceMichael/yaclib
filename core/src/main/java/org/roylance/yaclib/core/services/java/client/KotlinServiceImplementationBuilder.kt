package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceImplementationBuilder(private val controller: YaclibModel.Controller,
                                         private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()

        val restInterfaceName = "I${controller.name}${CommonTokens.UpperCaseRestName}"
        val restVariableName = "rest${controller.name}"
        val initialTemplate = """package ${dependency.group}.${CommonTokens.ServicesName}

import org.roylance.common.service.IProtoSerializerService

class ${controller.name}${CommonTokens.ServiceName}(
        private val $restVariableName: $restInterfaceName,
        private val protoSerializer: IProtoSerializerService): ${StringUtilities.convertServiceNameToInterfaceName(controller)} {
"""

        workspace.append(initialTemplate)

        controller.actionsList.forEach { action ->
            val argumentsList = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
            }.joinToString()

            val base64Variables = action.inputsList.map { input ->
                "val base64${input.argumentName} = protoSerializer.serializeToBase64(${input.argumentName})"
            }.joinToString("\n")

            val base64InputParameters = action.inputsList.map { input ->
                "base64${input.argumentName}"
            }.joinToString()

            val initialActionTemplate = """
    override fun ${action.name}($argumentsList): ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass} {
        $base64Variables
        val responseCall = $restVariableName.${action.name}($base64InputParameters)
        val response = responseCall.execute()
        return protoSerializer.deserializeFromBase64(response.body(),
                ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass}.getDefaultInstance())
    }
"""
            workspace.append(initialActionTemplate)
        }

        workspace.append("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileName("${controller.name}${CommonTokens.ServiceName}")
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group,
                        CommonTokens.ServicesName))

        return  returnFile.build()
    }
}