package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class FurtherAngularSetupBuilder: IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val template = """
// this file won't be overwritten, add more dependencies for angular as needed
export function furtherAngularSetup(app:any) {

}
"""
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(template)
                .setFileName("FurtherAngularSetup")
                .setFileExtension(YaclibModel.FileExtension.TS_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("src/main/javascript/app")
        return returnFile.build()
    }
}