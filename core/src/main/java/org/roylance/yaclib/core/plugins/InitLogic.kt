package org.roylance.yaclib.core.plugins

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.java.client.GradleFileBuilder
import org.roylance.yaclib.core.services.java.client.GradleSettingsBuilder
import org.roylance.yaclib.core.services.java.init.ControllerProtoBuilder
import org.roylance.yaclib.core.services.java.init.InitPropertiesBuilder
import org.roylance.yaclib.core.services.java.init.ModelProtoBuilder
import org.roylance.yaclib.core.services.java.init.YaclibGradleBuilder
import java.nio.file.Paths

class InitLogic(private val location: String,
                private val mainDependency: YaclibModel.Dependency,
                private val yaclibDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        val projectInformation = YaclibModel.ProjectInformation.newBuilder()
                .setMainDependency(mainDependency)
                .build()

        val files = YaclibModel.AllFiles.newBuilder()

        val gradleFile = GradleFileBuilder(projectInformation, CommonTokens.ApiName).build()
        val gradleSettings = GradleSettingsBuilder(CommonTokens.ApiName).build()
        val yaclibFile = YaclibGradleBuilder(mainDependency).build()
        val propertiesFile = InitPropertiesBuilder(mainDependency, yaclibDependency).build()
        val modelFile = ModelProtoBuilder(mainDependency).build()
        val controllerFile = ControllerProtoBuilder(mainDependency).build()

        files.addFiles(gradleFile)
                .addFiles(gradleSettings)
                .addFiles(yaclibFile)
                .addFiles(propertiesFile)
                .addFiles(modelFile)
                .addFiles(controllerFile)

        val filePersistService = FilePersistService()

        val apiLocation = Paths.get(location, CommonTokens.ApiName).toString()
        filePersistService.persistFiles(apiLocation, files.build())

        return true
    }
}