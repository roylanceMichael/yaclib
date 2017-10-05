package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class SetupCFGBuilder : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val initialTemplate = """[metadata]
description-file = README.md
"""
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(initialTemplate.trim())
        .setFileExtension(YaclibModel.FileExtension.CFG_EXT)
        .setFileName("setup")
        .setFullDirectoryLocation("")
        .build()

    return returnFile
  }
}