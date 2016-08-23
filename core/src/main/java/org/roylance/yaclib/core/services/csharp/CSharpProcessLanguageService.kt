package org.roylance.yaclib.core.services.csharp

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import java.util.*

class CSharpProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                                mainDependency: YaclibModel.Dependency,
                                thirdPartyDependencies: MutableList<YaclibModel.Dependency>): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        val solutionGuid = UUID.randomUUID().toString().toUpperCase()
        val projectGuid = UUID.randomUUID().toString().toUpperCase()

        returnList.addFiles(XProjBuilder(projectGuid, mainDependency).build())
        returnList.addFiles(IHttpClientBuilder(mainDependency).build())
        returnList.addFiles(SolutionBuilder(solutionGuid, projectGuid, mainDependency).build())
        returnList.addFiles(ProjectJsonBuilder(mainDependency).build())

        controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(CSharpServiceBuilder(mainDependency, controller).build())
                returnList.addFiles(CSharpServiceImplementationBuilder(controllerDependency.dependency, controller).build())
            }
        }

        return returnList.build()
    }
}