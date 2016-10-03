package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import java.nio.file.Paths

class JavaServerBuilder(private val location: String): IBuilder<Boolean> {
    override fun build(): Boolean {
        val javaServerDirectory = Paths.get(location, CommonTokens.ServerApi).toFile()
        val mavenCleanProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "clean"))
        FileProcessUtilities.handleProcess(mavenCleanProcess, "mavenCleanProcess")

        val mavenCompileProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "compile"))
        FileProcessUtilities.handleProcess(mavenCompileProcess, "mavenCompileProcess")

        val mavenPackageProcess = ProcessBuilder()
                .directory(javaServerDirectory)
                .command(FileProcessUtilities.buildCommand("mvn", "package"))
        FileProcessUtilities.handleProcess(mavenPackageProcess, "mavenPackageProcess")

        val javascriptDirectory = Paths.get(this.location, CommonTokens.ServerApi, "src", "main", "javascript").toFile()
        val npmInstallProcess = ProcessBuilder()
                .directory(javascriptDirectory)
                .command(FileProcessUtilities.buildCommand("npm", "install"))
        FileProcessUtilities.handleProcess(npmInstallProcess, "npmInstallProcess")

        val gulpProcess = ProcessBuilder()
                .directory(javascriptDirectory)
                .command(FileProcessUtilities.buildCommand("gulp", ""))
        FileProcessUtilities.handleProcess(gulpProcess, "gulpProcess")

        return true
    }
}