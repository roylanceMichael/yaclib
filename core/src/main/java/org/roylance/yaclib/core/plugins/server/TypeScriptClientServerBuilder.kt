package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.nio.file.Paths

class TypeScriptClientServerBuilder(private val location: String): IBuilder<Boolean> {
    override fun build(): Boolean {
        val javascriptDirectory = Paths.get(location, CommonTokens.ServerApi, "src", "main", "javascript").toFile()
        println("restoring server npm")
        val restoreDependenciesReport = TypeScriptUtilities.restoreDependencies(javascriptDirectory.toString())
        println(restoreDependenciesReport.normalOutput)
        println(restoreDependenciesReport.errorOutput)

        println("gulping server npm")
        val gulpReport = FileProcessUtilities.executeProcess(javascriptDirectory.toString(), "gulp", "")
        println(gulpReport.normalOutput)
        println(gulpReport.errorOutput)

        return true
    }
}