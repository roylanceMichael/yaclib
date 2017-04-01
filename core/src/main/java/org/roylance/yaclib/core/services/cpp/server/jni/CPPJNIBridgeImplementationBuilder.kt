package org.roylance.yaclib.core.services.cpp.server.jni

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class CPPJNIBridgeImplementationBuilder(
        private val controller: YaclibModel.Controller,
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val className = "${controller.name}${CommonTokens.ServiceName}JNI"
    private val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${dependency.group}.${CommonTokens.ServicesName};

class $className: ${StringUtilities.convertServiceNameToInterfaceName(controller)} {
    private val bridge = ${controller.name}${CommonTokens.ServiceName}JNIBridge();
"""
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder(initialTemplate)

        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.argumentName}: ${input.filePackage}.${input.fileClass}.${input.messageClass}"
            }.joinToString()

            val actionTemplate = "\toverride fun ${action.name}($colonSeparatedInputs): ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass} {\n"
            workspace.append(actionTemplate)

            val firstInput = action.inputsList.first().argumentName

            workspace.appendln("\t\tval bytes = $firstInput.toByteArray();")
            workspace.appendln("\t\tval result = this.bridge.${action.name}(bytes);")
            workspace.appendln("\t\treturn ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass}.parseFrom(result);")
            workspace.appendln("\t}");
        }

        workspace.appendln("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileName(initialTemplate)
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFileToWrite(workspace.toString())
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(
                        dependency.group,
                        CommonTokens.ServicesName))

        return returnFile.build()
    }
}