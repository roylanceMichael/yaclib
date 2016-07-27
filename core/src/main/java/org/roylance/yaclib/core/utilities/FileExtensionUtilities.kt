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
        return ".java"
    }
}