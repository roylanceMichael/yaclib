package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class TypeScriptServiceImplementationBuilder(private val controller: Models.Controller,
                                             private val overallPackage: String): IBuilder<Models.File> {
    override fun build(): Models.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """import {$interfaceName} from "./$interfaceName";
import {IHttpExecuteService} from "./${HttpExecuteServiceBuilder.FileName}";
import ProtoBufBuilder = $overallPackage.ProtoBufBuilder;

declare module $overallPackage.services {
    export class ${controller.name}${CommonTokens.ServiceName} implements $interfaceName {
        httpExecuteService:IHttpExecuteService;
        modelFactory:ProtoBufBuilder;

        constructor(httpExecuteService:IHttpExecuteService,
                    modelFactory:ProtoBufBuilder) {
            this.httpExecuteService = httpExecuteService;
            this.modelFactory = modelFactory;
        }
"""
        workspace.append(initialTemplate)
        controller.actionsList.forEach { action ->
            // for now, only processing one input and one output
            if (action.inputsCount == 1) {
                val colonSeparatedInputs = action.inputsList.map { input ->
                    "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
                }.joinToString()

                val actionTemplate = "\t\t${action.name}($colonSeparatedInputs, onSuccess:(response: ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass})=>void, onError:(response:any)=>void) {"

                val fullUrl = StringUtilities.buildUrl("/rest/${controller.name}/${action.name}")
                val functionTemplate = """
            const self = this;
            this.httpExecuteService.performPost("$fullUrl",
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

        return returnFile    }
}