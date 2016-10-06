package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities
import java.nio.file.Paths

class PythonBuilder(private val location: String,
                    private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        val pythonDirectory = Paths.get(this.location, CommonTokens.PythonName).toFile()

        // custom to yaclib
        println("building protobufs for pip")
        val generateProtoProcess = PythonUtilities.buildProtobufs(location, mainDependency)
        FileProcessUtilities.handleProcess(generateProtoProcess, "generateProtoProcess")

        println("packaging pip")
        val packageReport = PythonUtilities.buildPackage(pythonDirectory.toString(), mainDependency)
        println(packageReport.normalOutput)
        println(packageReport.errorOutput)

        println("deploying pip")
        val deployReport = PythonUtilities.publish(pythonDirectory.toString(), mainDependency)
        println(deployReport.normalOutput)
        println(deployReport.errorOutput)

        return true
    }
}