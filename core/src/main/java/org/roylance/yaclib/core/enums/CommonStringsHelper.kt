package org.roylance.yaclib.core.enums

object CommonStringsHelper {
    const val DebianBuild = "build/debian/"
    const val DebianBuildBuild = "build/debian/build/"
    const val BuildInstallPath = "build/install/"
    const val SBinPath = "${DebianBuildBuild}usr/sbin/"
    const val DebianEtcPath = "${DebianBuildBuild}etc/init.d"
    const val CustomScript = "custom.sh"

    const val CreateAndInstallPackageName = "create_install_package.sh"
    private const val RunName = "run"
    private const val StopName = "stop"

    fun buildDebianEtcPath(projectName: String): String {
        return "$DebianEtcPath/$projectName"
    }

    fun buildProjectRunScriptLocation(projectName: String): String {
        return buildInstallPath(projectName, "${projectName}_$RunName.sh")
    }

    fun buildSystemRunScriptLocation(projectName: String): String {
        return "$SBinPath${projectName}_$RunName"
    }

    fun buildProjectStopScriptLocation(projectName: String): String {
        return buildInstallPath(projectName, "${projectName}_$StopName.sh")
    }

    fun buildSystemStopScriptLocation(projectName: String): String {
        return "$SBinPath${projectName}_$StopName"
    }

    fun buildInstallPath(projectName: String, fileName: String): String {
        return "$BuildInstallPath$projectName/$fileName"
    }
}