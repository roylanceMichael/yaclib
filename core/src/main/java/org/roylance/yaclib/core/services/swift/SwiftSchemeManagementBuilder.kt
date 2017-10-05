package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftSchemeManagementBuilder(
    private val projectInformation: YaclibModel.ProjectInformation) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val file = YaclibModel.File.newBuilder()
        .setFileExtension(YaclibModel.FileExtension.PLIST_EXT)
        .setFileToWrite(InitialTemplate)
        .setFileName("xcschememanagement")
        .setFullDirectoryLocation("${SwiftUtilities.buildSwiftFullName(
            projectInformation.mainDependency)}.xcodeproj/xcshareddata/xcshemes")
        .build()
    return file
  }

  private val InitialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<plist version="1.0">
<dict>
  <key>SchemeUserState</key>
  <dict>
    <key>${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.xcscheme</key>
    <dict></dict>
  </dict>
  <key>SuppressBuildableAutocreation</key>
  <dict></dict>
</dict>
</plist>

"""
}