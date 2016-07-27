package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IProcessLanguageService

class JavaServerProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: Models.AllControllers, overallPackage: String, groupName: String, dependency: Models.Dependency):Models.AllFiles {
        val returnList = Models.AllFiles.newBuilder()

        returnList.addFiles(POMFileBuilder(groupName, overallPackage, dependency).build())
        returnList.addFiles(KotlinIServiceLocatorBuilder(controllers, overallPackage).build())
        returnList.addFiles(KotlinServiceLocatorBuilder(controllers, overallPackage).build())

        controllers.controllersList.forEach { controller ->
            returnList.addFiles(KotlinServiceBuilder(controller, overallPackage).build())
            returnList.addFiles(JavaRestBuilder(controller, overallPackage).build())
        }

        return returnList.build()
    }
}