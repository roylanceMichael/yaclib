package org.roylance.yaclib.core.plugins

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.java.client.GradleFileBuilder
import org.roylance.yaclib.core.services.java.common.GradleSettingsBuilder
import org.roylance.yaclib.core.services.java.init.*
import java.nio.file.Paths

class InitLogic(private val location: String,
    private val mainDependency: YaclibModel.Dependency,
    private val yaclibDependency: YaclibModel.Dependency) : IBuilder<Boolean> {
  override fun build(): Boolean {
    val projectInformation = YaclibModel.ProjectInformation.newBuilder()
        .setMainDependency(mainDependency)
        .build()

    val files = YaclibModel.AllFiles.newBuilder()

    val generateProtoFile = GenerateProtoBuilder().build()
    val gradleFile = GradleFileBuilder(projectInformation, mainDependency.name).build()
    val gradleSettings = GradleSettingsBuilder(mainDependency.name).build()
    val yaclibFile = YaclibGradleBuilder(mainDependency).build()
    val propertiesFile = InitPropertiesBuilder(mainDependency, yaclibDependency).build()
    val modelFile = ModelProtoBuilder(mainDependency).build()
    val controllerFile = ControllerProtoBuilder(mainDependency).build()

    files.addFiles(gradleFile)
        .addFiles(generateProtoFile)
        .addFiles(gradleSettings)
        .addFiles(yaclibFile)
        .addFiles(propertiesFile)
        .addFiles(modelFile)
        .addFiles(controllerFile)

    val filePersistService = FilePersistService()

    val apiLocation = Paths.get(location, mainDependency.name).toString()
    filePersistService.persistFiles(apiLocation, files.build())

    return true
  }
}