package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities
import java.nio.file.Paths

class PythonBuilder(private val location: String,
    private val mainDependency: YaclibModel.Dependency) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val pythonDirectory = Paths.get(this.location, CommonTokens.PythonName).toFile()
    println(InitUtilities.buildPhaseMessage("python client begin"))

    // custom to yaclib
    println(InitUtilities.buildPhaseMessage("building protobufs for pip"))
    val generateProtoReport = PythonUtilities.buildProtobufs(location, mainDependency)
    println(generateProtoReport.normalOutput)
    println(generateProtoReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("packaging pip"))
    val packageReport = PythonUtilities.buildPackage(pythonDirectory.toString(), mainDependency)
    println(packageReport.normalOutput)
    println(packageReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("deploying pip"))
    val deployReport = PythonUtilities.publish(pythonDirectory.toString(), mainDependency)
    println(deployReport.normalOutput)
    println(deployReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("python client end"))

    return true
  }
}