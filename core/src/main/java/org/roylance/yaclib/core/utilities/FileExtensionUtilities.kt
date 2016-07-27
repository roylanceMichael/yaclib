package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.Models

object FileExtensionUtilities {
    fun getExtension(extension: Models.FileExtension):String {
        if (Models.FileExtension.JAVASCRIPT_EXT.equals(extension)) {
            return ".js"
        }
        if (Models.FileExtension.KT_EXT.equals(extension)) {
            return ".kt"
        }
        if (Models.FileExtension.POM_EXT.equals(extension)) {
            return ".xml"
        }
        if (Models.FileExtension.SWIFT_EXT.equals(extension)) {
            return ".swift"
        }
        if (Models.FileExtension.XML_EXT.equals(extension)) {
            return ".xml"
        }
        if (Models.FileExtension.HTML_EXT.equals(extension)) {
            return ".html"
        }
        if (Models.FileExtension.GRADLE_EXT.equals(extension)) {
            return ".gradle"
        }
        if (Models.FileExtension.JSON_EXT.equals(extension)) {
            return ".json"
        }
        if (Models.FileExtension.TS_EXT.equals(extension)) {
            return ".ts"
        }
        if (Models.FileExtension.NONE_EXT.equals(extension)) {
            return ""
        }
        if (Models.FileExtension.BAT_EXT.equals(extension)) {
            return ".bat"
        }

        return ".java"
    }
}