package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.MavenUtilities
import java.nio.file.Paths

class JavaServerBuilder(private val location: String): IBuilder<Boolean> {
    override fun build(): Boolean {
        val javaServerDirectory = Paths.get(location, CommonTokens.ServerApi).toFile()
        println(InitUtilities.buildPhaseMessage("java server begin"))

        val cleanReport = MavenUtilities.clean(javaServerDirectory.toString())
        println(cleanReport.normalOutput)
        println(cleanReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("compiling"))
        val compileReport = MavenUtilities.build(javaServerDirectory.toString())
        println(compileReport.normalOutput)
        println(compileReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("packaging"))
        val packageReport = MavenUtilities.buildPackage(javaServerDirectory.toString(), YaclibModel.Dependency.getDefaultInstance())
        println(packageReport.normalOutput)
        println(packageReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("java server end"))

        return true
    }
}