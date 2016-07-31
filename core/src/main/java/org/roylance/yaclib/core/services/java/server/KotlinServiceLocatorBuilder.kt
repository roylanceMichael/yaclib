package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceLocatorBuilder(
        private val controllerDependencies: YaclibModel.AllControllerDependencies,
        private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val initialTemplate = """package ${mainDependency.group}.${CommonTokens.UtilitiesName}

object ${CommonTokens.ServiceLocatorSingletonName}: ${CommonTokens.ServiceLocatorName} {
    override val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
        get() = org.roylance.common.service.ProtoSerializerService(${mainDependency.group}.${CommonTokens.ServicesName}.Base64Service())
${this.buildAllControllerServices()}
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate)
                .setFileName(CommonTokens.ServiceLocatorSingletonName)
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(mainDependency.group,
                        CommonTokens.UtilitiesName))

        return returnFile.build()
    }

    private fun buildAllControllerServices():String {
        val workspace = StringBuilder()

        this.controllerDependencies.controllerDependenciesList.forEach { controllerDependencies ->
            controllerDependencies.controllers.controllersList.forEach { controller ->
                val item = """    override val ${StringUtilities.convertServiceNameToVariableName(controller)}: ${controllerDependencies.dependency.group}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)}
        get() = throw UnsupportedOperationException()
"""
                workspace.append(item)
            }
        }

        return workspace.toString()
    }
}