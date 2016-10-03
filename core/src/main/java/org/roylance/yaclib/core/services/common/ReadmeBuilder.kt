package org.roylance.yaclib.core.services.common

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class ReadmeBuilder(mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """${mainDependency.group}.${mainDependency.name}
================
This library and readme was auto-generated and auto-published by [Yaclib](https://github.com/roylanceMichael/yaclib). This TypeScript library contains the interfaces and implementations to communicate with the corresponding rest server in Java. More documentation to come...
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.MD_EXT)
                .setFileName("README")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }
}