package org.roylance.yaclib.core.services.typescript

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class HttpExecuteServiceBuilder : IBuilder<YaclibModel.File> {
    private val InitialTemplate = """
export interface $FileName {
    performPost(url:string, data:any, onSuccess:(data) => void, onError:(data) => void)
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.TS_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileName(FileName)
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }

    companion object {
        const val FileName = CommonTokens.HttpExecute
        const val VariableName = CommonTokens.HttpExecuteVariableName
    }
}