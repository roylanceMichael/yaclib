package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class Java8Base64ServiceBuilder(private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val ScriptedTemplate = """package ${dependency.group}.${CommonTokens.ServicesName}

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

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(ScriptedTemplate)
                .setFileName("Base64Service")
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group, CommonTokens.ServicesName))

        return returnFile.build()
    }
}