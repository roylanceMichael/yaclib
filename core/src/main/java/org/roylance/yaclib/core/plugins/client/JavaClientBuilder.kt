package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import java.nio.file.Paths

class JavaClientBuilder(private val location: String,
    private val mainDependency: YaclibModel.Dependency) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val javaClientDirectory = Paths.get(this.location, "${mainDependency.name}${CommonTokens.ClientSuffix}").toFile()

    println(InitUtilities.buildPhaseMessage("java client begin"))

    println(InitUtilities.buildPhaseMessage("building gradle"))
    val buildReport = GradleUtilities.build(javaClientDirectory.toString())
    println(buildReport.normalOutput)
    println(buildReport.errorOutput)

    // todo: will fix this so it is optional to publish or not
    println(InitUtilities.buildPhaseMessage("publishing gradle"))
    val publishReport = GradleUtilities.publish(javaClientDirectory.toString(), mainDependency)
    println(publishReport.normalOutput)
    println(publishReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("java client end"))

    return true
  }
}