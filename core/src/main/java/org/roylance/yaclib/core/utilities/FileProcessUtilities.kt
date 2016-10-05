package org.roylance.yaclib.core.utilities

import org.apache.commons.io.IOUtils
import org.roylance.yaclib.YaclibModel
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.nio.charset.Charset
import java.util.*

object FileProcessUtilities {
    fun buildCommand(application: String, allArguments: String, resolveWithWhich: Boolean = true):List<String> {
        val returnList = ArrayList<String>()

        returnList.add("/bin/bash")
        returnList.add("-l")
        returnList.add("-c")

        if (resolveWithWhich) {
            if (allArguments.length == 0) {
                returnList.add("\"$(which $application)\"")
            }
            else {
                returnList.add("\"$($(which $application) $allArguments)\"")
            }
        }
        else {
            if (allArguments.length == 0) {
                returnList.add("\"application\"")
            }
            else {
                returnList.add("\"$($application $allArguments)\"")
            }
        }

        return returnList
    }

    fun readFile(path: String): String {
        val foundFile = File(path)
        return foundFile.readText()
    }

    fun writeFile(file: String, path: String) {
        val newFile = File(path)
        newFile.writeText(file)
    }

    fun handleProcess(process: ProcessBuilder, name: String) {
        try {
            process.redirectError(ProcessBuilder.Redirect.INHERIT)
            process.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            process.start().waitFor()
            println("finished $name")
        }
        catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun buildReport(process: Process): YaclibModel.ProcessReport {
        val returnReport = YaclibModel.ProcessReport.newBuilder()

        val inputWriter = StringWriter()
        val errorWriter = StringWriter()

        IOUtils.copy(process.inputStream, inputWriter, Charset.defaultCharset())
        IOUtils.copy(process.errorStream, errorWriter, Charset.defaultCharset())

        return returnReport.setErrorOutput(errorWriter.toString()).setNormalOutput(inputWriter.toString()).build()
    }

    fun executeProcess(location: String, application: String, allArguments: String, resolveWithWhich: Boolean = true): YaclibModel.ProcessReport {
        val command = buildCommand(application, allArguments, resolveWithWhich)

        val process = ProcessBuilder()
            .command(command)
            .directory(File(location))
            .start()

        process.waitFor()

        return buildReport(process)
    }
}