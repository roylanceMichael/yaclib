package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

class IndexHTMLBuilder(mainDependency: YaclibModel.Dependency) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(IndexHTMLFile)
        .setFileName("index")
        .setFileExtension(YaclibModel.FileExtension.HTML_EXT)
        .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
        .setFullDirectoryLocation("src/main/webapp")

    return returnFile.build()
  }
  private val IndexHTMLFile = """<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>${mainDependency.name}${CommonTokens.ServerSuffix}</title>
    <meta name="viewport" content="width=device-width" />
</head>
<body>

</body>
</html>
"""
}