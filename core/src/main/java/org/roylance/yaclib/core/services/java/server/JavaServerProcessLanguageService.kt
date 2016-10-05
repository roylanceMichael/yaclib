package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.common.NPMBlobBuilder
import org.roylance.yaclib.core.services.common.NPMPackageBuilder
import org.roylance.yaclib.core.services.java.common.PropertiesBuilder
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class JavaServerProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        val actualProjectInformation = projectInformation.toBuilder()
                .setIsServer(true)
                .addAllThirdPartyDependencies(TypeScriptUtilities.baseServerTypeScriptKit)
                .build()

        returnList.addFiles(POMFileBuilder(actualProjectInformation).build())
        returnList.addFiles(SettingsXMLBuilder(actualProjectInformation.controllers, actualProjectInformation.mainDependency).build())
        returnList.addFiles(GulpFileBuilder(actualProjectInformation.controllers).build())
        returnList.addFiles(NPMBlobBuilder(actualProjectInformation).build())
        returnList.addFiles(NPMPackageBuilder(actualProjectInformation).build())
        returnList.addFiles(HttpExecuteImplementationBuilder(actualProjectInformation.mainDependency).build())
        returnList.addFiles(FurtherAngularSetupBuilder().build())
        returnList.addFiles(IndexHTMLBuilder().build())
        returnList.addFiles(WiringFileBuilder(actualProjectInformation.controllers).build())
        returnList.addFiles(JavaLaunchBuilder().build())
        returnList.addFiles(JavaServletXMLBuilder(actualProjectInformation.mainDependency).build())
        returnList.addFiles(Java8Base64ServiceBuilder(actualProjectInformation.mainDependency).build())
        returnList.addFiles(ProcBuilder().build())

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