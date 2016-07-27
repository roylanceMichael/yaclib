package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinServiceLocatorBuilder(
        private val controllers: Models.AllControllers,
        private val overallPackageName: String): IBuilder<Models.File> {

    override fun build(): Models.File {
        val allControllerNames = this.controllers.controllersList.map { controller ->
            """    override val ${StringUtilities.convertServiceNameToVariableName(controller)}: $overallPackageName.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)}
        get() = throw UnsupportedOperationException()
""" }.joinToString("\n")

        val template = """package $overallPackageName.${CommonTokens.UtilitiesName}

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
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(this.overallPackageName, CommonTokens.UtilitiesName))

        return returnFile.build()
    }
}