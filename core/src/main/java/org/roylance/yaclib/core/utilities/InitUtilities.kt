package org.roylance.yaclib.core.utilities

object InitUtilities {
    const val OsNameProperty = "os.name"

    const val Chmod = "chmod"
    const val ChmodExecutable = "755"
    const val RemoveDirectory = "rm"
    const val Find = "find"
    const val Move = "mv"
    const val Bash = "bash"
    const val Curl = "curl"

    const val Gradle = "gradle"
    const val Maven = "mvn"

    const val Nuget = "nuget"
    const val DotNet = "dotnet"

    const val Python = "python"
    const val Pip = "pip"

    const val TypeScriptCompiler = "tsc"
    const val NPM = "npm"

    const val Protoc = "protoc"

    const val Separator = """---------------------------------------------------------------------------------------------------------"""

    const val MinimumRequiredErrorMessage = """Minimum requirements to run Yaclib not met. Please ensure the following requirements are met:
OS is OSX/MacOS or Linux
protoc, mvn, gradle, python, tsc, npm can all be located with "which"
optional: dotnet and nuget can be located with "which"
MAKE SURE THAT ~/.bashrc or ~/.bash_profile PATH contains references to the folder these apps are located in!
"""

    fun hasMinimumRequired(): Boolean {
        return isNotWindows() &&
                hasNPM() &&
                hasTypeScriptCompiler() &&
                hasPython() &&
                hasGradle() &&
                hasProtoc() &&
                hasMaven()
    }

    fun hasCSharp(): Boolean {
        return hasDotNet() && hasNuget()
    }

    fun hasProtoc(): Boolean {
        return FileProcessUtilities.getActualLocation(Protoc).length > 0
    }

    fun hasGradle(): Boolean {
        return FileProcessUtilities.getActualLocation(Gradle).length > 0
    }

    fun hasMaven(): Boolean {
        return FileProcessUtilities.getActualLocation(Maven).length > 0
    }

    fun hasDotNet(): Boolean {
        return FileProcessUtilities.getActualLocation(DotNet).length > 0
    }

    fun hasNuget(): Boolean {
        return FileProcessUtilities.getActualLocation(Nuget).length > 0
    }

    fun hasPython(): Boolean {
        return FileProcessUtilities.getActualLocation(Python).length > 0
    }

    fun hasTypeScriptCompiler(): Boolean {
        return FileProcessUtilities.getActualLocation(TypeScriptCompiler).length > 0
    }

    fun hasNPM(): Boolean {
        return FileProcessUtilities.getActualLocation(NPM).length > 0
    }

    fun isNotWindows(): Boolean {
        return !System.getProperty(OsNameProperty).toLowerCase().startsWith("window")
    }

    fun buildPhaseMessage(message: String): String {
        return """$Separator
$message
$Separator"""
    }
}