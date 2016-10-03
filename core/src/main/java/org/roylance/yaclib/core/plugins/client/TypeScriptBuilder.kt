package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.ArtifactoryUtilities
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.io.File
import java.nio.file.Paths

class TypeScriptBuilder(private val location: String,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        // run node stuff
        val javaScriptDirectory = Paths.get(location, CommonTokens.JavaScriptName).toFile()

        val npmInstallProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand("npm", "install"))
        FileProcessUtilities.handleProcess(npmInstallProcess, "npmInstallProcess")

        val createModelJsonProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(), "../api/src/main/resources/*.proto > ${ModelJson}", false))
        FileProcessUtilities.handleProcess(createModelJsonProcess, "createModelJsonProcess")

        val createModelJSProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(), "../api/src/main/resources/*.proto -t js > ${ModelJS}", false))
        FileProcessUtilities.handleProcess(createModelJSProcess, "createModelJSProcess")

        val proto2TypeScriptProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand(Paths.get(javaScriptDirectory.toString(), NodeModules, "proto2typescript", Bin, "proto2typescript-bin.js").toString(), "--file ${ModelJson} > ${mainDependency.typescriptModelFile}.d.ts", false))
        FileProcessUtilities.handleProcess(proto2TypeScriptProcess, "proto2TypeScriptProcess")

        // protobuf ts helper
        val typeScriptDefinitionPath = "./${mainDependency.typescriptModelFile}.d.ts"
        val inputFileString = FileProcessUtilities.readFile(Paths.get(javaScriptDirectory.toString(), ModelJS).toString())
        val outputTypeScript = TypeScriptUtilities.buildTypeScriptOutput(
                typeScriptDefinitionPath,
                mainDependency.typescriptModelFile,
                inputFileString)
        FileProcessUtilities.writeFile(outputTypeScript, Paths.get(javaScriptDirectory.toString(), "${mainDependency.typescriptModelFile}Factory.ts").toString())

        Paths.get(javaScriptDirectory.toString(), ModelJson).toFile().delete()
        Paths.get(javaScriptDirectory.toString(), ModelJS).toFile().delete()

        val tsCompileProcess = ProcessBuilder()
                .directory(javaScriptDirectory)
                .command(FileProcessUtilities.buildCommand("tsc", ""))
        FileProcessUtilities.handleProcess(tsCompileProcess, "tsCompileProcess")

        if (mainDependency.hasNpmRepository() &&
                mainDependency.npmRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_NPM &&
                mainDependency.npmRepository.url.length > 0) {
            val scriptToRun = ArtifactoryUtilities.buildUploadTarGzScript(javaScriptDirectory.toString(),
                    mainDependency)

            val actualScriptFile = File(javaScriptDirectory, ArtifactoryUtilities.UploadScriptName)
            actualScriptFile.writeText(scriptToRun)

            val scriptPermissionProcess = ProcessBuilder()
                    .directory(javaScriptDirectory)
                    .command(FileProcessUtilities.buildCommand("chmod", "755 $actualScriptFile"))
            FileProcessUtilities.handleProcess(scriptPermissionProcess, "scriptPermissionProcess")

            val deployProcess = ProcessBuilder()
                    .directory(javaScriptDirectory)
                    .command(FileProcessUtilities.buildCommand("bash", actualScriptFile.toString()))

            FileProcessUtilities.handleProcess(deployProcess, "deployProcess")
        }
        else {
            val npmPublishProcess = ProcessBuilder()
                    .directory(Paths.get(this.location, CommonTokens.JavaScriptName).toFile())
                    .command(FileProcessUtilities.buildCommand("npm", "publish"))
            FileProcessUtilities.handleProcess(npmPublishProcess, "npmPublishProcess")
        }
        return true
    }

    companion object {
        private const val Bin = "bin"
        private const val NodeModules = "node_modules"
        private const val ModelJson = "model.json"
        private const val ModelJS = "model.js"
    }
}