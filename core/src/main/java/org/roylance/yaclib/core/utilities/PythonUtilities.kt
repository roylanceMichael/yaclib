package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import java.nio.file.Paths

object PythonUtilities {
    const val ProtobufName = "protobuf"

    fun convertPythonFileNameImport(fileName: String): String {
        val removedDotProto = fileName.replace(".proto", "")
        return "${removedDotProto}_pb2"
    }

    fun buildPackageName(dependency: YaclibModel.Dependency): String {
        val replacedName = replacePeriodWithUnderscore(dependency.group)
        return "${replacedName}_${dependency.name}"
    }

    fun buildPythonSourceDirectory(mainDependency: YaclibModel.Dependency): String {
        return Paths.get(CommonTokens.PythonName, buildPackageName(mainDependency)).toString()
    }

    fun buildPythonRootDirectory(): String {
        return CommonTokens.PythonName
    }

    fun buildWheelUrl(dependency: YaclibModel.Dependency): String {
        val newGroup = ArtifactoryUtilities.replacePeriodWithForwardSlash(dependency.group)
        return "$newGroup/${dependency.name}/${replacePeriodWithUnderscore(dependency.group)}_${dependency.name}-${dependency.majorVersion}-${dependency.minorVersion}-py2.py3-none-any.whl"
    }

    fun buildWheelFileName(dependency: YaclibModel.Dependency): String {
        return "${replacePeriodWithUnderscore(dependency.group)}_${dependency.name}-${dependency.majorVersion}-${dependency.minorVersion}-py2.py3-none-any.whl"
    }

    fun replacePeriodWithUnderscore(item: String): String {
        return item.replace(".", "_")
    }

    fun buildProtobufs(location: String, mainDependency: YaclibModel.Dependency): ProcessBuilder {
        val pythonDirectory = Paths.get(location, buildPythonSourceDirectory(mainDependency)).toFile()
        pythonDirectory.mkdirs()

        val protobufLocation = Paths.get(location, CommonTokens.ApiName, "src", "main", "resources").toString()
        val arguments = "-I=$protobufLocation --proto_path=$protobufLocation --python_out=$pythonDirectory $protobufLocation/*.proto"
        val generateProtoProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("protoc", arguments))

        return generateProtoProcess
    }
}