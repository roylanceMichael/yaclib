package org.roylance.yaclib.core.plugins

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.cpp.server.jni.CPPJNILanguageService
import org.roylance.yaclib.core.services.csharp.CSharpProcessLanguageService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.python.PythonProcessLanguageService
import org.roylance.yaclib.core.services.swift.SwiftProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import java.nio.file.Paths

class ClientLogic(
    private val location: String,
    private val projectInformation: YaclibModel.ProjectInformation) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val filePersistService = FilePersistService()

    val javaClientFiles = JavaClientProcessLanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(this.location, CommonTokens.ClientApi).toString(),
        javaClientFiles)

    val javaJNIFiles = CPPJNILanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(this.location, CommonTokens.ServerJni).toString(),
        javaJNIFiles)

    val typeScriptFiles = TypeScriptProcessLanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(location, CommonTokens.JavaScriptName).toString(),
        typeScriptFiles)

    val csharpFiles = CSharpProcessLanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(location, CommonTokens.CSharpName).toString(),
        csharpFiles)

    val pythonFiles = PythonProcessLanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(location, CommonTokens.PythonName).toString(),
        pythonFiles)

    val swiftDirectory = Paths.get(location, CommonTokens.SwiftName).toFile()
    if (!swiftDirectory.exists()) {
      swiftDirectory.mkdirs()
    }

    val swiftFiles = SwiftProcessLanguageService().buildInterface(projectInformation)
    filePersistService.persistFiles(Paths.get(location).toString(), swiftFiles)

    return true
  }
}