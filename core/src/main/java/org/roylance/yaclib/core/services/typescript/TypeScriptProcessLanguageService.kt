package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IProcessLanguageService

class TypeScriptProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: Models.AllControllers,
                                overallPackage: String,
                                groupName: String,
                                dependency: Models.Dependency): Models.AllFiles {
        val returnList = Models.AllFiles.newBuilder()

        returnList.addFiles(PackageBuilder(overallPackage).build())
        returnList.addFiles(HttpExecuteServiceBuilder(overallPackage).build())
        controllers.controllersList.forEach { controller ->
            returnList.addFiles(TypeScriptServiceBuilder(controller, overallPackage).build())
            returnList.addFiles(TypeScriptServiceImplementationBuilder(controller, overallPackage).build())
        }

        return returnList.build()
    }
}