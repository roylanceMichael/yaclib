package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class GradleSettingsBuilder : IBuilder<YaclibModel.File> {
    private val InitialTemplate = "rootProject.name = '${CommonTokens.ClientApi}'"
    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileName("settings")
                .setFileExtension(YaclibModel.FileExtension.GRADLE_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }
}
