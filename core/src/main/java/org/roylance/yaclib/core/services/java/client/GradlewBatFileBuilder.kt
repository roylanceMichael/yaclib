package org.roylance.yaclib.core.services.java.client

import org.apache.commons.io.IOUtils
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import java.nio.charset.Charset

class GradlewBatFileBuilder: IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val resource = GradlewFileBuilder::class.java.getResourceAsStream(ActualFileLocation)
        val actualFile = IOUtils.toString(resource, Charset.defaultCharset())
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(actualFile)
                .setFileName("gradlew")
                .setFileExtension(YaclibModel.FileExtension.BAT_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }

    companion object {
        private const val ActualFileLocation = "/gradlew.bat"
    }
}