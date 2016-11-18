package org.roylance.yaclib.core.utilities

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.io.xpp3.MavenXpp3Writer
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object MavenUtilities: IProjectBuilderServices {
    const val PomName = "pom"
    const val PomXml = "$PomName.xml"
    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val reader = MavenXpp3Reader()

        val pomFile = File(location, PomXml)
        if (!pomFile.exists()) {
            pomFile.createNewFile()
        }
        val model = reader.read(FileReader(pomFile))
        val minorVersion = model.properties.getProperty(JavaUtilities.MinorName).toInt() + 1
        model.properties.setProperty(JavaUtilities.MinorName, minorVersion.toString())

        MavenXpp3Writer().write(FileWriter(pomFile), model)

        return YaclibModel.ProcessReport.newBuilder()
            .setNewMinor(minorVersion)
            .setNewMajor(dependency.majorVersion)
            .setContent("${dependency.majorVersion}.$minorVersion")
            .build()
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val reader = MavenXpp3Reader()

        val pomFile = File(location, PomXml)
        if (!pomFile.exists()) {
            pomFile.createNewFile()
        }

        val variableName = JavaUtilities.buildPackageVariableName(otherDependency)
        val model = reader.read(FileReader(pomFile))
        if (otherDependency.thirdPartyDependencyVersion.isEmpty()) {
            model.properties.setProperty(variableName, "${otherDependency.majorVersion}.${otherDependency.minorVersion}")
        }
        else {
            model.properties.setProperty(variableName, otherDependency.thirdPartyDependencyVersion)
        }

        MavenXpp3Writer().write(FileWriter(pomFile), model)

        return YaclibModel.ProcessReport.newBuilder()
                .build()
    }

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        val reader = MavenXpp3Reader()

        val pomFile = File(location, PomXml)
        if (!pomFile.exists()) {
            pomFile.createNewFile()
        }

        val model = reader.read(FileReader(pomFile))
        return YaclibModel.ProcessReport.newBuilder()
                .setNewMinor(model.properties.getProperty(JavaUtilities.MinorName).toInt())
                .setNewMajor(model.properties.getProperty(JavaUtilities.MajorName).toInt())
                .build()
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val reader = MavenXpp3Reader()

        val pomFile = File(location, PomXml)
        if (!pomFile.exists()) {
            pomFile.createNewFile()
        }
        val model = reader.read(FileReader(pomFile))
        model.properties.setProperty(JavaUtilities.MinorName, dependency.minorVersion.toString())
        model.properties.setProperty(JavaUtilities.MajorName, dependency.majorVersion.toString())

        MavenXpp3Writer().write(FileWriter(pomFile), model)

        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.Maven, "clean")
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.Maven, "compile")
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.Maven, "package")
    }

    override fun publish(location: String, dependency: YaclibModel.Dependency, apiKey: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }
}