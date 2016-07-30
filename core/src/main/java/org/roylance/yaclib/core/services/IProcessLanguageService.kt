package org.roylance.yaclib.core.services

import org.roylance.yaclib.YaclibModel
import java.util.*

interface IProcessLanguageService {
    fun buildInterface(controllerDependencies: YaclibModel.AllControllerDependencies,
                       mainDependency: YaclibModel.Dependency,
                       thirdPartyDependencies: MutableList<YaclibModel.Dependency> = ArrayList()): YaclibModel.AllFiles
}