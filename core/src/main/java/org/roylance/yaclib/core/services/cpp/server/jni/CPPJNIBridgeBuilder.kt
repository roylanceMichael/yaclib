package org.roylance.yaclib.core.services.cpp.server.jni

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.StringUtilities

class CPPJNIBridgeBuilder(private val controller: YaclibModel.Controller,
                          private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val className = JavaUtilities.buildJavaBridgeName(controller)

    private val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${dependency.group}.${CommonTokens.ServicesName};

public class $className {
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
            val commaSeparatedInputs = action.inputsList.map { input ->
                "byte[] ${input.argumentName}"
            }.joinToString()

            val commaSeparatedNames = action.inputsList.map { it.argumentName }.joinToString()

            val actionTemplate = "\tpublic byte[] ${action.name}($commaSeparatedInputs) {\n"
            workspace.append(actionTemplate)

            workspace.appendln("\t\treturn ${action.name}JNI($commaSeparatedNames);")
            workspace.appendln("\t}");
        }

        workspace.appendln("}")

        val returnFile = YaclibModel.File.newBuilder()
                .setFileName(className)
                .setFileExtension(YaclibModel.FileExtension.JAVA_EXT)
                .setFileToWrite(workspace.toString())
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(
                        dependency.group,
                        CommonTokens.ServicesName))

        return returnFile.build()
    }
}