package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.PythonUtilities
import org.roylance.yaclib.core.utilities.StringUtilities
import java.util.*

class PythonServiceImplementationBuilder(private val dependency: YaclibModel.Dependency,
                                         private val controller: YaclibModel.Controller): IBuilder<YaclibModel.File> {
    private val initialTemplate = """import base64
import requests
${buildImportStatements()}


class ${controller.name}${CommonTokens.ServiceName}(object):
    def __init__(self, base_url):
        self.base_url = base_url

${buildFunctions()}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileExtension(YaclibModel.FileExtension.PY_EXT)
                .setFileName("${controller.name}${CommonTokens.ServiceName}")
                .setFullDirectoryLocation(PythonUtilities.buildPackageName(dependency))
                .build()

        return returnFile
    }

    private fun buildImportStatements(): String {
        val uniqueFiles = HashSet<String>()

        controller.actionsList.forEach { action ->
            action.inputsList.forEach { input ->
                uniqueFiles.add(PythonUtilities.convertPythonFileNameImport(input.fileName))
            }
            uniqueFiles.add(PythonUtilities.convertPythonFileNameImport(action.output.fileName))
        }

        return uniqueFiles.map { "import $it" }.joinToString("\n")
    }

    private fun buildFunctions(): String {
        val workspace = StringBuilder()

        controller.actionsList.forEach { action ->
            val firstArgument = action.inputsList.map { it.argumentName }.first()

            val actionTemplate = "\tdef ${action.name}($firstArgument):"
            workspace.appendln(actionTemplate)
            workspace.appendln("\t\tbase64_$firstArgument = base64.b64encode($firstArgument.SerializeToString())")

            val fullUrl = StringUtilities.buildUrl("/rest/${controller.name}/${action.name}")
            val functionImplementation = "\t\tresponse_call = requests.post(self.base_url + '$fullUrl', data = base64_$firstArgument)"
            workspace.appendln(functionImplementation)

            val outputFileName = PythonUtilities.convertPythonFileNameImport(action.output.fileName)

            workspace.appendln("\t\tresponse = $outputFileName.${action.output.messageClass}()")
            workspace.appendln("\t\tresponse.ParseFromString(base64.b64decode(response_call.text))")
            workspace.appendln("\t\treturn response")
            workspace.appendln()
        }

        return workspace.toString()
    }
}