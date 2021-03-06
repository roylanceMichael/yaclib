package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel

object FileExtensionUtilities {
  fun getExtension(extension: YaclibModel.FileExtension): String {
    if (YaclibModel.FileExtension.JAVASCRIPT_EXT == extension) {
      return ".js"
    }
    if (YaclibModel.FileExtension.KT_EXT == extension) {
      return ".kt"
    }
    if (YaclibModel.FileExtension.POM_EXT == extension) {
      return ".xml"
    }
    if (YaclibModel.FileExtension.SWIFT_EXT == extension) {
      return ".swift"
    }
    if (YaclibModel.FileExtension.XML_EXT == extension) {
      return ".xml"
    }
    if (YaclibModel.FileExtension.HTML_EXT == extension) {
      return ".html"
    }
    if (YaclibModel.FileExtension.GRADLE_EXT == extension) {
      return ".gradle"
    }
    if (YaclibModel.FileExtension.JSON_EXT == extension) {
      return ".json"
    }
    if (YaclibModel.FileExtension.TS_EXT == extension) {
      return ".ts"
    }
    if (YaclibModel.FileExtension.NONE_EXT == extension) {
      return ""
    }
    if (YaclibModel.FileExtension.BAT_EXT == extension) {
      return ".bat"
    }
    if (YaclibModel.FileExtension.JS_EXT == extension) {
      return ".js"
    }
    if (YaclibModel.FileExtension.SLN_EXT == extension) {
      return ".sln"
    }
    if (YaclibModel.FileExtension.CS_EXT == extension) {
      return ".cs"
    }
    if (YaclibModel.FileExtension.XPROJ_EXT == extension) {
      return ".xproj"
    }
    if (YaclibModel.FileExtension.SH_EXT == extension) {
      return ".sh"
    }
    if (YaclibModel.FileExtension.MD_EXT == extension) {
      return ".md"
    }
    if (YaclibModel.FileExtension.PY_EXT == extension) {
      return ".py"
    }
    if (YaclibModel.FileExtension.CFG_EXT == extension) {
      return ".cfg"
    }
    if (YaclibModel.FileExtension.PROPERTIES_EXT == extension) {
      return ".properties"
    }
    if (YaclibModel.FileExtension.BLOB_EXT == extension) {
      return ".blob"
    }
    if (YaclibModel.FileExtension.PROTO_EXT == extension) {
      return ".proto"
    }
    if (YaclibModel.FileExtension.PLIST_EXT == extension) {
      return ".plist"
    }
    if (YaclibModel.FileExtension.XCWORKSPACEDATA_EXT == extension) {
      return ".xcworkspacedata"
    }
    if (YaclibModel.FileExtension.PBXPROJ_EXT == extension) {
      return ".pbxproj"
    }
    if (YaclibModel.FileExtension.H_EXT == extension) {
      return ".h"
    }
    if (YaclibModel.FileExtension.XCSCHEME_EXT == extension) {
      return ".xcscheme"
    }
    return ".java"
  }
}