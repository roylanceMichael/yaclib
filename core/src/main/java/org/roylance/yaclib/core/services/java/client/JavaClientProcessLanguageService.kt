package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IProcessLanguageService
import org.roylance.yaclib.core.services.java.server.KotlinServiceBuilder

class JavaClientProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: Models.AllControllers,
                                dependency: Models.Dependency): Models.AllFiles {
        val returnList = Models.AllFiles.newBuilder()

        returnList.addFiles(GradleFileBuilder(dependency).build())
        returnList.addFiles(GradlewFileBuilder().build())
        returnList.addFiles(GradlewBatFileBuilder().build())
        returnList.addFiles(GradleSettingsBuilder().build())
        controllers.controllersList.forEach { controller ->
            returnList.addFiles(JavaRetrofitBuilder(controller, dependency).build())
            returnList.addFiles(KotlinServiceBuilder(controller, dependency).build())
            returnList.addFiles(KotlinServiceImplementationBuilder(controller, dependency).build())
        }

        return returnList.build()
    }
}