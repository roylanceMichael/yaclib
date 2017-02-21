package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel

class CartFileBuilder: IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val file = YaclibModel.File.newBuilder()
                .setFileExtension(YaclibModel.FileExtension.NONE_EXT)
                .setFileToWrite(InitialTemplate)
                .setFileName("Cartfile")
                .setFullDirectoryLocation("")
                .build()
        return file
    }

    private val InitialTemplate = """github "apple/swift-protobuf"
github "Alamofire/Alamofire"
"""
}