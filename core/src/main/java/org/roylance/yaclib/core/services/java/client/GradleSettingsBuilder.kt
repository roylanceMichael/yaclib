package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder

class GradleSettingsBuilder(projectName: String): IBuilder<Models.File> {
    private val InitialTemplate = "rootProject.name = '${CommonTokens.ClientApi}'"
    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileName("settings")
                .setFileExtension(Models.FileExtension.GRADLE_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }
}
