package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.*

object GradleUtilities: IProjectBuilderServices {
    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
        }
        finally {
            inputStream.close()
        }

        var minorVersion = properties.getProperty(JavaUtilities.MinorName).toInt()
        minorVersion += 1
        properties.setProperty(JavaUtilities.MinorName, minorVersion.toString())

        val majorVersion = properties.getProperty(JavaUtilities.MajorName).toInt()

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "incrementVersion: ${dependency.majorVersion}.${dependency.minorVersion}")
        }
        finally {
            outputStream.close()
        }

        return YaclibModel.ProcessReport.newBuilder()
                .setNewMinor(minorVersion)
                .setNewMajor(majorVersion)
                .build()
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
        }
        finally {
            inputStream.close()
        }

        // special rule for java types
        if (otherDependency.name == CommonTokens.ApiName) {
            properties.setProperty(JavaUtilities.buildPackageVariableName(otherDependency
                    .toBuilder()
                    .setName(CommonTokens.ClientApi).build()), "${otherDependency.majorVersion}.${otherDependency.minorVersion}")
        }

        val variableName = JavaUtilities.buildPackageVariableName(otherDependency)
        val version = "${otherDependency.majorVersion}.${otherDependency.minorVersion}"
        properties.setProperty(variableName, version)

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "update dependency: $variableName $version")
        }
        finally {
            outputStream.close()
        }

        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
            return YaclibModel.ProcessReport.newBuilder()
                    .setNewMinor(properties.getProperty(JavaUtilities.MinorName).toInt())
                    .setNewMajor(properties.getProperty(JavaUtilities.MajorName).toInt())
                    .build()
        }
        finally {
            inputStream.close()
        }
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
        }
        finally {
            inputStream.close()
        }

        properties.setProperty(JavaUtilities.MajorName, dependency.majorVersion.toString())
        properties.setProperty(JavaUtilities.MinorName, dependency.minorVersion.toString())

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "set version: ${dependency.majorVersion}.${dependency.minorVersion}")
        }
        finally {
            outputStream.close()
        }

        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "clean")
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "build")
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun publish(location: String,
                         dependency: YaclibModel.Dependency,
                         apiKey: String): YaclibModel.ProcessReport {
        if (dependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
            return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "artifactoryPublish")
        }
        else {
            return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "bintrayUpload")
        }
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }
}