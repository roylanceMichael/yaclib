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

        val generateProtoProcess = CSharpUtilities.buildProtobufs(this.location, mainDependency)
        FileProcessUtilities.handleProcess(generateProtoProcess, "generateProtoProcess")

        val dotnetRestoreProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "restore"))
        FileProcessUtilities.handleProcess(dotnetRestoreProcess, "dotnetRestoreProcess")

        val dotnetBuildProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "build"))
        FileProcessUtilities.handleProcess(dotnetBuildProcess, "dotnetBuildProcess")

        val dotnetPackProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "pack"))
        FileProcessUtilities.handleProcess(dotnetPackProcess, "dotnetPackProcess")

        if (nugetKey != null) {
            val nugetDirectoryLocation = Paths.get(csharpDirectory.toString(), "bin", "Debug").toFile()
            val nugetPackageName = "${CSharpUtilities.buildFullName(mainDependency)}.${mainDependency.majorVersion}.${mainDependency.minorVersion}.0.nupkg"
            val dotnetPublishProcess = ProcessBuilder()
                    .directory(nugetDirectoryLocation)
                    .command(FileProcessUtilities.buildCommand(Nuget, "push $nugetPackageName ${this.nugetKey}"))
            FileProcessUtilities.handleProcess(dotnetPublishProcess, "dotnetPublishProcess")
        }

        return true
    }

    companion object {
        private const val Nuget = "nuget"
        private const val DotNet = "dotnet"
    }
}