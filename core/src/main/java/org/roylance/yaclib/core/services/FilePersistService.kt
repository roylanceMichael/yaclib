package org.roylance.yaclib.core.services

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.utilities.FileExtensionUtilities
import java.io.File

class FilePersistService: IFilePersistService {
    override fun persistFiles(initialLocation: String, files: Models.AllFiles) {
        val initialLocationDir = File(initialLocation)

        if (!initialLocationDir.exists()) {
            initialLocationDir.mkdirs()
        }

        files.filesList.forEach { file ->
            val existingDir = File(initialLocation, file.fullDirectoryLocation)

            if (!existingDir.exists()) {
                existingDir.mkdirs()
            }

            val existingFile = File(existingDir.absolutePath, file.fileName + FileExtensionUtilities.getExtension(file.fileExtension))

            if (existingFile.exists() && file.fileUpdateType.equals(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)) {
                return@forEach
            }

            existingFile.writeText(file.fileToWrite)
        }
    }
}