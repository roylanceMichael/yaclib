package org.roylance.yaclib.core.services.python

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder

class PythonProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(PropertiesBuilder(projectInformation.mainDependency).build())
        returnList.addFiles(InitBuilder(projectInformation.mainDependency).build())
        returnList.addFiles(ReadmeBuilder(projectInformation.mainDependency).build())
        returnList.addFiles(SetupBuilder().build())
        returnList.addFiles(SetupCFGBuilder().build())

        projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(PythonServiceImplementationBuilder(projectInformation.mainDependency, controller).build())
            }
        }

        return returnList.build()
    }
}