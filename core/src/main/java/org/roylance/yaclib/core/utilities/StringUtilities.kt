package org.roylance.yaclib.core.utilities

import com.google.protobuf.Descriptors
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens

object StringUtilities {
    fun convertPackageToJavaFolderStructureServices(packageName:String, type: String): String {
        val workspace = StringBuilder("src/main/java/")
        val splitItems = packageName.split(".")
        splitItems.forEach {
            workspace.append("$it/")
        }
        workspace.append(type)

        return workspace.toString()
    }

    fun convertProtoFileNameToJavaClassName(fileDescriptor: Descriptors.FileDescriptor): String {
        val workspace = StringBuilder()
        val noPeriodProtoFileName = fileDescriptor.fullName.substringBefore('.')
        val splitByUnderScore = noPeriodProtoFileName.split("_")

        splitByUnderScore.forEach {
            if (it.isEmpty()) {
                return@forEach
            }
            if (it.isNotEmpty()) {
                workspace.append(it[0].toUpperCase())
            }
            if (it.length > 1) {
                workspace.append(it.substring(1))
            }
        }

        return workspace.toString()
    }

    fun convertServiceNameToInterfaceName(controller: YaclibModel.Controller): String {
        return "I${controller.name[0].toUpperCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }

    fun convertServiceNameToVariableName(controller: YaclibModel.Controller): String {
        return "${controller.name[0].toLowerCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }

    fun convertServiceNameToJavaCall(controller: YaclibModel.Controller): String {
        return "get${controller.name[0].toUpperCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }

    fun buildUrl(url:String):String {
        return url.replace("_", "-").toLowerCase()
    }

    fun convertToPascalCase(name: String): String {
        val splitItems = name.split(Period)

        return splitItems.map {
            if (it.length > 1) {
                "${it[0].toUpperCase()}${it.substring(1)}"
            }
            else {
                it.toUpperCase()
            }
        }.joinToString(Period)
    }

    private const val Period = "."
}