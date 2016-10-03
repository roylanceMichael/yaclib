package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.PythonUtilities

class InitBuilder(private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val initialTemplate = """
"""
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.PY_EXT)
                .setFileName("__init__")
                .setFullDirectoryLocation(PythonUtilities.buildPackageName(dependency))
                .build()

        return returnFile
    }
}