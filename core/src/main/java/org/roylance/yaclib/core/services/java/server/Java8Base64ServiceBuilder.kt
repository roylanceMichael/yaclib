package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class Java8Base64ServiceBuilder(private val overallPackage: String): IBuilder<Models.File> {
    private val ScriptedTemplate = """package $overallPackage.${CommonTokens.ServicesName}

import org.roylance.common.service.IBase64Service
import java.util.*

class Base64Service: IBase64Service {
    override fun deserialize(string64: String): ByteArray {
        return Base64.getDecoder().decode(string64)
    }

    override fun serialize(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }
}
"""

    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(ScriptedTemplate)
                .setFileName("Base64Service")
                .setFileExtension(Models.FileExtension.KT_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(overallPackage, CommonTokens.ServicesName))

        return returnFile.build()
    }
}