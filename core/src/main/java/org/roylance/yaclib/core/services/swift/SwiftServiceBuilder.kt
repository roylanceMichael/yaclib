package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftServiceBuilder(private val controller: YaclibModel.Controller): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """${CommonTokens.DoNotAlterMessage}
import Foundation

public protocol $interfaceName {
"""
        workspace.append(initialTemplate)

        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.argumentName}: ${SwiftUtilities.buildSwiftFullName(input)}"
            }.joinToString()

            val actionTemplate = "\tfunc ${action.name}($colonSeparatedInputs, onSuccess: @escaping (_ response: ${SwiftUtilities.buildSwiftFullName(action.output).trim()}) -> Void, onError: @escaping (_ response: String) -> Void)\n"
            workspace.append(actionTemplate)
        }
        workspace.append("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileExtension(YaclibModel.FileExtension.SWIFT_EXT)
                .setFileName(interfaceName)
                .setFullDirectoryLocation("${CommonTokens.SwiftName}/Source")
                .build()
        return returnFile
    }
}