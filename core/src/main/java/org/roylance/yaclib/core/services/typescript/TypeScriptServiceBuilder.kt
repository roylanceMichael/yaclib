package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class TypeScriptServiceBuilder(
    private val controller: YaclibModel.Controller) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val workspace = StringBuilder()
    val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

    val initialTemplate = """${CommonTokens.DoNotAlterMessage}
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

    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(workspace.toString())
        .setFileExtension(YaclibModel.FileExtension.TS_EXT)
        .setFileName(interfaceName)
        .setFullDirectoryLocation("")
        .build()

    return returnFile
  }
}