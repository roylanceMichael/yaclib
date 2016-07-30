package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class JavaServerProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                                mainDependency: YaclibModel.Dependency,
                                thirdPartyDependencies: MutableList<YaclibModel.Dependency>): YaclibModel.AllFiles {
        if (thirdPartyDependencies.size == 0) {
            thirdPartyDependencies.addAll(TypeScriptUtilities.baseServerTypeScriptKit)
        }

        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(POMFileBuilder(controllerDependencies, mainDependency, thirdPartyDependencies).build())
        returnList.addFiles(GulpFileBuilder(controllerDependencies).build())
        returnList.addFiles(NPMPackageBuilder(controllerDependencies, mainDependency, thirdPartyDependencies).build())
        returnList.addFiles(HttpExecuteImplementationBuilder(mainDependency).build())
        returnList.addFiles(FurtherAngularSetupBuilder().build())
        returnList.addFiles(IndexHTMLBuilder().build())
        returnList.addFiles(WiringFileBuilder(controllerDependencies).build())
        returnList.addFiles(JavaLaunchBuilder().build())
        returnList.addFiles(JavaServletXMLBuilder().build())
        returnList.addFiles(Java8Base64ServiceBuilder(mainDependency).build())

        returnList.addFiles(KotlinIServiceLocatorBuilder(controllerDependencies, mainDependency).build())
        returnList.addFiles(KotlinServiceLocatorBuilder(controllerDependencies, mainDependency).build())

        controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
            controllerDependency.controllers.controllersList.forEach { controller ->
                returnList.addFiles(KotlinServiceBuilder(controller, mainDependency).build())
                returnList.addFiles(JavaRestBuilder(controller, controllerDependency.dependency, mainDependency).build())
            }
        }

        return returnList.build()
    }
}