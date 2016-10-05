package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder
import org.roylance.yaclib.core.services.java.common.PropertiesBuilder

class JavaClientProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(PropertiesBuilder(projectInformation.controllers, projectInformation.mainDependency, projectInformation.thirdPartyDependenciesList).build())
        returnList.addFiles(ReadmeBuilder(projectInformation.mainDependency).build())
        returnList.addFiles(GradleFileBuilder(projectInformation).build())
        returnList.addFiles(GradleSettingsBuilder().build())

        projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(JavaRetrofitBuilder(controller, projectInformation.mainDependency).build())
                returnList.addFiles(KotlinServiceBuilder(controller, projectInformation.mainDependency).build())
                returnList.addFiles(KotlinServiceImplementationBuilder(controller, projectInformation.mainDependency).build())
            }
        }

        return returnList.build()
    }
}