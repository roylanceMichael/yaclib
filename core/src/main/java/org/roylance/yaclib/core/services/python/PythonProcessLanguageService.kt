package org.roylance.yaclib.core.services.python

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder

class PythonProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                                mainDependency: YaclibModel.Dependency,
                                thirdPartyDependencies: MutableList<YaclibModel.Dependency>): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(InitBuilder(mainDependency).build())
        returnList.addFiles(ReadmeBuilder(mainDependency).build())
        returnList.addFiles(SetupBuilder(mainDependency).build())
        returnList.addFiles(SetupCFGBuilder().build())

        controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(PythonServiceImplementationBuilder(mainDependency, controller).build())
            }
        }

        return returnList.build()
    }
}