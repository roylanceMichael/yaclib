package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinIServiceLocatorBuilder(
        private val controllers: Models.AllControllers,
        private val overallPackageName: String): IBuilder<Models.File> {

    override fun build(): Models.File {
        val allControllerNames = this.controllers.controllersList.map { controller ->
            "\tval ${StringUtilities.convertServiceNameToVariableName(controller)}: ${overallPackageName}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)}"
        }.joinToString("\n")

        val template = """package $overallPackageName.${CommonTokens.UtilitiesName}

interface ${CommonTokens.ServiceLocatorName} {
    val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
$allControllerNames
}
"""

        val returnFile = Models.File.newBuilder()
            .setFileToWrite(template)
            .setFileName(CommonTokens.ServiceLocatorName)
            .setFileExtension(Models.FileExtension.KT_EXT)
            .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(this.overallPackageName, CommonTokens.UtilitiesName))

        return returnFile.build()
    }
}