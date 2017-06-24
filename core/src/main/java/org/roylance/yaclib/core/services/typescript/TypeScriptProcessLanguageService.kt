package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.NPMBlobBuilder
import org.roylance.yaclib.core.services.common.NPMPackageBuilder
import org.roylance.yaclib.core.services.common.ReadmeBuilder
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class TypeScriptProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val actualProjectInformation = projectInformation.toBuilder()
                .addAllThirdPartyDependencies(TypeScriptUtilities.baseTypeScriptKit)
                .build()

        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(NPMBlobBuilder(actualProjectInformation).build())
        returnList.addFiles(NPMPackageBuilder(actualProjectInformation).build())
        returnList.addFiles(HttpExecuteServiceBuilder().build())
        returnList.addFiles(ReadmeBuilder(actualProjectInformation.mainDependency).build())

        val allControllers = YaclibModel.AllControllers.newBuilder()
        actualProjectInformation.controllers.controllerDependenciesList
                .filter { it.dependency.group == projectInformation.mainDependency.group && it.dependency.name == projectInformation.mainDependency.name }
                .forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(TypeScriptServiceBuilder(
                        controller).build())
                returnList.addFiles(TypeScriptServiceImplementationBuilder(controller,
                        controllerDependency.dependency)
                        .build())

                allControllers.addControllers(controller)
            }
        }

        returnList.addFiles(TSConfigBuilder(allControllers.build(), actualProjectInformation.mainDependency).build())
        return returnList.build()
    }
}