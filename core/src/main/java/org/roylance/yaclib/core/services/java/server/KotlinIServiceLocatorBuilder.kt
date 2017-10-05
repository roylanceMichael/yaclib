package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinIServiceLocatorBuilder(
    private val controllerDependencies: YaclibModel.AllControllerDependencies,
    private val mainDependency: YaclibModel.Dependency) : IBuilder<YaclibModel.File> {

  private val initialTemplate = """${CommonTokens.DoNotAlterMessage}
package ${mainDependency.group}.${CommonTokens.UtilitiesName}

interface ${CommonTokens.ServiceLocatorName} {
    val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
${this.buildAllControllerServices()}
}
"""

  override fun build(): YaclibModel.File {
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(initialTemplate)
        .setFileName(CommonTokens.ServiceLocatorName)
        .setFileExtension(YaclibModel.FileExtension.KT_EXT)
        .setFullDirectoryLocation(
            StringUtilities.convertPackageToJavaFolderStructureServices(mainDependency.group,
                CommonTokens.UtilitiesName))

    return returnFile.build()
  }

  private fun buildAllControllerServices(): String {
    val workspace = StringBuilder()

    this.controllerDependencies.controllerDependenciesList.forEach { controllerDependencies ->
      controllerDependencies.controllers.controllersList.forEach { controller ->
        val item = """    val ${StringUtilities.convertServiceNameToVariableName(
            controller)}: ${controllerDependencies.dependency.group}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(
            controller)}
"""
        workspace.append(item)
      }
    }

    return workspace.toString()
  }
}