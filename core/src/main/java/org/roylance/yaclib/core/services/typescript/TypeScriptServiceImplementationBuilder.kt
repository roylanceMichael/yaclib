package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class TypeScriptServiceImplementationBuilder(private val controller: YaclibModel.Controller,
                                             private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """import {$interfaceName} from "./$interfaceName";
import {${HttpExecuteServiceBuilder.FileName}} from "./${HttpExecuteServiceBuilder.FileName}";
import ProtoBufBuilder = ${dependency.group}.ProtoBufBuilder;

export class ${controller.name}${CommonTokens.ServiceName} implements $interfaceName {
    ${HttpExecuteServiceBuilder.VariableName}:${HttpExecuteServiceBuilder.FileName};
    modelFactory:ProtoBufBuilder;

    constructor(${HttpExecuteServiceBuilder.VariableName}:${HttpExecuteServiceBuilder.FileName},
                modelFactory:ProtoBufBuilder) {
        this.${HttpExecuteServiceBuilder.VariableName} = ${HttpExecuteServiceBuilder.VariableName};
        this.modelFactory = modelFactory;
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
                    ${action.inputsList.first().argumentName}.toBase64(),
                    function(result:string) {
                        onSuccess(self.modelFactory.${action.output.messageClass}.decode64(result));
                    },
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

        return returnFile    }
}