package org.roylance.yaclib.core.plugins.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.nio.file.Paths

class CPPJNIServerBuilder(private val location: String,
    private val projectInformation: YaclibModel.ProjectInformation) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val javaServerJNIDirectory = Paths.get(location, CommonTokens.ServerJni).toFile()
    println(InitUtilities.buildPhaseMessage("java server jni begin"))

    val cleanReport = GradleUtilities.clean(javaServerJNIDirectory.toString())
    println(cleanReport.normalOutput)
    println(cleanReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("compiling"))
    val compileReport = GradleUtilities.build(javaServerJNIDirectory.toString())
    println(compileReport.normalOutput)
    println(compileReport.errorOutput)

    val javaHPath = Paths.get(javaServerJNIDirectory.toString(), "build", "classes",
        "main").toFile()
    val jniPath = Paths.get(javaServerJNIDirectory.toString(), "src", "main", "jni").toFile()

    projectInformation.controllers.controllerDependenciesList
        .filter { it.dependency.group == projectInformation.mainDependency.group && it.dependency.name == projectInformation.mainDependency.name }
        .forEach { controllerDependency ->
          controllerDependency.controllers.controllersList.forEach { controller ->
            val controllerName = JavaUtilities.buildJavaBridgeName(controller)
            val fullName = "${controllerDependency.dependency.group}.${CommonTokens.ServicesName}.$controllerName"

            println(InitUtilities.buildPhaseMessage("creating $fullName"))
            val report = FileProcessUtilities.executeProcess(javaHPath.toString(), "javah",
                fullName)
            println(report.normalOutput)
            println(report.errorOutput)
          }
        }

    println(InitUtilities.buildPhaseMessage("creating jni directory, if it doesn't exist"))
    val createReport = FileProcessUtilities.executeProcess(javaServerJNIDirectory.toString(),
        "mkdir", "-p $jniPath")
    println(createReport.normalOutput)
    println(createReport.errorOutput)

    println(InitUtilities.buildPhaseMessage("moving h files to $jniPath"))
    val moveReport = FileProcessUtilities.executeProcess(javaHPath.toString(), "mv", "*.h $jniPath")
    println(moveReport.normalOutput)
    println(moveReport.errorOutput)

    return true
  }
}