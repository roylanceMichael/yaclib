package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel.Dependency
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import java.nio.file.Paths

class JavaServerBuilder(private val location: String,
    private val mainDependency: Dependency) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val javaServerDirectory = Paths.get(location, "${mainDependency.name}${CommonTokens.ServerSuffix}").toFile()
    println(InitUtilities.buildPhaseMessage("java server begin"))

    // todo: just sticking with jetty
    val cleanReport = GradleUtilities.clean(javaServerDirectory.toString())
    println(cleanReport.normalOutput)
    println(cleanReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("compiling"))
    val compileReport = GradleUtilities.build(javaServerDirectory.toString())
    println(compileReport.normalOutput)
    println(compileReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("java server end"))
    return true
  }
}