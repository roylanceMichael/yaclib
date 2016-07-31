package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService

class JavaClientProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                                mainDependency: YaclibModel.Dependency,
                                thirdPartyDependencies: MutableList<YaclibModel.Dependency>): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(GradleFileBuilder(controllerDependencies, mainDependency).build())
        returnList.addFiles(GradlewFileBuilder().build())
        returnList.addFiles(GradlewBatFileBuilder().build())
        returnList.addFiles(GradleSettingsBuilder().build())

        controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(JavaRetrofitBuilder(controller, mainDependency).build())
                returnList.addFiles(KotlinServiceBuilder(controller, mainDependency).build())
                returnList.addFiles(KotlinServiceImplementationBuilder(controller, mainDependency).build())
            }
        }

        return returnList.build()
    }
}