package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel

object FileExtensionUtilities {
    fun getExtension(extension: YaclibModel.FileExtension):String {
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

        return ".java"
    }
}