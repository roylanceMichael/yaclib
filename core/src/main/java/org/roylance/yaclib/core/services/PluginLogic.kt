package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.apache.commons.io.FileUtils
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import java.io.IOException
import java.nio.file.Paths

class PluginLogic(
        val typeScriptModelFile: String,
        val nodeAliasName: String?,
        val majorVersion: Int,
        val minorVersion: Int,
        val location: String,
        val repositoryType: YaclibModel.RepositoryType,
        val mainModel: Descriptors.FileDescriptor,
        val mainController: Descriptors.FileDescriptor,
        val dependencyDescriptors: List<DependencyDescriptor>,
        val thirdPartyServerDependencies: List<YaclibModel.Dependency>,
        val nugetKey: String?,
        val githubRepo: String,
        val repoUrl: String,
        val repoName: String,
        val repoUser: String,
        val license: String,
        val author: String): IBuilder<Boolean> {

    override fun build(): Boolean {
        println("deleting ${Paths.get(location, CommonTokens.JavaScriptName).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.JavaScriptName).toFile())
        println("deleting ${Paths.get(location, CommonTokens.ClientApi).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.ClientApi).toFile())
        println("deleting ${Paths.get(location, CommonTokens.CSharpName)}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.CSharpName).toFile())

        println("running main logic now")
        MainLogic(
                this.typeScriptModelFile,
                this.nodeAliasName,
                this.majorVersion,
                this.minorVersion,
                this.location,
                this.repositoryType,
                this.mainModel,
                this.mainController,
                this.dependencyDescriptors,
                this.thirdPartyServerDependencies,
                this.nugetKey != null,
                this.githubRepo,
                this.repoUrl,
                this.repoName,
                this.repoUser,
                this.license,
                this.author).build()

        println("now doing final cleanup")
        if (this.nugetKey != null) {
            this.handleCSharp()
        }
        this.handleJavaClient()
        this.handleJavaScript()
        this.handleServer()

        return true
    }

    private fun handleServer() {
        val javaServerDirectory = Paths.get(this.location, CommonTokens.ServerApi).toFile()
        val mavenCleanProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "clean"))
        this.handleProcess(mavenCleanProcess, "mavenCleanProcess")

        val mavenCompileProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "compile"))
        this.handleProcess(mavenCompileProcess, "mavenCompileProcess")

        val mavenPackageProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "package"))
        this.handleProcess(mavenPackageProcess, "mavenPackageProcess")

        val javascriptDirectory = Paths.get(this.location, CommonTokens.ServerApi, "src", "main", "javascript").toFile()
        val npmInstallProcess = ProcessBuilder()
                .directory(javascriptDirectory)
                .command(FileProcessUtilities.buildCommand("npm", "install"))
        this.handleProcess(npmInstallProcess, "npmInstallProcess")

        val gulpProcess = ProcessBuilder()
                .directory(javascriptDirectory)
                .command(FileProcessUtilities.buildCommand("gulp", ""))
        this.handleProcess(gulpProcess, "gulpProcess")
    }

    private fun handleJavaClient() {
        // run node stuff
        val javaClientDirectory = Paths.get(this.location, CommonTokens.ClientApi).toFile()
        val gradleBuildProcess = ProcessBuilder()
                .directory(javaClientDirectory)
                .command(FileProcessUtilities.buildCommand("gradle", "build"))
        this.handleProcess(gradleBuildProcess, "gradleBuildProcess")

        if (this.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
            val artifactoryPublish = ProcessBuilder()
                    .directory(javaClientDirectory)
                    .command(FileProcessUtilities.buildCommand("gradle", "artifactoryPublish"))
            this.handleProcess(artifactoryPublish, "artifactoryPublish")
        }
        else {
            val bintrayUpload = ProcessBuilder()
                    .directory(javaClientDirectory)
                    .command(FileProcessUtilities.buildCommand("gradle", "bintrayUpload"))
            this.handleProcess(bintrayUpload, "bintrayUploadProcess")
        }
    }

    private fun handleJavaScript() {
        // run node stuff
        val javaScriptDirectory = Paths.get(this.location, CommonTokens.JavaScriptName).toFile()

        val npmInstallProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand("npm", "install"))
        this.handleProcess(npmInstallProcess, "npmInstallProcess")

        val createModelJsonProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(), "../api/src/main/resources/*.proto > $ModelJson", false))
        this.handleProcess(createModelJsonProcess, "createModelJsonProcess")

        val createModelJSProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(), "../api/src/main/resources/*.proto -t js > $ModelJS", false))
        this.handleProcess(createModelJSProcess, "createModelJSProcess")

        val proto2TypeScriptProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "proto2typescript", Bin, "proto2typescript-bin.js").toString(), "--file $ModelJson > $typeScriptModelFile.d.ts", false))
        this.handleProcess(proto2TypeScriptProcess, "proto2TypeScriptProcess")

        val protoTypeScriptHelperProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "protobuftshelper", "run.sh").toString(), "$ModelJS ${typeScriptModelFile}Factory.ts ./$typeScriptModelFile.d.ts $typeScriptModelFile", false))
        this.handleProcess(protoTypeScriptHelperProcess, "protoTypeScriptHelperProcess")

        Paths.get(javaScriptDirectory.toString(), ModelJson).toFile().delete()
        Paths.get(javaScriptDirectory.toString(), ModelJS).toFile().delete()

        val tsCompileProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand("tsc", ""))
        this.handleProcess(tsCompileProcess, "tsCompileProcess")

        val npmPublishProcess = ProcessBuilder()
                .directory(Paths.get(this.location, CommonTokens.JavaScriptName).toFile())
                .command(FileProcessUtilities.buildCommand("npm", "publish"))
        this.handleProcess(npmPublishProcess, "npmPublishProcess")
    }

    private fun handleCSharp() {
        val mainDependency = YaclibModel.Dependency.newBuilder()
                .setName(CommonTokens.ApiName)
                .setMajorVersion(this.majorVersion)
                .setMinorVersion(this.minorVersion)
                .setTypescriptModelFile(this.typeScriptModelFile)
                .setGroup(this.mainModel.`package`)
                .build()

        val csharpDirectory = Paths.get(this.location, CommonTokens.CSharpName, CSharpUtilities.buildFullName(mainDependency)).toFile()

        val generateProtoProcess = CSharpUtilities.buildProtobufs(this.location, mainDependency)
        this.handleProcess(generateProtoProcess, "generateProtoProcess")

        val dotnetRestoreProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "restore"))
        this.handleProcess(dotnetRestoreProcess, "dotnetRestoreProcess")

        val dotnetBuildProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "build"))
        this.handleProcess(dotnetBuildProcess, "dotnetBuildProcess")

        val dotnetPackProcess = ProcessBuilder()
                .directory(csharpDirectory)
                .command(FileProcessUtilities.buildCommand(DotNet, "pack"))
        this.handleProcess(dotnetPackProcess, "dotnetPackProcess")

        val nugetDirectoryLocation = Paths.get(csharpDirectory.toString(), "bin", "Debug").toFile()
        val nugetPackageName = "${CSharpUtilities.buildFullName(mainDependency)}.${this.majorVersion}.${this.minorVersion}.0.nupkg"
        val dotnetPublishProcess = ProcessBuilder()
                .directory(nugetDirectoryLocation)
                .command(FileProcessUtilities.buildCommand(Nuget, "push $nugetPackageName ${this.nugetKey}"))
        this.handleProcess(dotnetPublishProcess, "dotnetPublishProcess")
    }

    private fun handleProcess(process: ProcessBuilder, name: String) {
        try {
            process.redirectError(ProcessBuilder.Redirect.INHERIT)
            process.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            process.start().waitFor()
            println("finished $name")
        }
        catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val Bin = "bin"
        private const val NodeModules = "node_modules"
        private const val ModelJson = "model.json"
        private const val ModelJS = "model.js"
        private const val Nuget = "nuget"
        private const val DotNet = "dotnet"
    }
}