package org.roylance.yaclib.core.services

import org.roylance.yaclib.YaclibModel

interface IProcessLanguageService {
    fun buildInterface(controllers: YaclibModel.AllControllers,
                       dependency: YaclibModel.Dependency): YaclibModel.AllFiles
}