package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.nio.file.Paths

class TypeScriptClientServerBuilder(private val location: String,
    private val apiProjectName: String) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val javascriptDirectory = Paths.get(location, "${apiProjectName}${CommonTokens.ServerSuffix}", "src", "main",
        "javascript").toFile()
    println(InitUtilities.buildPhaseMessage("typescript server begin"))

    val projectModel = TypeScriptUtilities.buildNpmModel(
        javascriptDirectory.toString()) ?: return false
    val shouldInstallAnon = projectModel.dependencies.values.any {
      it.startsWith("https:") && it.endsWith(".tar.gz")
    }

    println(InitUtilities.buildPhaseMessage("restoring: (installing anon: $shouldInstallAnon)"))
    val restoreDependenciesReport = TypeScriptUtilities.restoreDependencies(
        javascriptDirectory.toString(), shouldInstallAnon)
    println(restoreDependenciesReport.normalOutput)
    println(restoreDependenciesReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("gulping"))
    val gulpReport = FileProcessUtilities.executeProcess(javascriptDirectory.toString(), "gulp", "")
    println(gulpReport.normalOutput)
    println(gulpReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("typescript server end"))

    return true
  }
}