package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities
import java.io.StringWriter
import java.util.*

class PropertiesBuilder(private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val properties = Properties()

        properties.setProperty(JavaUtilities.GroupName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.group))
        properties.setProperty(JavaUtilities.NameName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.name))
        properties.setProperty(JavaUtilities.FullPackageName, PythonUtilities.surroundWithDoubleQuotes(PythonUtilities.buildPackageName(mainDependency)))
        properties.setProperty(JavaUtilities.MinorName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.minorVersion.toString()))
        properties.setProperty(JavaUtilities.MajorName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.majorVersion.toString()))
        properties.setProperty(JavaUtilities.FullVersionName, PythonUtilities.surroundWithDoubleQuotes("${mainDependency.majorVersion}.${mainDependency.minorVersion}"))
        properties.setProperty(JavaUtilities.AuthorName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.authorName))
        properties.setProperty(JavaUtilities.GithubUrlName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.githubRepo))
        properties.setProperty(JavaUtilities.LicenseName, PythonUtilities.surroundWithDoubleQuotes(mainDependency.license))
        properties.setProperty(JavaUtilities.DescriptionName, PythonUtilities.surroundWithDoubleQuotes("models to interface with the ${mainDependency.group}.${mainDependency.name} system"))

        val stringWriter = StringWriter()
        properties.store(stringWriter, "generated from yaclib on ${Date().toString()}")

        val file = YaclibModel.File.newBuilder()
                .setFileExtension(YaclibModel.FileExtension.PY_EXT)
                .setFileToWrite(stringWriter.toString().trim())
                .setFileName(PythonUtilities.PropertiesNameWithoutExtension)
                .setFullDirectoryLocation("")
                .build()

        return file
    }
}