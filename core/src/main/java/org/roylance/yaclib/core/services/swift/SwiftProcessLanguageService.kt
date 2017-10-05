package org.roylance.yaclib.core.services.swift

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService

class SwiftProcessLanguageService : IProcessLanguageService {
  override fun buildInterface(
      projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
    val returnList = YaclibModel.AllFiles.newBuilder()

    returnList.addFiles(SwiftPBXProjBuilder(projectInformation).build())
    returnList.addFiles(SwiftContentsXCWorkspaceDataBuilder(projectInformation).build())
    returnList.addFiles(SwiftShareDataBuilder(projectInformation).build())
    returnList.addFiles(SwiftSchemeManagementBuilder(projectInformation).build())

    returnList.addFiles(CartFileBuilder().build())
    returnList.addFiles(InfoPlistBuilder(projectInformation, false).build())

    projectInformation
        .controllers.controllerDependenciesList
        .filter { it.dependency.group == projectInformation.mainDependency.group && it.dependency.name == projectInformation.mainDependency.name }
        .forEach { controllerDependency ->
          controllerDependency.controllers.controllersList.forEach { controller ->
            returnList.addFiles(SwiftServiceBuilder(controller).build())
            returnList.addFiles(SwiftServiceImplementationBuilder(controller).build())
          }
        }

    return returnList.build()
  }
}