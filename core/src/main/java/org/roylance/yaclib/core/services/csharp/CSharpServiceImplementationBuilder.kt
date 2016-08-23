package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.StringUtilities

class CSharpServiceImplementationBuilder(
        private val dependency: YaclibModel.Dependency,
        private val controller: YaclibModel.Controller
): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """using System.Threading.Tasks;
using Google.Protobuf;

namespace ${CSharpUtilities.buildFullName(dependency)}
{
    public class ${controller.name}${CommonTokens.ServiceName}: $interfaceName
    {
        private readonly ${CommonTokens.HttpExecute} ${CommonTokens.HttpExecuteVariableName};
        public ${controller.name}${CommonTokens.ServiceName}(${CommonTokens.HttpExecute} ${CommonTokens.HttpExecuteVariableName})
        {
            this.${CommonTokens.HttpExecuteVariableName} = ${CommonTokens.HttpExecuteVariableName};
        }
"""

        workspace.append(initialTemplate)
        controller.actionsList.forEach { action ->
            val argumentsList = action.inputsList.map { input ->
                "${StringUtilities.convertToPascalCase(input.filePackage)}.${input.messageClass} ${input.argumentName}"
            }.joinToString()



            val base64Variables = action.inputsList.map { input ->
                "var base64${input.argumentName} = System.Convert.ToBase64String(${input.argumentName}.ToByteArray());"
            }.joinToString("\n")

            val base64InputParameters = action.inputsList.map { input ->
                "base64${input.argumentName}"
            }.joinToString()

            val fullUrl = StringUtilities.buildUrl("/rest/${controller.name}/${action.name}")
            val initialActionTemplate = """
        public async Task<${StringUtilities.convertToPascalCase(action.output.filePackage)}.${action.output.messageClass}> ${action.name}($argumentsList)
        {
            $base64Variables
            var responseCall = await this.${CommonTokens.HttpExecuteVariableName}.PostAsync("$fullUrl", $base64InputParameters);
            var bytes = System.Convert.FromBase64String(responseCall);
            return ${StringUtilities.convertToPascalCase(action.output.filePackage)}.${action.output.messageClass}.Parser.ParseFrom(bytes);
        }
"""
            workspace.append(initialActionTemplate)
        }

        workspace.appendln("\t}")
        workspace.append("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileExtension(YaclibModel.FileExtension.CS_EXT)
                .setFileName("${controller.name}${CommonTokens.ServiceName}")
                .setFullDirectoryLocation(CSharpUtilities.buildFullName(dependency))
                .build()

        return returnFile
    }
}