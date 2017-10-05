package org.roylance.yaclib.core.services.java.init

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class GenerateProtoBuilder : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    return YaclibModel.File.newBuilder()
        .setFileExtension(YaclibModel.FileExtension.SH_EXT)
        .setFullDirectoryLocation("")
        .setFileName("generate_proto")
        .setFileToWrite(InitialTemplate)
        .build()
  }

  companion object {
    const val InitialTemplate = """#!/usr/bin/env bash
protoc -I=src/main/resources --proto_path=src/main/resources --java_out=src/main/java src/main/resources/*
"""
  }
}