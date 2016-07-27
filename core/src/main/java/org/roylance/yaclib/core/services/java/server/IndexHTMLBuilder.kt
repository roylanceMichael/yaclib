package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder

class IndexHTMLBuilder: IBuilder<Models.File> {
    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(IndexHTMLFile)
                .setFileName("index")
                .setFileExtension(Models.FileExtension.HTML_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation("src/main/webapp")

        return returnFile.build()
    }

    companion object {
        private const val IndexHTMLFile = """<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>web</title>
    <meta name="viewport" content="width=device-width" />
</head>
<body>

</body>
</html>
"""
    }
}