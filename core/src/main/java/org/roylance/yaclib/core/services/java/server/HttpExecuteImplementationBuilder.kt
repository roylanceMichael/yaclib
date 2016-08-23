package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.typescript.HttpExecuteServiceBuilder

class HttpExecuteImplementationBuilder(private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val template = """import {${HttpExecuteServiceBuilder.FileName}} from "${buildNodeModulePath()}"

export class $FileName implements ${HttpExecuteServiceBuilder.FileName} {
    httpPost:string = "POST";
    httpService:any;

    constructor(httpService:any) {
        this.httpService = httpService;
    }

    performPost(url:string, data:any, onSuccess:(data)=>void, onError:(data)=>void) {
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data
        }).success(onSuccess)
          .error(onError);
    }
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(template.trim())
                .setFileExtension(YaclibModel.FileExtension.TS_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileName("HttpExecute")
                .setFullDirectoryLocation("src/main/javascript/app")
                .build()

        return returnFile
    }

    private fun buildNodeModulePath():String {
        if (mainDependency.nodeAliasName.length > 0) {
            return "../node_modules/${this.mainDependency.nodeAliasName}/${this.mainDependency.group}.${this.mainDependency.name}/${HttpExecuteServiceBuilder.FileName}"
        }

        return "../node_modules/${this.mainDependency.group}.${this.mainDependency.name}/${HttpExecuteServiceBuilder.FileName}"
    }

    companion object {
        const val FileName = "HttpExecute"
    }
}