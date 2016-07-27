package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IProcessLanguageService

class TypeScriptProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: Models.AllControllers,
                                dependency: Models.Dependency): Models.AllFiles {
        val returnList = Models.AllFiles.newBuilder()

        returnList.addFiles(HttpExecuteServiceBuilder().build())
        controllers.controllersList.forEach { controller ->
            returnList.addFiles(TypeScriptServiceBuilder(controller).build())
            returnList.addFiles(TypeScriptServiceImplementationBuilder(controller, dependency).build())
        }

        return returnList.build()
    }
}