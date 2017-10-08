package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.MavenUtilities
import java.nio.file.Paths

class JavaServerBuilder(private val location: String,
    private val mainDependency: YaclibModel.Dependency,
    private val serverType: YaclibModel.ServerType) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val javaServerDirectory = Paths.get(location, "${mainDependency.name}${CommonTokens.ServerSuffix}").toFile()
    println(InitUtilities.buildPhaseMessage("java server begin"))

    if (serverType == YaclibModel.ServerType.GRADLE_JETTY_EMBEDDED) {
      val cleanReport = GradleUtilities.clean(javaServerDirectory.toString())
      println(cleanReport.normalOutput)
      println(cleanReport.errorOutput)

      println(InitUtilities.buildPhaseMessage("compiling"))
      val compileReport = GradleUtilities.build(javaServerDirectory.toString())
      println(compileReport.normalOutput)
      println(compileReport.errorOutput)
    } else {
      val cleanReport = MavenUtilities.clean(javaServerDirectory.toString())
      println(cleanReport.normalOutput)
      println(cleanReport.errorOutput)

      println(InitUtilities.buildPhaseMessage("compiling"))
      val compileReport = MavenUtilities.build(javaServerDirectory.toString())
      println(compileReport.normalOutput)
      println(compileReport.errorOutput)

      println(InitUtilities.buildPhaseMessage("packaging"))
      val packageReport = MavenUtilities.buildPackage(javaServerDirectory.toString(),
          YaclibModel.Dependency.getDefaultInstance())
      println(packageReport.normalOutput)
      println(packageReport.errorOutput)
    }

    println(InitUtilities.buildPhaseMessage("java server end"))
    return true
  }
}