package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.plugins.client.PythonBuilder
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

object PythonUtilities: IProjectBuilderServices {
    const val ProtobufName = "protobuf"

    const val PropertiesNameWithoutExtension = "properties"
    const val PropertiesName = "$PropertiesNameWithoutExtension.py"

    fun convertPythonFileNameImport(fileName: String): String {
        val removedDotProto = fileName.replace(".proto", "")
        return "${removedDotProto}_pb2"
    }

    fun buildPackageName(dependency: YaclibModel.Dependency): String {
        val replacedName = replacePeriodWithUnderscore(dependency.group)
        return "${replacedName}_${dependency.name}"
    }

    fun buildPythonSourceDirectory(mainDependency: YaclibModel.Dependency): String {
        return Paths.get(CommonTokens.PythonName, buildPackageName(mainDependency)).toString()
    }

    fun buildPythonRootDirectory(): String {
        return CommonTokens.PythonName
    }

    fun buildWheelUrl(dependency: YaclibModel.Dependency): String {
        val newGroup = ArtifactoryUtilities.replacePeriodWithForwardSlash(dependency.group)
        return "$newGroup/${dependency.name}/${replacePeriodWithUnderscore(dependency.group)}_${dependency.name}-${dependency.majorVersion}-${dependency.minorVersion}-py2.py3-none-any.whl"
    }

    fun buildWheelFileName(dependency: YaclibModel.Dependency): String {
        return "${replacePeriodWithUnderscore(dependency.group)}_${dependency.name}-${dependency.majorVersion}-${dependency.minorVersion}-py2.py3-none-any.whl"
    }

    fun replacePeriodWithUnderscore(item: String): String {
        return item.replace(".", "_")
    }

    fun buildProtobufs(location: String, mainDependency: YaclibModel.Dependency): ProcessBuilder {
        val pythonDirectory = Paths.get(location, buildPythonSourceDirectory(mainDependency)).toFile()
        pythonDirectory.mkdirs()

        val protobufLocation = Paths.get(location, CommonTokens.ApiName, "src", "main", "resources").toString()
        val arguments = "-I=$protobufLocation --proto_path=$protobufLocation --python_out=$pythonDirectory $protobufLocation/*.proto"
        val generateProtoProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("protoc", arguments))

        return generateProtoProcess
    }

    fun surroundWithDoubleQuotes(item: Any): String {
        return """"$item""""
    }

    fun cleanseProperty(item: String): String {
        return item.replace("\"", "")
    }

    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, PropertiesName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)

            val currentVersion = cleanseProperty(properties.getProperty(JavaUtilities.MinorName)).toInt() + 1
            properties.setProperty(JavaUtilities.MinorName, surroundWithDoubleQuotes(currentVersion.toString()))
            properties.setProperty(JavaUtilities.FullVersionName, surroundWithDoubleQuotes("${dependency.majorVersion}.$currentVersion"))

            return YaclibModel.ProcessReport.newBuilder().setNewMinor(currentVersion).build()
        }
        finally {
            inputStream.close()
        }    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, PropertiesName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
            return YaclibModel.ProcessReport.newBuilder()
                    .setContent(cleanseProperty(properties.getProperty(JavaUtilities.FullVersionName)))
                    .setNewMajor(cleanseProperty(properties.getProperty(JavaUtilities.MajorName)).toInt())
                    .setNewMinor(cleanseProperty(properties.getProperty(JavaUtilities.MinorName)).toInt())
                    .build()
        }
        finally {
            inputStream.close()
        }
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val propertiesFile = Paths.get(location, PropertiesName).toFile()
        if (!propertiesFile.exists()) {
            return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
        }
        val properties = Properties()
        val inputStream = FileInputStream(propertiesFile)
        try {
            properties.load(inputStream)
            properties.setProperty(JavaUtilities.MinorName, surroundWithDoubleQuotes(dependency.minorVersion.toString()))
            properties.setProperty(JavaUtilities.MajorName, surroundWithDoubleQuotes(dependency.majorVersion.toString()))
            properties.setProperty(JavaUtilities.FullVersionName, surroundWithDoubleQuotes("${dependency.majorVersion}.${dependency.minorVersion}"))
            return YaclibModel.ProcessReport.getDefaultInstance()
        }
        finally {
            inputStream.close()
        }
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val normalLogs = StringBuilder()
        val errorLogs = StringBuilder()
        // cleanup, no need to
        FileProcessUtilities.executeProcess(location, "find", ". -name '*pyc' -delete")
        FileProcessUtilities.executeProcess(location, "find", ". -name '*DS_Store' -delete")

        val buildWheelReport = FileProcessUtilities.executeProcess(location, "python", "setup.py bdist_wheel --universal")
        normalLogs.appendln(buildWheelReport.normalOutput)
        errorLogs.appendln(buildWheelReport.errorOutput)

        val moveWheelReport = FileProcessUtilities.executeProcess(location, "mv", "${Paths.get(location, "dist")}/*.whl  $location/${PythonUtilities.buildWheelFileName(dependency)}")
        normalLogs.appendln(moveWheelReport.normalOutput)
        normalLogs.appendln(moveWheelReport.errorOutput)

        FileProcessUtilities.executeProcess(location, "rm", "-rf dist")
        FileProcessUtilities.executeProcess(location, "rm", "-rf build")
        FileProcessUtilities.executeProcess(location, "rm", "-rf *egg-info")

        return YaclibModel.ProcessReport.newBuilder().setNormalOutput(normalLogs.toString()).setErrorOutput(errorLogs.toString()).build()
    }

    override fun publish(location: String,
                         dependency: YaclibModel.Dependency,
                         apiKey: String): YaclibModel.ProcessReport {
        if (dependency.hasPipRepository() &&
                dependency.pipRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_PYTHON &&
                dependency.pipRepository.url.length > 0) {
            val scriptToRun = ArtifactoryUtilities.buildUploadWheelScript(location.toString(), dependency)

            val actualScriptFile = File(location, ArtifactoryUtilities.UploadWheelScriptName)
            actualScriptFile.writeText(scriptToRun)

            FileProcessUtilities.executeProcess(location, "chmod", "755 $actualScriptFile")

            return FileProcessUtilities.executeProcess(location, "bash", actualScriptFile.toString())
        }

        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }
}