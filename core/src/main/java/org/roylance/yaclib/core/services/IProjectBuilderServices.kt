package org.roylance.yaclib.core.services

import org.roylance.yaclib.YaclibModel

interface IProjectBuilderServices {
    fun getVersion(location: String): YaclibModel.ProcessReport
    fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport
    fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport
    fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport
    fun clean(location: String): YaclibModel.ProcessReport
    fun build(location: String): YaclibModel.ProcessReport
    fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport
    fun buildPublish(location: String, dependency: YaclibModel.Dependency, apiKey: String = ""): YaclibModel.ProcessReport
    fun restoreDependencies(location: String): YaclibModel.ProcessReport
}