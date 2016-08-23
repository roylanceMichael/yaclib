package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import java.nio.file.Paths

object CSharpUtilities {
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
}