package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceLocatorBuilder(
        private val controllers: YaclibModel.AllControllers,
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    override fun build(): YaclibModel.File {
        val allControllerNames = this.controllers.controllersList.map { controller ->
            """    override val ${StringUtilities.convertServiceNameToVariableName(controller)}: ${dependency.group}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)}
        get() = throw UnsupportedOperationException()
""" }.joinToString("\n")

        val template = """package ${dependency.group}.${CommonTokens.UtilitiesName}

object ${CommonTokens.ServiceLocatorSingletonName}: ${CommonTokens.ServiceLocatorName} {
    override val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
        get() = throw UnsupportedOperationException()
$allControllerNames
}
"""
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(template)
                .setFileName(CommonTokens.ServiceLocatorSingletonName)
                .setFileExtension(YaclibModel.FileExtension.KT_EXT)
                .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group,
                        CommonTokens.UtilitiesName))

        return returnFile.build()
    }
}