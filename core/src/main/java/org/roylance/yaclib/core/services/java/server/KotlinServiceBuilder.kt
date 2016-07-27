package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceBuilder(
        private val controller: Models.Controller,
        private val overallPackage: String): IBuilder<Models.File> {
    override fun build(): Models.File {
        val initialTemplate = """package $overallPackage.${CommonTokens.ServicesName}

interface I${controller.name}Service {
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

        val returnFile = Models.File.newBuilder()
                .setFileName(StringUtilities.convertServiceNameToInterfaceName(controller))
                .setFileExtension(Models.FileExtension.KT_EXT)
                .setFileToWrite(workspace.toString())
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(overallPackage, CommonTokens.ServicesName))

        return returnFile.build()
    }
}