package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel

object FileExtensionUtilities {
    fun getExtension(extension: YaclibModel.FileExtension):String {
        if (YaclibModel.FileExtension.JAVASCRIPT_EXT.equals(extension)) {
            return ".js"
        }
        if (YaclibModel.FileExtension.KT_EXT.equals(extension)) {
            return ".kt"
        }
        if (YaclibModel.FileExtension.POM_EXT.equals(extension)) {
            return ".xml"
        }
        if (YaclibModel.FileExtension.SWIFT_EXT.equals(extension)) {
            return ".swift"
        }
        if (YaclibModel.FileExtension.XML_EXT.equals(extension)) {
            return ".xml"
        }
        if (YaclibModel.FileExtension.HTML_EXT.equals(extension)) {
            return ".html"
        }
        if (YaclibModel.FileExtension.GRADLE_EXT.equals(extension)) {
            return ".gradle"
        }
        if (YaclibModel.FileExtension.JSON_EXT.equals(extension)) {
            return ".json"
        }
        if (YaclibModel.FileExtension.TS_EXT.equals(extension)) {
            return ".ts"
        }
        if (YaclibModel.FileExtension.NONE_EXT.equals(extension)) {
            return ""
        }
        if (YaclibModel.FileExtension.BAT_EXT.equals(extension)) {
            return ".bat"
        }
        if (YaclibModel.FileExtension.JS_EXT.equals(extension)) {
            return ".js"
        }
        if (YaclibModel.FileExtension.SLN_EXT.equals(extension)) {
            return ".sln"
        }
        if (YaclibModel.FileExtension.CS_EXT.equals(extension)) {
            return ".cs"
        }
        if (YaclibModel.FileExtension.XPROJ_EXT.equals(extension)) {
            return ".xproj"
        }

        return ".java"
    }
}