package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService

class JavaServerProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: YaclibModel.AllControllers,
                                dependency: YaclibModel.Dependency):YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(IndexHTMLBuilder().build())
        returnList.addFiles(JavaLaunchBuilder().build())
        returnList.addFiles(JavaServletXMLBuilder().build())
        returnList.addFiles(Java8Base64ServiceBuilder(dependency).build())
        returnList.addFiles(POMFileBuilder(dependency).build())
        returnList.addFiles(KotlinIServiceLocatorBuilder(controllers, dependency).build())
        returnList.addFiles(KotlinServiceLocatorBuilder(controllers, dependency).build())

        controllers.controllersList.forEach { controller ->
            returnList.addFiles(KotlinServiceBuilder(controller, dependency).build())
            returnList.addFiles(JavaRestBuilder(controller, dependency).build())
        }

        return returnList.build()
    }
}