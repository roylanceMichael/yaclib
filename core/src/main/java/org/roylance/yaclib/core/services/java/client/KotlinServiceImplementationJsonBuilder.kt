package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceImplementationJsonBuilder(private val controller: YaclibModel.Controller,
                                             private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()

        val restInterfaceName = "I${controller.name}${CommonTokens.UpperCaseRestName}"
        val restVariableName = "rest${controller.name}"
        val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${mainDependency.group}.${CommonTokens.ServicesName}

import com.google.protobuf.util.JsonFormat;

class ${controller.name}${CommonTokens.ServiceName}(
        private val $restVariableName: $restInterfaceName): ${StringUtilities.convertServiceNameToInterfaceName(controller)} {
        private val parser = JsonFormat.parser()
        private val printer = JsonFormat.printer()
"""

        workspace.append(initialTemplate)

        controller.actionsList.forEach { action ->
            val argumentsList = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
            }.joinToString()

            val jsonVariables = action.inputsList.map { input ->
                "val json${input.argumentName} = this.printer.print(${input.argumentName})"
            }.joinToString("\n")

            val jsonInputParameters = action.inputsList.map { input ->
                "json${input.argumentName}"
            }.joinToString()

            val initialActionTemplate = """
    override fun ${action.name}($argumentsList): ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass} {
        $jsonVariables
        val responseCall = $restVariableName.${action.name}($jsonInputParameters)
        val response = responseCall.execute()
        val actualResponse = ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass}.newBuilder()
        this.parser.merge(response.body(), actualResponse);
        return actualResponse.build()
    }
"""
            workspace.append(initialActionTemplate)
        }

        workspace.append("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileName("${controller.name}${CommonTokens.ServiceName}")
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(mainDependency.group,
                        CommonTokens.ServicesName))

        return  returnFile.build()
    }
}