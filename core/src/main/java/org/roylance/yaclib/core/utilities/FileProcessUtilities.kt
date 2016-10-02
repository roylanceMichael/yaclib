package org.roylance.yaclib.core.utilities

import java.io.File
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
}