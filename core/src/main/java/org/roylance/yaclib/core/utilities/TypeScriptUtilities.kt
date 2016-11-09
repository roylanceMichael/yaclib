package org.roylance.yaclib.core.utilities

import com.google.gson.GsonBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.models.NPMPackage
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.File
import java.nio.file.Paths
import java.util.*

object TypeScriptUtilities: IProjectBuilderServices {
    private val neverTarThisDirectory = object: HashSet<String>() {
        init {
            add("node_modules")
        }
    }
    private val gson = GsonBuilder().setPrettyPrinting().create()

    val protobufJsDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^5.0.1").setGroup("protobufjs").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val roylanceCommonDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^${JavaUtilities.RoylanceCommonVersion}.0").setGroup("roylance.common").setType(YaclibModel.DependencyType.TYPESCRIPT)!!

    val proto2TypeScriptDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^2.2.0").setGroup("proto2typescript").setType(YaclibModel.DependencyType.TYPESCRIPT)!!

    val baseTypeScriptKit = object: ArrayList<YaclibModel.Dependency>(){
        init {
            add(protobufJsDependencyBuilder.build())
            add(roylanceCommonDependencyBuilder.build())
        }
    }

    val angularDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^1.5.7").setGroup("angular").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val angularRouteBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^1.5.7").setGroup("angular-route").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val bytebufferBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^5.0.1").setGroup("bytebuffer").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val longBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.2.0").setGroup("long").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val bootstrapBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.3.6").setGroup("bootstrap").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val d3Builder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.5.17").setGroup("d3").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val dagreD3Builder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^0.4.17").setGroup("dagre-d3").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val fontAwesomeBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^4.6.3").setGroup("font-awesome").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val jqueryBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^2.2.4").setGroup("jquery").setType(YaclibModel.DependencyType.TYPESCRIPT)!!
    val webpackStreamBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.2.0").setGroup("webpack-stream").setType(YaclibModel.DependencyType.TYPESCRIPT)!!

    val baseServerTypeScriptKit = object: ArrayList<YaclibModel.Dependency>() {
        init {
            add(angularDependencyBuilder.build())
            add(angularRouteBuilder.build())
            add(bytebufferBuilder.build())
            add(longBuilder.build())
            add(bootstrapBuilder.build())
            add(d3Builder.build())
            add(dagreD3Builder.build())
            add(fontAwesomeBuilder.build())
            add(jqueryBuilder.build())
            add(protobufJsDependencyBuilder.build())
            add(webpackStreamBuilder.build())
        }
    }

    fun buildDependency(dependency: YaclibModel.Dependency): String {
        if (dependency.type == YaclibModel.DependencyType.INTERNAL) {
            if (dependency.hasNpmRepository() && dependency.npmRepository.npmScope.isNotEmpty()) {
                return """"${buildFullName(dependency)}": "${buildVersion(dependency)}"
"""
            }
            else if (dependency.hasNpmRepository() && dependency.npmRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_NPM) {
                val dependencyUrl = JavaUtilities.buildRepositoryUrl(dependency.npmRepository)
                val fileName = ArtifactoryUtilities.buildTarUrl(dependency)
                return """"${buildFullName(dependency)}": "$dependencyUrl/$fileName"
"""
            }
            else {
                return """"${buildFullName(dependency)}": "${buildVersion(dependency)}"
"""
            }
        }

        if (dependency.hasNpmRepository() && dependency.npmRepository.npmScope.isNotEmpty()) {
            return """"${buildFullName(dependency)}": "${buildVersion(dependency)}"
"""
        }
        else if (dependency.npmRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_NPM) {
            return """"${buildFullName(dependency)}": "${dependency.npmRepository.url}"
"""
        }
        else {
            return """"${buildFullName(dependency)}": "${buildVersion(dependency)}"
"""
        }
    }

    fun buildTypeScriptOutput(referencePath: String,
                              exportFactoryName: String,
                              exportedJS: String): String {
        return """
/// <reference path="$referencePath" />
declare var dcodeIO:any;
$exportedJS
export var $exportFactoryName = _root;
"""
    }

    fun buildNpmModel(location: String): NPMPackage? {
        val actualFile = File(location, NPMUtilities.PackageNameJson)
        if (!actualFile.exists()) {
            return null
        }

        return gson.fromJson(actualFile.readText(), NPMPackage::class.java)
    }

    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val projectModel = buildNpmModel(location) ?: return YaclibModel.ProcessReport.getDefaultInstance()

        val splitVersion = projectModel.version!!.split(".")
        if (splitVersion.size < 3) {
            return YaclibModel.ProcessReport.getDefaultInstance()
        }

        val majorVersion = splitVersion[0].toInt()
        val newMinorVersion = splitVersion[1].toInt() + 1

        projectModel.version = "$majorVersion.$newMinorVersion.0"
        File(location, NPMUtilities.PackageNameJson).writeText(gson.toJson(projectModel))

        return YaclibModel.ProcessReport.newBuilder()
                .setNewMajor(majorVersion)
                .setNewMinor(newMinorVersion)
                .setContent(projectModel.version)
                .build()
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val projectModel = buildNpmModel(location) ?: return YaclibModel.ProcessReport.getDefaultInstance()

        val currentKey = buildFullName(otherDependency).trim()

        if (projectModel.dependencies.containsKey(currentKey)) {
            projectModel.dependencies[currentKey] = buildVersion(otherDependency)
        }

        File(location, NPMUtilities.PackageNameJson).writeText(gson.toJson(projectModel))

        return YaclibModel.ProcessReport
            .newBuilder()
            .build()
    }

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        val projectModel = buildNpmModel(location) ?: return YaclibModel.ProcessReport.getDefaultInstance()

        val splitVersion = projectModel.version!!.split(".")
        if (splitVersion.size < 3) {
            return YaclibModel.ProcessReport.getDefaultInstance()
        }

        val majorVersion = splitVersion[0].toInt()
        val minorVersion = splitVersion[1].toInt()

        return YaclibModel.ProcessReport.newBuilder()
                .setNewMinor(minorVersion)
                .setNewMajor(majorVersion)
                .setContent(projectModel.version).build()
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        val projectModel = buildNpmModel(location) ?: return YaclibModel.ProcessReport.getDefaultInstance()
        projectModel.version = buildVersion(dependency)

        File(location, NPMUtilities.PackageNameJson).writeText(gson.toJson(projectModel))

        return YaclibModel.ProcessReport.newBuilder()
                .setNewMajor(dependency.majorVersion)
                .setNewMinor(dependency.minorVersion)
                .setContent(projectModel.version)
                .build()
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        return FileProcessUtilities.executeProcess(location, InitUtilities.TypeScriptCompiler, "")
    }

    override fun buildPackage(location: String,
                              dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        return YaclibModel.ProcessReport.getDefaultInstance()
    }

    override fun publish(location: String,
                         dependency: YaclibModel.Dependency,
                         apiKey: String): YaclibModel.ProcessReport {
        val normalLogs = StringBuilder()
        val errorLogs = StringBuilder()

        if (dependency.hasNpmRepository() &&
                dependency.npmRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_NPM &&
                dependency.npmRepository.url.isNotEmpty()) {
            val tarFileName = Paths.get(location, ArtifactoryUtilities.buildTarFileName(dependency)).toString()
            val result = FileProcessUtilities.createTarFromDirectory(location, tarFileName, neverTarThisDirectory)
            if (result) {
                normalLogs.appendln("successfully created $tarFileName")
            }
            else {
                errorLogs.appendln("error creating $tarFileName")
            }

            val scriptToRun = ArtifactoryUtilities.buildUploadTarGzScript(location, dependency)
            val actualScriptFile = File(location, ArtifactoryUtilities.UploadScriptName)
            actualScriptFile.writeText(scriptToRun)

            FileProcessUtilities.executeProcess(location, InitUtilities.Chmod, "${InitUtilities.ChmodExecutable} $tarFileName")
            FileProcessUtilities.executeProcess(location, InitUtilities.Chmod, "${InitUtilities.ChmodExecutable} $actualScriptFile")

            println("running upload")
            val deployReport = FileProcessUtilities.executeProcess(location, actualScriptFile.toString(), "")
            normalLogs.appendln(deployReport.normalOutput)
            errorLogs.appendln(deployReport.errorOutput)
            println("ran upload upload")
        }
        else {
            val publishReport = FileProcessUtilities.executeProcess(location, InitUtilities.NPM, "publish")
            normalLogs.appendln(publishReport.normalOutput)
            errorLogs.appendln(publishReport.errorOutput)
        }

        return YaclibModel.ProcessReport.newBuilder().setNormalOutput(normalLogs.toString()).setErrorOutput(errorLogs.toString()).build()
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        if (doAnonymously) {
            FileProcessUtilities.executeProcess(location, InitUtilities.Move, "~/.npmrc ~/.npmrctmp")
        }

        val result = FileProcessUtilities.executeProcess(location, InitUtilities.NPM, "install")

        if (doAnonymously) {
            FileProcessUtilities.executeProcess(location, InitUtilities.Move, "~/.npmrctmp ~/.npmrc")
        }

        return result
    }

    private fun buildVersion(dependency: YaclibModel.Dependency): String {
        if (dependency.type == YaclibModel.DependencyType.INTERNAL) {
            return "${dependency.majorVersion}.${dependency.minorVersion}.0"
        }
        else {
            return dependency.thirdPartyDependencyVersion
        }
    }

    private fun buildFullName(dependency: YaclibModel.Dependency): String {
        if (dependency.type == YaclibModel.DependencyType.INTERNAL) {
            if (dependency.hasNpmRepository() && dependency.npmRepository.npmScope.isNotEmpty()) {
                return "${dependency.npmRepository.npmScope}/${dependency.group}.${dependency.name}"
            }
            else {
                return "${dependency.group}.${dependency.name}"
            }
        }

        if (dependency.hasNpmRepository() && dependency.npmRepository.npmScope.isNotEmpty()) {
            return "${dependency.npmRepository.npmScope}/${dependency.group}"
        }
        else {
            return dependency.group
        }
    }
}