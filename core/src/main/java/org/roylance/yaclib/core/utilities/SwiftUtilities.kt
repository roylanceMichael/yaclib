package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.nio.file.Paths

object SwiftUtilities: IProjectBuilderServices {
    const val Carthage = "carthage"
    const val XCodeBuild = "xcodebuild"
    const val Clean = "clean"

    const val AlamofireFrameworkLocation = "Carthage/Build/iOS/Alamofire.framework"
    const val SwiftProtobufFrameworkLocation = "Carthage/Build/iOS/SwiftProtobuf.framework"

    const val Update = "update"

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, XCodeBuild, Clean)
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, XCodeBuild, "")
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, XCodeBuild, "")
    }

    override fun publish(location: String, dependency: YaclibModel.Dependency, apiKey: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, Carthage, Update)
    }

    fun addFrameworksToProject(dependency: YaclibModel.Dependency, location: String): YaclibModel.ProcessReport {
        val frameworksScript = addFrameworksPyScript(dependency, location)
        return FileProcessUtilities.executeScript(location, "python", frameworksScript)
    }

    fun addFilesToProject(dependency: YaclibModel.Dependency, location: String, files: List<String>): YaclibModel.ProcessReport {
        val filesScript = addFilesPyScript(dependency, files)
        println(filesScript)
        return FileProcessUtilities.executeScript(location, "python", filesScript)
    }

    fun buildSwiftFullName(message: YaclibModel.Message): String {
        val fullName = StringUtilities
                .convertToPascalCase("${message.filePackage}.${message.messageClass}")
                .replace(StringUtilities.Period, "_")
        return fullName
    }

    fun buildSwiftFullName(dependency: YaclibModel.Dependency): String {
        val fullName = StringUtilities
                .convertToPascalCase("${dependency.group}.${dependency.name}")
                .replace(StringUtilities.Period, "_")
        return fullName
    }

    fun buildProtobufs(location: String): YaclibModel.ProcessReport {
        val sourceDirectory = Paths.get(location, CommonTokens.SwiftName, "Source").toFile()
        val protobufLocation = Paths.get(location, CommonTokens.ApiName, "src", "main", "resources").toString()
        val arguments = "-I=$protobufLocation --proto_path=$protobufLocation --swift_out=$sourceDirectory $protobufLocation/*.proto"
        return FileProcessUtilities.executeProcess(sourceDirectory.toString(), InitUtilities.Protoc, arguments)
    }

    private fun addFrameworksPyScript(dependency: YaclibModel.Dependency, location: String): String {
        val script = """from pbxproj import XcodeProject
from pbxproj.XcodeProject import *

file_options = FileOptions(weak=True)
project = XcodeProject.load('${SwiftUtilities.buildSwiftFullName(dependency)}.xcodeproj/project.pbxproj')

project.add_file('$location/${CommonTokens.SwiftName}/$AlamofireFrameworkLocation', force=False, file_options=file_options)
project.add_file('$location/${CommonTokens.SwiftName}/$SwiftProtobufFrameworkLocation', force=False, file_options=file_options)

project.save()
"""
        return script
    }

    private fun addFilesPyScript(dependency: YaclibModel.Dependency, files: List<String>): String {
        val workspace = StringBuilder()
        workspace.appendln("""from pbxproj import XcodeProject
from pbxproj.XcodeProject import *

file_options = FileOptions(weak=True)
project = XcodeProject.load('${SwiftUtilities.buildSwiftFullName(dependency)}.xcodeproj/project.pbxproj')
""")
        files.forEach {
            workspace.appendln("project.add_file('$it', force=False, file_options=file_options)")
        }

        workspace.appendln("project.save()")
        return workspace.toString()
    }
}