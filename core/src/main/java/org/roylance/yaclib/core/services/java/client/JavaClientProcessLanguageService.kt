package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.java.server.KotlinServiceBuilder

class JavaClientProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: Models.AllControllers,
                                overallPackage: String,
                                groupName: String,
                                dependency: Models.Dependency): Models.AllFiles {
        val returnList = Models.AllFiles.newBuilder()

        returnList.addFiles(GradleFileBuilder(overallPackage, dependency).build())
        returnList.addFiles(GradleSettingsBuilder(groupName).build())
        controllers.controllersList.forEach { controller ->
            returnList.addFiles(JavaRetrofitBuilder(controller, overallPackage).build())
            returnList.addFiles(KotlinServiceBuilder(controller, overallPackage).build())
            returnList.addFiles(KotlinServiceImplementationBuilder(controller, overallPackage).build())
        }

        return returnList.build()
    }
}