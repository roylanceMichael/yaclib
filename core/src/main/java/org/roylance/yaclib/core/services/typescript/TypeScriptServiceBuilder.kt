package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class TypeScriptServiceBuilder(private val controller: Models.Controller): IBuilder<Models.File> {
    override fun build(): Models.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """/// <reference path="./model" />
export interface $interfaceName {
"""
        workspace.append(initialTemplate)
        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.messageClass}"
            }.joinToString()

            val actionTemplate = "\t${action.name}($colonSeparatedInputs, onSuccess:(response: ${action.output.filePackage}.${action.output.messageClass})=>void, onError:(response:any)=>void)\n"
            workspace.append(actionTemplate)
        }

        workspace.append("}")

        val returnFile = Models.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileExtension(Models.FileExtension.TS_EXT)
                .setFileName(interfaceName)
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }
}