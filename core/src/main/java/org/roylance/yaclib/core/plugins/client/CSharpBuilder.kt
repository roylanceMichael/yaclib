package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import java.nio.file.Paths

class CSharpBuilder(private val location: String,
    private val mainDependency: YaclibModel.Dependency,
    private val nugetKey: String?) : IBuilder<Boolean> {
  override fun build(): Boolean {
    println(InitUtilities.buildPhaseMessage("csharp client begin"))

    if (!InitUtilities.hasCSharp()) {
      println(InitUtilities.buildPhaseMessage(
          "${InitUtilities.DotNet} and ${InitUtilities.Nuget} not located, skipping csharp"))
      println(InitUtilities.buildPhaseMessage("csharp client end"))
      return false
    }

    val csharpDirectory = Paths.get(this.location, "${mainDependency.name}${CommonTokens.CSharpSuffix}",
        CSharpUtilities.buildFullName(mainDependency)).toFile()

    // custom to yaclib process
    println(InitUtilities.buildPhaseMessage("building protobufs for nuget"))
    val generateProtoProcessReport = CSharpUtilities.buildProtobufs(this.location, mainDependency)
    println(generateProtoProcessReport.normalOutput)
    println(generateProtoProcessReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("restore nuget"))
    val restoreReport = CSharpUtilities.restoreDependencies(csharpDirectory.toString())
    println(restoreReport.normalOutput)
    println(restoreReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("build nuget"))
    val buildReport = CSharpUtilities.build(csharpDirectory.toString())
    println(buildReport.normalOutput)
    println(buildReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("packing nuget"))
    val packReport = CSharpUtilities.buildPackage(csharpDirectory.toString(), mainDependency)
    println(packReport.normalOutput)
    println(packReport.errorOutput)

    if (nugetKey != null) {
      println(InitUtilities.buildPhaseMessage("publishing nuget"))
      val publishReport = CSharpUtilities.publish(csharpDirectory.toString(), mainDependency,
          nugetKey)
      println(publishReport.normalOutput)
      println(publishReport.errorOutput)
    }

    println(InitUtilities.buildPhaseMessage("csharp client end"))
    return true
  }
}