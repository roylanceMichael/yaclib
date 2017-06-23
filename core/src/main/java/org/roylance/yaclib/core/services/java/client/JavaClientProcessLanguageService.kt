package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.ReadmeBuilder
import org.roylance.yaclib.core.services.java.common.GradleSettingsBuilder
import org.roylance.yaclib.core.services.java.common.PropertiesBuilder
import org.roylance.yaclib.core.utilities.JavaUtilities
import java.util.*

class JavaClientProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        val buildProperties = HashMap<String, String>()
        buildProperties[JavaUtilities.KotlinName] = JavaUtilities.KotlinVersion
        buildProperties[JavaUtilities.RoylanceCommonName] = JavaUtilities.RoylanceCommonVersion
        buildProperties[JavaUtilities.ArtifactoryName] = JavaUtilities.ArtifactoryVersion
        buildProperties[JavaUtilities.BintrayName] = JavaUtilities.BintrayVersion
        buildProperties[JavaUtilities.RetrofitName] = JavaUtilities.RetrofitVersion

        returnList.addFiles(PropertiesBuilder(projectInformation.controllers, projectInformation.mainDependency, projectInformation.thirdPartyDependenciesList, buildProperties).build())
        returnList.addFiles(ReadmeBuilder(projectInformation.mainDependency).build())
        returnList.addFiles(GradleFileBuilder(projectInformation, CommonTokens.ClientApi).build())
        returnList.addFiles(GradleSettingsBuilder(CommonTokens.ClientApi).build())

        projectInformation.controllers.controllerDependenciesList
                .filter { it.dependency.group == projectInformation.mainDependency.group && it.dependency.name == projectInformation.mainDependency.name }
                .forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(JavaRetrofitBuilder(controller, projectInformation.mainDependency).build())
                returnList.addFiles(KotlinServiceBuilder(controller, projectInformation.mainDependency).build())
                if (projectInformation.doNotUseJson) {
                    returnList.addFiles(KotlinServiceImplementationBuilder(controller, projectInformation.mainDependency).build())
                }
                else {
                    returnList.addFiles(KotlinServiceImplementationJsonBuilder(controller, projectInformation.mainDependency).build())
                }
            }
        }

        return returnList.build()
    }
}