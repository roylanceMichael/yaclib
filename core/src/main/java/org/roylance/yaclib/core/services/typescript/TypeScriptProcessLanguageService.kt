package org.roylance.yaclib.core.services.typescript

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProcessLanguageService

class TypeScriptProcessLanguageService: IProcessLanguageService {
    override fun buildInterface(controllers: YaclibModel.AllControllers,
                                dependency: YaclibModel.Dependency): YaclibModel.AllFiles {
        val returnList = YaclibModel.AllFiles.newBuilder()

        returnList.addFiles(HttpExecuteServiceBuilder().build())
        controllers.controllersList.forEach { controller ->
            returnList.addFiles(TypeScriptServiceBuilder(controller).build())
            returnList.addFiles(TypeScriptServiceImplementationBuilder(controller, dependency).build())
        }

        return returnList.build()
    }
}