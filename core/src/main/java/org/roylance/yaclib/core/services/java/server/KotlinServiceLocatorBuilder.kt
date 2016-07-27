package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceLocatorBuilder(
        private val controllers: Models.AllControllers,
        private val dependency: Models.Dependency): IBuilder<Models.File> {

    override fun build(): Models.File {
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
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(template)
                .setFileName(CommonTokens.ServiceLocatorSingletonName)
                .setFileExtension(Models.FileExtension.KT_EXT)
                .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group,
                        CommonTokens.UtilitiesName))

        return returnFile.build()
    }
}