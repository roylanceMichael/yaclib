package org.roylance.yaclib.core.services.java.server

import com.google.protobuf.util.JsonFormat
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class JavaRestJsonBuilder(
        private val controller: YaclibModel.Controller,
        private val dependency: YaclibModel.Dependency,
        private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val lowercaseName = controller.name.toLowerCase()
        val serviceName = StringUtilities.convertServiceNameToVariableName(controller)
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${mainDependency.group}.${CommonTokens.RestName};

import org.roylance.common.service.IProtoSerializerService;

import ${mainDependency.group}.${CommonTokens.ServiceLocatorLocation};
import ${dependency.group}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)};

import com.google.protobuf.util.JsonFormat;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;

@Path("/$lowercaseName")
public class ${controller.name}Controller {
    @Context
    private ServletContext context;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    private final $interfaceName $serviceName;
    private final JsonFormat.Parser parser;
    private final JsonFormat.Printer printer;

    public ${controller.name}Controller() {
        this.parser = JsonFormat.parser();
        this.printer = JsonFormat.printer();
        this.$serviceName = ${CommonTokens.ServiceLocatorSingletonName}.INSTANCE.${StringUtilities.convertServiceNameToJavaCall(controller)}();
    }
"""
        workspace.append(initialTemplate)
        JsonFormat.parser()
        controller.actionsList.forEach { action ->
            var colonSeparatedInputs = action.inputsList.map { input ->
                "String ${input.argumentName}"
            }.joinToString()

            if (colonSeparatedInputs.isNotEmpty()) {
                colonSeparatedInputs = ", " + colonSeparatedInputs
            }

            val lowercaseActionName = StringUtilities.buildUrl(action.name.toLowerCase())

            val actionVariableWorkspace = StringBuilder()
            val allActualArgumentNames = action.inputsList.map { input -> "${input.argumentName}Actual" }.joinToString()

            action.inputsList.forEach { input ->
                val deserializeTemplate = """
            final ${input.filePackage}.${input.fileClass}.${input.messageClass}.Builder ${input.argumentName}Temp = ${input.filePackage}.${input.fileClass}.${input.messageClass}.newBuilder();
            this.parser.merge(${input.argumentName}, ${input.argumentName}Temp);
            final ${input.filePackage}.${input.fileClass}.${input.messageClass} ${input.argumentName}Actual = ${input.argumentName}Temp.build();
"""
                actionVariableWorkspace.append(deserializeTemplate)
            }

            val executeWorkspace = """
            final ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass} response = this.$serviceName.${action.name}($allActualArgumentNames);
            final String serializedResponse = this.printer.print(response);
            asyncResponse.resume(serializedResponse);
"""
            actionVariableWorkspace.append(executeWorkspace)

            val actionTemplate = """
    @POST
    @Path("/$lowercaseActionName")
    public void ${action.name}(@Suspended AsyncResponse asyncResponse$colonSeparatedInputs) throws Exception {
        new Thread(() -> {
            $actionVariableWorkspace
        }).start();
    }
"""
            workspace.append(actionTemplate)
        }

        workspace.append("}")
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileName("${controller.name}Controller")
                .setFileExtension(YaclibModel.FileExtension.JAVA_EXT)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(mainDependency.group,
                        CommonTokens.RestName))

        return returnFile.build()
    }
}