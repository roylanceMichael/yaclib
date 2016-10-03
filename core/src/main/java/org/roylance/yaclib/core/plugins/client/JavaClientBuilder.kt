package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import java.nio.file.Paths

class JavaClientBuilder(private val location: String,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        // run node stuff
        val javaClientDirectory = Paths.get(this.location, CommonTokens.ClientApi).toFile()
        val gradleBuildProcess = ProcessBuilder()
                .directory(javaClientDirectory)
                .command(FileProcessUtilities.buildCommand("gradle", "build"))
        FileProcessUtilities.handleProcess(gradleBuildProcess, "gradleBuildProcess")

        if (this.mainDependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
            val artifactoryPublish = ProcessBuilder()
                    .directory(javaClientDirectory)
                    .command(FileProcessUtilities.buildCommand("gradle", "artifactoryPublish"))
            FileProcessUtilities.handleProcess(artifactoryPublish, "artifactoryPublish")
        }
        else {
            val bintrayUpload = ProcessBuilder()
                    .directory(javaClientDirectory)
                    .command(FileProcessUtilities.buildCommand("gradle", "bintrayUpload"))
            FileProcessUtilities.handleProcess(bintrayUpload, "bintrayUploadProcess")
        }

        return true
    }
}