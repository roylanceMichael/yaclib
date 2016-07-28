package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class KotlinIServiceLocatorBuilder(
        private val controllers: YaclibModel.AllControllers,
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    override fun build(): YaclibModel.File {
        val allControllerNames = this.controllers.controllersList.map { controller ->
            "\tval ${StringUtilities.convertServiceNameToVariableName(controller)}: ${dependency.group}.${CommonTokens.ServicesName}.${StringUtilities.convertServiceNameToInterfaceName(controller)}"
        }.joinToString("\n")

        val template = """package ${dependency.group}.${CommonTokens.UtilitiesName}

interface ${CommonTokens.ServiceLocatorName} {
    val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
$allControllerNames
}
"""

        val returnFile = YaclibModel.File.newBuilder()
            .setFileToWrite(template)
            .setFileName(CommonTokens.ServiceLocatorName)
            .setFileExtension(YaclibModel.FileExtension.KT_EXT)
            .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(dependency.group, CommonTokens.UtilitiesName))

        return returnFile.build()
    }
}