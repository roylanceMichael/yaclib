package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.StringUtilities

class CSharpServiceBuilder(private val dependency: YaclibModel.Dependency,
    private val controller: YaclibModel.Controller) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val workspace = StringBuilder()
    val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

    val initialTemplate = """${CommonTokens.DoNotAlterMessage}
using System.Threading.Tasks;

namespace ${CSharpUtilities.buildFullName(dependency)}
{
    public interface $interfaceName
    {
"""
    workspace.append(initialTemplate)
    controller.actionsList.forEach { action ->
      val colonSeparatedInputs = action.inputsList.map { input ->
        "${StringUtilities.convertToPascalCase(
            input.filePackage)}.${input.messageClass} ${input.argumentName}"
      }.joinToString()

      val actionTemplate = "\t\tTask<${StringUtilities.convertToPascalCase(
          action.output.filePackage)}.${action.output.messageClass}> ${action.name}($colonSeparatedInputs);\n"
      workspace.append(actionTemplate)
    }

    workspace.appendln("\t}")
    workspace.appendln("}")

    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(workspace.toString())
        .setFileExtension(YaclibModel.FileExtension.CS_EXT)
        .setFileName(interfaceName)
        .setFullDirectoryLocation(CSharpUtilities.buildFullName(dependency))
        .build()

    return returnFile
  }

}