package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.ArtifactoryUtilities
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities
import java.io.File
import java.nio.file.Paths

class PythonBuilder(private val location: String,
                    private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        val pythonDirectory = Paths.get(this.location, CommonTokens.PythonName).toFile()

        val generateProtoProcess = PythonUtilities.buildProtobufs(location, mainDependency)
        FileProcessUtilities.handleProcess(generateProtoProcess, "generateProtoProcess")

        val removePycFiles = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("find", ". -name '*.pyc' -delete"))
        FileProcessUtilities.handleProcess(removePycFiles, "removePycFiles")

        val removeDSStoreFiles = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("find", ". -name '*.DS_Store' -delete"))
        FileProcessUtilities.handleProcess(removeDSStoreFiles, "removeDSStoreFiles")

        val buildWheelProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("python", "setup.py bdist_wheel --universal"))
        FileProcessUtilities.handleProcess(buildWheelProcess, "buildWheelProcess")

        val moveWheelProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("mv", "${Paths.get(pythonDirectory.toString(), DistName)}/*.whl  $pythonDirectory/${PythonUtilities.buildWheelFileName(mainDependency)}"))
        FileProcessUtilities.handleProcess(moveWheelProcess, "moveWheelProcess")

        val removeDistProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("rm", "-rf $DistName"))
        FileProcessUtilities.handleProcess(removeDistProcess, "removeDistProcess")

        val removeBuildProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("rm", "-rf build"))
        FileProcessUtilities.handleProcess(removeBuildProcess, "removeBuildProcess")

        val removeEggInfoProcess = ProcessBuilder()
                .directory(pythonDirectory)
                .command(FileProcessUtilities.buildCommand("rm", "-rf *egg-info"))
        FileProcessUtilities.handleProcess(removeEggInfoProcess, "removeEggInfoProcess")

        if (mainDependency.hasPipRepository() &&
                mainDependency.pipRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_PYTHON &&
                mainDependency.pipRepository.url.length > 0) {
            val scriptToRun = ArtifactoryUtilities.buildUploadWheelScript(pythonDirectory.toString(), mainDependency)

            val actualScriptFile = File(pythonDirectory, ArtifactoryUtilities.UploadWheelScriptName)
            actualScriptFile.writeText(scriptToRun)

            val scriptPermissionProcess = ProcessBuilder()
                    .directory(pythonDirectory)
                    .command(FileProcessUtilities.buildCommand("chmod", "755 $actualScriptFile"))
            FileProcessUtilities.handleProcess(scriptPermissionProcess, "scriptPermissionProcess")

            val deployProcess = ProcessBuilder()
                    .directory(pythonDirectory)
                    .command(FileProcessUtilities.buildCommand("bash", actualScriptFile.toString()))

            FileProcessUtilities.handleProcess(deployProcess, "deployProcess")
        }

        return true
    }

    companion object {
        private const val DistName = "dist"
    }
}