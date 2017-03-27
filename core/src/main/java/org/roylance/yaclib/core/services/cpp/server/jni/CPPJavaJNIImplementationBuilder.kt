package org.roylance.yaclib.core.services.cpp.server.jni

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class CPPJavaJNIImplementationBuilder(
        private val controller: YaclibModel.Controller,
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val className = "${controller.name}${CommonTokens.ServiceName}JNI"
    private val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${dependency.group}.${CommonTokens.ServicesName};

public class $className implements ${StringUtilities.convertServiceNameToInterfaceName(controller)} {
"""
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder(initialTemplate)

        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "byte[] ${input.argumentName}"
            }.joinToString()

            val actionTemplate = "\tprivate native byte[] ${action.name}JNI($colonSeparatedInputs);\n"
            workspace.append(actionTemplate)
        }

        controller.actionsList.forEach { action ->
            val colonSeparatedInputs = action.inputsList.map { input ->
                "${input.filePackage}.${input.fileClass}.${input.messageClass} ${input.argumentName}"
            }.joinToString()

            val actionTemplate = "\t@Override public ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass} ${action.name}($colonSeparatedInputs) throws InvalidProtocolBufferException {\n"
            workspace.append(actionTemplate)

            val firstInput = action.inputsList.first().argumentName

            workspace.appendln("\t\tfinal byte[] bytes = $firstInput.toByteArray();")
            workspace.appendln("\t\tfinal byte[] result = ${action.name}JNI(bytes);")
            workspace.appendln("\t\treturn ${action.output.filePackage}.${action.output.fileClass}.${action.output.messageClass}.parseFrom(result);")
            workspace.appendln("\t}");
        }

        workspace.appendln("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileName(initialTemplate)
                .setFileExtension(YaclibModel.FileExtension.JAVA_EXT)
                .setFileToWrite(workspace.toString())
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(
                        dependency.group,
                        CommonTokens.ServicesName))

        return returnFile.build()
    }
}