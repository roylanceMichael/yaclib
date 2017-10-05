package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftContentsXCWorkspaceDataBuilder(
    private val projectInformation: YaclibModel.ProjectInformation) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val file = YaclibModel.File.newBuilder()
        .setFileExtension(YaclibModel.FileExtension.XCWORKSPACEDATA_EXT)
        .setFileToWrite(InitialTemplate)
        .setFileName("contents")
        .setFullDirectoryLocation("${SwiftUtilities.buildSwiftFullName(
            projectInformation.mainDependency)}.xcodeproj/project.xcworkspace")
        .build()
    return file
  }

  private val InitialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<Workspace
   version = "1.0">
   <FileRef
      location = "self:${SwiftUtilities.buildSwiftFullName(
      projectInformation.mainDependency)}.xcodeproj">
   </FileRef>
</Workspace>
"""
}