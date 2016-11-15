package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class MavenProcBuilder : IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val initialTemplate = """#!/usr/bin/env bash
if [ -d "/app/.jdk" ]; then
    export JAVA_HOME=/app/.jdk
fi

sh target/bin/webapp
"""
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileName("proc")
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileExtension(YaclibModel.FileExtension.SH_EXT)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }
}