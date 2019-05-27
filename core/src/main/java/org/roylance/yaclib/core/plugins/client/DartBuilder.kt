package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.DartUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import java.nio.file.Paths

class DartBuilder(private val location: String,
  private val mainDependency: YaclibModel.Dependency) : IBuilder<Boolean> {
  override fun build(): Boolean {
    println(InitUtilities.buildPhaseMessage("dart client begin"))

    val dartDirectory = Paths.get(this.location, "${mainDependency.name}${CommonTokens.DartSuffix}",
        CSharpUtilities.buildFullName(mainDependency)).toFile()

    // custom to yaclib process
    println(InitUtilities.buildPhaseMessage("building protobufs for nuget"))
    val generateProtoProcessReport = DartUtilities.buildProtobufs(this.location, mainDependency)
    println(generateProtoProcessReport.normalOutput)
    println(generateProtoProcessReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("restore dart deps"))
    val restoreReport = DartUtilities.restoreDependencies(dartDirectory.toString())
    println(restoreReport.normalOutput)
    println(restoreReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("build dart"))
    val buildReport = DartUtilities.build(dartDirectory.toString())
    println(buildReport.normalOutput)
    println(buildReport.errorOutput)


    return true
  }
}