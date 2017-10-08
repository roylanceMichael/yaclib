package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class InfoPlistBuilder(private val projectInformation: YaclibModel.ProjectInformation,
    private val isTest: Boolean) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val fileDirectory = "${projectInformation.mainDependency.name}${CommonTokens.SwiftSuffix}/Source"

    val file = YaclibModel.File.newBuilder()
        .setFileExtension(YaclibModel.FileExtension.PLIST_EXT)
        .setFileToWrite(InitialTemplate)
        .setFileName("Info")
        .setFullDirectoryLocation(fileDirectory)
        .build()
    return file
  }

  private val InitialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>CFBundleDevelopmentRegion</key>
	<string>en</string>
	<key>CFBundleExecutable</key>
	<string>$(EXECUTABLE_NAME)</string>
	<key>CFBundleIdentifier</key>
	<string>$(PRODUCT_BUNDLE_IDENTIFIER)</string>
	<key>CFBundleInfoDictionaryVersion</key>
	<string>6.0</string>
	<key>CFBundleName</key>
	<string>$(PRODUCT_NAME)</string>
	<key>CFBundlePackageType</key>
	<string>FMWK</string>
	<key>CFBundleShortVersionString</key>
	<string>1.0</string>
	<key>CFBundleVersion</key>
	<string>$(CURRENT_PROJECT_VERSION)</string>
	<key>NSPrincipalClass</key>
	<string></string>
</dict>
</plist>

"""
}