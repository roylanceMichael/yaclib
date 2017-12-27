package org.roylance.yaclib.core.services.cpp.server.jni

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder
import org.roylance.yaclib.core.services.java.client.KotlinServiceBuilder
import org.roylance.yaclib.core.services.java.common.GradleSettingsBuilder
import org.roylance.yaclib.core.services.java.common.PropertiesBuilder
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.util.HashMap

class CPPJNILanguageService : IProcessLanguageService {
  override fun buildInterface(
      projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
    val returnList = YaclibModel.AllFiles.newBuilder()

    val buildProperties = HashMap<String, String>()
    buildProperties[JavaUtilities.KotlinName] = YaclibStatics.KotlinVersion
    buildProperties[JavaUtilities.RoylanceCommonName] = YaclibStatics.RoylanceCommonVersion
    buildProperties[JavaUtilities.ArtifactoryName] = YaclibStatics.ArtifactoryVersion
    buildProperties[JavaUtilities.BintrayName] = YaclibStatics.BintrayVersion
    buildProperties[JavaUtilities.RetrofitName] = YaclibStatics.RetrofitVersion

    buildProperties[JavaUtilities.GroupName] = projectInformation.mainDependency.group
    buildProperties[JavaUtilities.NameName] = projectInformation.mainDependency.name
    buildProperties[JavaUtilities.MinorName] = projectInformation.mainDependency.minorVersion
    buildProperties[JavaUtilities.MajorName] = projectInformation.mainDependency.majorVersion
    buildProperties[JavaUtilities.YaclibVersionName] = YaclibStatics.YaclibVersion

    returnList.addFiles(PropertiesBuilder(projectInformation.controllers,
        projectInformation.mainDependency,
        projectInformation.thirdPartyDependenciesList,
        buildProperties).build())
    returnList.addFiles(ReadmeBuilder(projectInformation.mainDependency).build())
    returnList.addFiles(CPPJNIGradleBuilder(projectInformation, "${projectInformation.mainDependency.name}${CommonTokens.JNIAffix}${CommonTokens.ServerSuffix}").build())
    returnList.addFiles(GradleSettingsBuilder("${projectInformation.mainDependency.name}${CommonTokens.JNIAffix}${CommonTokens.ServerSuffix}").build())

    projectInformation.controllers.controllerDependenciesList
        .filter {
          it.dependency.group == projectInformation.mainDependency.group &&
              it.dependency.name == projectInformation.mainDependency.name
        }
        .forEach { controllerDependency ->
          controllerDependency.controllers.controllersList.forEach { controller ->
            returnList.addFiles(
                CPPJNIBridgeBuilder(controller, projectInformation.mainDependency).build())
            returnList.addFiles(CPPJNIBridgeImplementationBuilder(controller,
                projectInformation.mainDependency).build())
            returnList.addFiles(
                KotlinServiceBuilder(controller, projectInformation.mainDependency).build())
          }
        }

    return returnList.build()
  }
}