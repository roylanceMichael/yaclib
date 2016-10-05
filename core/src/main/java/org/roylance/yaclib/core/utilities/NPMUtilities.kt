package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.common.NPMPackageBuilder
import java.io.File
import java.util.*

object NPMUtilities {
    const val PackageNameWithoutExtension = "package"
    const val PackageNameJson = "$PackageNameWithoutExtension.json"
    const val PackageNameBlob = "$PackageNameWithoutExtension.blob"

    fun loadNPMBlob(location: String): YaclibModel.ProjectInformation {
        val actualFile = File(location, PackageNameBlob).readText()
        val base64File = Base64.getDecoder().decode(actualFile)
        return YaclibModel.ProjectInformation.parseFrom(base64File)
    }

    fun saveNPM(location: String, projectInformation: YaclibModel.ProjectInformation) {
        val base64File = Base64.getEncoder().encodeToString(projectInformation.toByteArray())
        File(location, PackageNameBlob).writeText(base64File)

        val newFile = NPMPackageBuilder(projectInformation).build()
        File(location, PackageNameJson).writeText(newFile.fileToWrite)
    }
}