package org.roylance.yaclib.core.services.java.init

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class ControllerProtoBuilder(
    private val dependency: YaclibModel.Dependency) : IBuilder<YaclibModel.File> {
  private val initialTemplate = """syntax = "proto3";
package ${dependency.group};

import "${dependency.name.toLowerCase()}_model.proto";

// todo: fill out your model here
"""

  override fun build(): YaclibModel.File {
    return YaclibModel.File.newBuilder()
        .setFileToWrite(initialTemplate)
        .setFileName("${dependency.name.toLowerCase()}_main_controller")
        .setFileExtension(YaclibModel.FileExtension.PROTO_EXT)
        .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
        .setFullDirectoryLocation("src/main/resources")
        .build()
  }
}