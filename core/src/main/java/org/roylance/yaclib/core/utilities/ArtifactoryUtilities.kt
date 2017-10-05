package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel

object ArtifactoryUtilities {
  const val UploadScriptName = "upload.sh"
  const val UploadWheelScriptName = "uploadWheel.sh"

  fun buildUploadWheelScript(directory: String, dependency: YaclibModel.Dependency): String {
    val actualUrl = JavaUtilities.buildRepositoryUrl(dependency.pipRepository)
    val wheelName = PythonUtilities.buildWheelFileName(dependency)

    val initialTemplate = """#!/usr/bin/env bash
if [ -z "$${JavaUtilities.ArtifactoryUserName}" ]; then
    ${InitUtilities.Curl} -u -X PUT "$actualUrl/${PythonUtilities.buildWheelUrl(
        dependency)}" -T $directory/$wheelName
else
    ${InitUtilities.Curl} -u $${JavaUtilities.ArtifactoryUserName}:$${JavaUtilities.ArtifactoryPasswordName} -X PUT "$actualUrl/${PythonUtilities.buildWheelUrl(
        dependency)}" -T $directory/$wheelName
fi
"""
    return initialTemplate
  }

  fun buildUploadTarGzScript(directory: String,
      dependency: YaclibModel.Dependency): String {
    val actualUrl = JavaUtilities.buildRepositoryUrl(dependency.npmRepository)
    val tarFileName = buildTarFileName(dependency)

    val initialTemplate = """#!/usr/bin/env bash
if [ -z "$${JavaUtilities.ArtifactoryUserName}" ]; then
    ${InitUtilities.Curl} -u -X PUT "$actualUrl/${buildTarUrl(
        dependency)}" -T $directory/$tarFileName
else
    ${InitUtilities.Curl} -u $${JavaUtilities.ArtifactoryUserName}:$${JavaUtilities.ArtifactoryPasswordName} -X PUT "$actualUrl/${buildTarUrl(
        dependency)}" -T $directory/$tarFileName
fi
"""
    return initialTemplate
  }

  fun buildTarFileName(dependency: YaclibModel.Dependency): String {
    return "${dependency.group}.${dependency.name}.${dependency.majorVersion}.${dependency.minorVersion}.tar.gz"
  }

  fun buildTarUrl(dependency: YaclibModel.Dependency): String {
    return buildUrlName(dependency) + ".tar.gz"
  }

  fun replacePeriodWithForwardSlash(item: String): String {
    val newGroup = item.replace(".", "/")
    return newGroup
  }

  private fun buildUrlName(dependency: YaclibModel.Dependency): String {
    val newGroup = replacePeriodWithForwardSlash(dependency.group)
    return "$newGroup/${dependency.name}/${dependency.majorVersion}.${dependency.minorVersion}"
  }


}