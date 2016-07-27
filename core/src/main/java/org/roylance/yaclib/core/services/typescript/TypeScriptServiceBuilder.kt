package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class TypeScriptServiceBuilder(private val controller: Models.Controller, private val overallPackage: String): IBuilder<Models.File> {
    override fun build(): Models.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """/// <reference path="./model" />
declare module $overallPackage.services {
    export interface $interfaceName {
"""
        workspace.append(initialTemplate)
        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
            }.joinToString()

            val actionTemplate = "\t\t${action.name}($colonSeparatedInputs, onSuccess:(response: ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass})=>void, onError:(response:any)=>void)\n"
            workspace.append(actionTemplate)
        }

        val finalClosing = """      }
}
"""
        workspace.append(finalClosing)

        val returnFile = Models.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileExtension(Models.FileExtension.TS_EXT)
                .setFileName(interfaceName)
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }
}