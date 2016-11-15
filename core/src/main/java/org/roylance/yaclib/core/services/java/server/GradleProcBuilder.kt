package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class GradleProcBuilder(private val appName: String): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val initialTemplate = """#!/usr/bin/env bash
#${CommonTokens.AutoGeneratedAlteringOkay}
if [ -d "/app/.jdk" ]; then
    export JAVA_HOME=/app/.jdk
fi
sh custom.sh
sh bin/$appName
"""
        return YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileName("proc")
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileExtension(YaclibModel.FileExtension.SH_EXT)
                .setFullDirectoryLocation("")
                .build()
    }
}