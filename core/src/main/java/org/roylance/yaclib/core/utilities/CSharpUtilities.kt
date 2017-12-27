package org.roylance.yaclib.core.utilities

import com.google.gson.Gson
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DotNetProject
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.File
import java.nio.file.Paths

object CSharpUtilities : IProjectBuilderServices {
  const val ProtobufVersion = "3.2.0"

  private val gson = Gson()

  override fun incrementVersion(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  override fun updateDependencyVersion(location: String,
      otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  override fun getVersion(location: String): YaclibModel.ProcessReport {
    val projectJson = File(location, "project.json")
    val allText = projectJson.readText()

    val actualJson = gson.fromJson(allText, DotNetProject::class.java)
    val returnModel = YaclibModel.ProcessReport.newBuilder().setContent(actualJson.version)
    return returnModel.build()
  }

  override fun setVersion(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  override fun clean(location: String): YaclibModel.ProcessReport {
    FileProcessUtilities.executeProcess(location, "rm", "-rf bin")
    return FileProcessUtilities.executeProcess(location, "rm", "-rf obj")
  }

  override fun build(location: String): YaclibModel.ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.DotNet, "build")
  }

  override fun buildPackage(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.DotNet, "pack")
  }

  override fun publish(location: String, dependency: YaclibModel.Dependency,
      apiKey: String): YaclibModel.ProcessReport {
    val nugetDirectoryLocation = Paths.get(location, "bin", "Debug").toString()
    val nugetPackage = buildNugetPackageName(dependency)

    if (dependency.unpublishNuget) {
      val pushReport = FileProcessUtilities.executeProcess(nugetDirectoryLocation,
          InitUtilities.Nuget,
          "push $nugetPackage $apiKey")
      val deleteReport = FileProcessUtilities.executeProcess(nugetDirectoryLocation,
          InitUtilities.Nuget,
          "delete ${buildFullName(
              dependency)} ${dependency.majorVersion}.${dependency.minorVersion}.0 $apiKey -NoPrompt")

      return pushReport.toBuilder()
          .setContent(pushReport.content + deleteReport.content)
          .setErrorOutput(pushReport.errorOutput + deleteReport.errorOutput)
          .build()
    }
    return FileProcessUtilities.executeProcess(nugetDirectoryLocation, InitUtilities.Nuget,
        "push $nugetPackage $apiKey")
  }

  override fun restoreDependencies(location: String,
      doAnonymously: Boolean): YaclibModel.ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.DotNet, "restore")
  }

  fun buildFullName(dependency: YaclibModel.Dependency): String {
    return StringUtilities.convertToPascalCase("${dependency.group}.${dependency.name}")
  }

  fun buildProtobufs(location: String,
      mainDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    val csharpDirectory = Paths.get(location, "${mainDependency.name}${CommonTokens.CSharpSuffix}",
        CSharpUtilities.buildFullName(mainDependency)).toFile()

    val protobufLocation = Paths.get(location, mainDependency.name, "src", "main",
        "proto").toString()
    val arguments = "-I=$protobufLocation --proto_path=$protobufLocation --csharp_out=$csharpDirectory $protobufLocation/*.proto"
    return FileProcessUtilities.executeProcess(csharpDirectory.toString(), InitUtilities.Protoc,
        arguments)
  }

  fun buildNugetPackageName(dependency: YaclibModel.Dependency): String {
    return "${buildFullName(
        dependency)}.${dependency.majorVersion}.${dependency.minorVersion}.0.nupkg"
  }
}