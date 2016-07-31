package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceBuilder(
        private val controller: YaclibModel.Controller,
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    override fun build(): YaclibModel.File {
        val initialTemplate = """package ${dependency.group}.${CommonTokens.ServicesName}

interface ${StringUtilities.convertServiceNameToInterfaceName(controller)} {
"""
        val workspace = StringBuilder(initialTemplate)
        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
            }.joinToString()

            val actionTemplate = "\tfun ${action.name}($colonSeparatedInputs): ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass}\n"
            workspace.append(actionTemplate)
        }
        workspace.append("}")
        val returnFile = YaclibModel.File.newBuilder()
                .setFileName(StringUtilities.convertServiceNameToInterfaceName(controller))
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFileToWrite(workspace.toString())
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group,
                        CommonTokens.ServicesName))

        return returnFile.build()
    }
}