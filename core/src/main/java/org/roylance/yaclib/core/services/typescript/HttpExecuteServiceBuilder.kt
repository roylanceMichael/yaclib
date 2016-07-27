package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder

class HttpExecuteServiceBuilder(overallPackage: String): IBuilder<Models.File> {
    private val InitialTemplate = """
declare module $overallPackage.services {
    export interface IHttpExecuteService {
        performPost(url:string, data:any, onSuccess:(data) => void, onError:(data) => void)
    }
}
"""

    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(InitialTemplate.trim())
                .setFileExtension(Models.FileExtension.TS_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileName(FileName)
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }

    companion object {
        const val FileName = "IHttpExecute"
    }
}