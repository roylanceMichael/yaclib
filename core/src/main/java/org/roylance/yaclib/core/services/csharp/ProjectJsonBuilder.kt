package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.CSharpUtilities

class ProjectJsonBuilder(private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """{
  "version": "${dependency.majorVersion}.${dependency.minorVersion}.0-*",

  "dependencies": {
    "Google.Protobuf": "3.0.0"
  },
  "buildOptions": {
    "debugType": "portable"
  },
  "frameworks": {
    "net45": {},
    "netstandard1.0": {
      "dependencies": {
        "System.Collections": "4.0.11",
        "System.Diagnostics.Debug": "4.0.11",
        "System.Globalization": "4.0.11",
        "System.IO": "4.1.0",
        "System.Linq": "4.1.0",
        "System.Linq.Expressions": "4.1.0",
        "System.ObjectModel": "4.0.12",
        "System.Reflection": "4.1.0",
        "System.Reflection.Extensions": "4.0.1",
        "System.Runtime": "4.1.0",
        "System.Runtime.Extensions": "4.1.0",
        "System.Text.Encoding": "4.0.11",
        "System.Text.RegularExpressions": "4.1.0",
        "System.Threading": "4.0.11"
      }
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