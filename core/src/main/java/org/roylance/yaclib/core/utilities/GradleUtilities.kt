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
        properties.setProperty(JavaUtilities.FullVersionName, "${dependency.majorVersion}.${dependency.minorVersion}")

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "")
        }
        finally {
            outputStream.close()
        }

        return YaclibModel.ProcessReport.newBuilder()
                .setContent(properties.getProperty(JavaUtilities.FullVersionName))
                .setNewMinor(minorVersion)
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

        properties.setProperty(JavaUtilities.buildPackageVariableName(otherDependency), "${otherDependency.majorVersion}.${otherDependency.minorVersion}")

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "")
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
                    .setContent(properties.getProperty(JavaUtilities.FullVersionName))
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
        properties.setProperty(JavaUtilities.FullVersionName, "${dependency.majorVersion}.${dependency.minorVersion}")

        val outputStream = FileOutputStream(propertiesFile)
        try {
            properties.store(FileOutputStream(propertiesFile), "")
        }
        finally {
            outputStream.close()
        }

        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, "gradle", "clean")
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, "gradle", "build")
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun buildPublish(location: String,
                              dependency: YaclibModel.Dependency,
                              apiKey: String): YaclibModel.ProcessReport {
        if (dependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
            return FileProcessUtilities.executeProcess(location, "gradle", "artifactoryPublish")
        }
        else {
            return FileProcessUtilities.executeProcess(location, "gradle", "bintrayUpload")
        }
    }

    override fun restoreDependencies(location: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }
}