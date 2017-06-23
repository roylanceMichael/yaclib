package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.NPMBlobBuilder
import org.roylance.yaclib.core.services.common.NPMPackageBuilder
import org.roylance.yaclib.core.services.java.common.GradleSettingsBuilder
import org.roylance.yaclib.core.services.java.common.PropertiesBuilder
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.util.*

class JavaServerProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        val actualProjectInformation = projectInformation.toBuilder()
                .setIsServer(true)
                .addAllThirdPartyDependencies(TypeScriptUtilities.baseServerTypeScriptKit)
                .build()

        if (projectInformation.mainDependency.serverType == YaclibModel.ServerType.MAVEN_TOMCAT_EMBEDDED) {
            returnList.addFiles(POMFileBuilder(actualProjectInformation).build())
            returnList.addFiles(SettingsXMLBuilder(actualProjectInformation.controllers, actualProjectInformation.mainDependency).build())
            returnList.addFiles(JavaLaunchBuilder().build())
            returnList.addFiles(MavenProcBuilder().build())
        }
        else {
            returnList.addFiles(GradleSettingsBuilder(CommonTokens.ServerApi).build())
            returnList.addFiles(JettyGradleProcBuilder(CommonTokens.ServerApi).build())
            returnList.addFiles(JettyGradleBuilder(projectInformation).build())

            val buildProperties = HashMap<String, String>()
            buildProperties[JavaUtilities.JerseyMediaName] = JavaUtilities.JerseyMediaVersion
            buildProperties[JavaUtilities.JettyServerName] = JavaUtilities.JettyServerVersion
            buildProperties[JavaUtilities.KotlinName] = JavaUtilities.KotlinVersion
            buildProperties[JavaUtilities.ServerPortName] = projectInformation.mainDependency.serverPort.toString()
            buildProperties[JavaUtilities.YaclibVersionName] = projectInformation.mainDependency.yaclibVersion
            buildProperties[JavaUtilities.HttpComponentsName] = JavaUtilities.HttpComponentsVersion

            returnList.addFiles(PropertiesBuilder(projectInformation.controllers, projectInformation.mainDependency, projectInformation.thirdPartyDependenciesList, buildProperties).build())
            returnList.addFiles(JettyMainBuilder(projectInformation).build())
            returnList.addFiles(CustomSetupBuilder().build())
        }

        returnList.addFiles(GulpFileBuilder(actualProjectInformation.controllers).build())
        returnList.addFiles(NPMBlobBuilder(actualProjectInformation).build())
        returnList.addFiles(NPMPackageBuilder(actualProjectInformation).build())
        returnList.addFiles(HttpExecuteImplementationBuilder(actualProjectInformation.mainDependency).build())
        returnList.addFiles(FurtherAngularSetupBuilder().build())
        returnList.addFiles(IndexHTMLBuilder().build())
        returnList.addFiles(WiringFileBuilder(actualProjectInformation.controllers).build())

        returnList.addFiles(JavaServletXMLBuilder(actualProjectInformation.mainDependency).build())
        returnList.addFiles(Java8Base64ServiceBuilder(actualProjectInformation.mainDependency).build())

        returnList.addFiles(KotlinIServiceLocatorBuilder(actualProjectInformation.controllers, actualProjectInformation.mainDependency).build())
        returnList.addFiles(KotlinServiceLocatorBuilder(actualProjectInformation.controllers, actualProjectInformation.mainDependency).build())

        actualProjectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(JavaRestBuilder(controller, controllerDependency.dependency, actualProjectInformation.mainDependency).build())
            }
        }

        return returnList.build()
    }
}