package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import java.nio.file.Paths

class CSharpBuilder(private val location: String,
                    private val mainDependency: YaclibModel.Dependency,
                    private val nugetKey: String?): IBuilder<Boolean> {
    override fun build(): Boolean {
        val csharpDirectory = Paths.get(this.location, CommonTokens.CSharpName, CSharpUtilities.buildFullName(mainDependency)).toFile()

        // custom to yaclib process
        println("building protobufs for nuget")
        val generateProtoProcess = CSharpUtilities.buildProtobufs(this.location, mainDependency)
        FileProcessUtilities.handleProcess(generateProtoProcess, "generateProtoProcess")

        println("restoring nuget")
        val restoreReport = CSharpUtilities.restoreDependencies(csharpDirectory.toString())
        println(restoreReport.normalOutput)
        println(restoreReport.errorOutput)

        println("building nuget")
        val buildReport = CSharpUtilities.build(csharpDirectory.toString())
        println(buildReport.normalOutput)
        println(buildReport.errorOutput)

        println("packing nuget")
        val packReport = CSharpUtilities.buildPackage(csharpDirectory.toString(), mainDependency)
        println(packReport.normalOutput)
        println(packReport.errorOutput)

        if (nugetKey != null) {
            println("publishing nuget")
            val publishReport = CSharpUtilities.buildPublish(csharpDirectory.toString(), mainDependency, nugetKey)
            println(publishReport.normalOutput)
            println(publishReport.errorOutput)
        }

        return true
    }
}