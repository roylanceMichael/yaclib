package org.roylance.yaclib.core.services.java.common

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.io.StringWriter
import java.util.*

class PropertiesBuilder(private val controllerDependencies: YaclibModel.AllControllerDependencies,
                        private val mainDependency: YaclibModel.Dependency,
                        private val thirdPartyDependencies: List<YaclibModel.Dependency>): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val properties = Properties()

        properties.setProperty(JavaUtilities.GroupName, mainDependency.group)
        properties.setProperty(JavaUtilities.NameName, mainDependency.name)
        properties.setProperty(JavaUtilities.FullPackageName, JavaUtilities.buildFullPackageName(mainDependency))
        properties.setProperty(JavaUtilities.MinorName, mainDependency.minorVersion.toString())
        properties.setProperty(JavaUtilities.MajorName, mainDependency.majorVersion.toString())
        properties.setProperty(JavaUtilities.FullVersionName, "${mainDependency.majorVersion}.${mainDependency.minorVersion}")

        val uniqueDependencies = HashMap<String, String>()
        controllerDependencies.controllerDependenciesList.forEach {
            uniqueDependencies[JavaUtilities.buildPackageVariableName(it.dependency)] = "${it.dependency.majorVersion}.${it.dependency.minorVersion}"
        }

        thirdPartyDependencies.forEach {
            uniqueDependencies[JavaUtilities.buildPackageVariableName(it)] = it.thirdPartyDependencyVersion
        }

        uniqueDependencies.keys.forEach {
            properties.setProperty(it, uniqueDependencies[it]!!)
        }

        val stringWriter = StringWriter()
        properties.store(stringWriter, "generated from yaclib on ${Date()}")

        val file = YaclibModel.File.newBuilder()
            .setFileExtension(YaclibModel.FileExtension.PROPERTIES_EXT)
            .setFileToWrite(stringWriter.toString().trim())
            .setFileName(JavaUtilities.PropertiesFileNameWithoutExtension)
            .setFullDirectoryLocation("")
            .build()

        return file
    }
}