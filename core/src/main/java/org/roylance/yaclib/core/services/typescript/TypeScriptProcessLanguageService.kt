package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class TypeScriptProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                                mainDependency: YaclibModel.Dependency,
                                thirdPartyDependencies: MutableList<YaclibModel.Dependency>): YaclibModel.AllFiles {
        thirdPartyDependencies.addAll(TypeScriptUtilities.baseTypeScriptKit)

        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(NPMPackageBuilder(mainDependency, thirdPartyDependencies).build())
        returnList.addFiles(HttpExecuteServiceBuilder().build())
        controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(TypeScriptServiceBuilder(controller, controllerDependency.dependency).build())
                returnList.addFiles(TypeScriptServiceImplementationBuilder(controller, controllerDependency.dependency)
                        .build())
            }
        }

        return returnList.build()
    }
}