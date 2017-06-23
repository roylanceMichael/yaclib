package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class TypeScriptServiceImplementationJsonBuilder(private val controller: YaclibModel.Controller,
                                                 private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """${CommonTokens.DoNotAlterMessage}
import {$interfaceName} from "./$interfaceName";
import {${HttpExecuteServiceBuilder.FileName}} from "./${HttpExecuteServiceBuilder.FileName}";
import {${TypeScriptUtilities.getFirstGroup(mainDependency.group)}} from "./${mainDependency.typescriptModelFile}";

export class ${controller.name}${CommonTokens.ServiceName} implements $interfaceName {
    ${HttpExecuteServiceBuilder.VariableName}:${HttpExecuteServiceBuilder.FileName};

    constructor(${HttpExecuteServiceBuilder.VariableName}:${HttpExecuteServiceBuilder.FileName}) {
        this.${HttpExecuteServiceBuilder.VariableName} = ${HttpExecuteServiceBuilder.VariableName};
    }
"""
        workspace.append(initialTemplate)
        controller.actionsList.forEach { action ->
            // for now, only processing one input and one output
            if (action.inputsCount == 1) {
                val colonSeparatedInputs = action.inputsList.map { input ->
                    "${input.argumentName}: ${input.filePackage}.${input.messageClass}"
                }.joinToString()

                val actionTemplate = "\t${action.name}($colonSeparatedInputs, onSuccess:(response: ${action.output.filePackage}.${action.output.messageClass})=>void, onError:(response:any)=>void) {"

                val fullUrl = StringUtilities.buildUrl("/rest/${controller.name}/${action.name}")
                val functionTemplate = """
            const self = this;
            this.${HttpExecuteServiceBuilder.VariableName}.performPost("$fullUrl",
                    ${action.inputsList.first().argumentName},
                    onSuccess,
                    onError);
        }
"""
                workspace.append(actionTemplate)
                workspace.append(functionTemplate)
            }
        }
        workspace.append("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileExtension(YaclibModel.FileExtension.TS_EXT)
                .setFileName("${controller.name}${CommonTokens.ServiceName}")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }
}