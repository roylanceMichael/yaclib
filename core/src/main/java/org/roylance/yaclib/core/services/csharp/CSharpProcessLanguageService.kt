package org.roylance.yaclib.core.services.csharp

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder
import java.util.*

class CSharpProcessLanguageService : IProcessLanguageService {
  override fun buildInterface(
      projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
    val returnList = YaclibModel.AllFiles.newBuilder()

    val solutionGuid = UUID.randomUUID().toString().toUpperCase()
    val projectGuid = UUID.randomUUID().toString().toUpperCase()

    returnList.addFiles(ReadmeBuilder(projectInformation.mainDependency).build())
    returnList.addFiles(XProjBuilder(projectGuid, projectInformation.mainDependency).build())
    returnList.addFiles(IHttpClientBuilder(projectInformation.mainDependency).build())
    returnList.addFiles(
        SolutionBuilder(solutionGuid, projectGuid, projectInformation.mainDependency).build())
    returnList.addFiles(ProjectJsonBuilder(projectInformation.mainDependency).build())

    projectInformation.controllers.controllerDependenciesList
        .filter { it.dependency.group == projectInformation.mainDependency.group && it.dependency.name == projectInformation.mainDependency.name }
        .forEach { controllerDependency ->
          controllerDependency.controllers.controllersList.forEach { controller ->
            returnList.addFiles(
                CSharpServiceBuilder(projectInformation.mainDependency, controller).build())
            returnList.addFiles(CSharpServiceImplementationBuilder(controllerDependency.dependency,
                controller).build())
          }
        }

    return returnList.build()
  }
}