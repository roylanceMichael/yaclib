package org.roylance.yaclib.core.services.common

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.NPMUtilities
import java.util.*

class NPMBlobBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val base64 = Base64.getEncoder().encodeToString(projectInformation.toByteArray())
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(base64)
                .setFileExtension(YaclibModel.FileExtension.BLOB_EXT)
                .setFileName(NPMUtilities.PackageNameWithoutExtension)
                .setFullDirectoryLocation(if (projectInformation.isServer) "src/main/javascript" else "")
                .build()

        return returnFile
    }
}