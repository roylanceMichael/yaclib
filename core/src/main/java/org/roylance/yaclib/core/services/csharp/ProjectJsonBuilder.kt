package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.CSharpUtilities

class ProjectJsonBuilder(private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """{
  "version": "0.0.${dependency.version}-*",

  "dependencies": {
    "NETStandard.Library": "1.6.0",
    "Google.Protobuf.Tools": "3.0.0",
    "Google.Protobuf": "3.0.0"
  },

  "frameworks": {
    "netstandard1.2": {
      "imports": [
      "dotnet5.6",
      "dnxcore50",
      "portable-net45+win8"
      ]
    }
  }
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.JSON_EXT)
                .setFileName("project")
                .setFullDirectoryLocation(CSharpUtilities.buildFullName(dependency))
                .build()
        return returnFile
    }
}