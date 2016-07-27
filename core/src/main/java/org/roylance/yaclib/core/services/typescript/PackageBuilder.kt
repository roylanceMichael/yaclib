package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder

class PackageBuilder(overallPackageName: String, dependency: Models.Dependency): IBuilder<Models.File> {
    private val initialTemplate = """{
  "name": "$overallPackageName.${CommonTokens.ClientApi}js",
  "version": "0.0.1",
  "description": "models and interfaces for $overallPackageName",
  "main": "model_factories.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "license": "ISC",
  "dependencies": {
    "${dependency.group}.${dependency.name}": "${dependency.version}",
    "protobufjs": "^5.0.1",
    "proto2typescript": "^2.2.0"
  }
}
"""

    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(initialTemplate.trim())
                .setFileExtension(Models.FileExtension.JSON_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFileName("package")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }
}