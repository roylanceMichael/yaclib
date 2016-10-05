package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.GradleUtilities
import java.nio.file.Paths

class JavaClientBuilder(private val location: String,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        val javaClientDirectory = Paths.get(this.location, CommonTokens.ClientApi).toFile()

        println("building gradle")
        val buildReport = GradleUtilities.build(javaClientDirectory.toString())
        println(buildReport.normalOutput)
        println(buildReport.errorOutput)

        println("publishing gradle")
        val publishReport = GradleUtilities.buildPublish(javaClientDirectory.toString(), mainDependency)
        println(publishReport.normalOutput)
        println(publishReport.errorOutput)

        return true
    }
}