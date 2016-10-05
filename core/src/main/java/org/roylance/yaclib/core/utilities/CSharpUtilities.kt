package org.roylance.yaclib.core.utilities

import com.google.gson.Gson
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DotNetProject
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.File
import java.nio.file.Paths

object CSharpUtilities: IProjectBuilderServices {
    const val ProtobufVersion = "3.0.0"

    private const val DotNet = "dotnet"
    private const val Nuget = "nuget"

    private val gson = Gson()

    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        val projectJson = File(location, "project.json")
        val allText = projectJson.readText()

        val actualJson = gson.fromJson(allText, DotNetProject::class.java)
        val returnModel = YaclibModel.ProcessReport.newBuilder().setContent(actualJson.version)
        return returnModel.build()
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        FileProcessUtilities.executeProcess(location, "rm", "-rf bin")
        return FileProcessUtilities.executeProcess(location, "rm", "-rf obj")
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, DotNet, "build")
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, DotNet, "pack")
    }

    override fun buildPublish(location: String, dependency: YaclibModel.Dependency, apiKey: String): YaclibModel.ProcessReport {
        val nugetDirectoryLocation = Paths.get(location, "bin", "Debug").toString()
        val nugetPackage = buildNugetPackageName(dependency)
        return FileProcessUtilities.executeProcess(nugetDirectoryLocation, Nuget, "push $nugetPackage $apiKey")
    }

    override fun restoreDependencies(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, DotNet, "restore")
    }

    fun buildFullName(dependency: YaclibModel.Dependency):String {
        return StringUtilities.convertToPascalCase("${dependency.group}.${dependency.name}")
    }

    fun buildProtobufs(location: String, mainDependency: YaclibModel.Dependency): ProcessBuilder {
        val csharpDirectory = Paths.get(location, CommonTokens.CSharpName, CSharpUtilities.buildFullName(mainDependency)).toFile()

        val protobufLocation = Paths.get(location, CommonTokens.ApiName, "src", "main", "resources").toString()
        val arguments = "-I=$protobufLocation --proto_path=$protobufLocation --csharp_out=${csharpDirectory.toString()} $protobufLocation/*.proto"
        val generateProtoProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand("protoc", arguments))

        return generateProtoProcess
    }

    fun buildNugetPackageName(dependency: YaclibModel.Dependency): String {
        return "${buildFullName(dependency)}.${dependency.majorVersion}.${dependency.minorVersion}.0.nupkg"
    }
}