package org.roylance.yaclib.core.services.java.client

import org.apache.commons.io.IOUtils
import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder

class GradlewBatFileBuilder: IBuilder<Models.File> {
    override fun build(): Models.File {
        val resource = GradlewFileBuilder::class.java.getResourceAsStream(ActualFileLocation)
        val actualFile = IOUtils.toString(resource)
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(actualFile)
                .setFileName("gradlew")
                .setFileExtension(Models.FileExtension.BAT_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }

    companion object {
        private const val ActualFileLocation = "/gradlew.bat"
    }
}