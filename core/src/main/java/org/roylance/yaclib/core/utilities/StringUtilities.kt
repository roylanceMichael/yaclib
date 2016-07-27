package org.roylance.yaclib.core.utilities

import com.google.protobuf.Descriptors
import org.roylance.yaclib.Models
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
            if (it.length == 0) {
                return@forEach
            }
            if (it.length > 0) {
                workspace.append(it[0].toUpperCase())
            }
            if (it.length > 1) {
                workspace.append(it.substring(1))
            }
        }

        return workspace.toString()
    }

    fun convertServiceNameToInterfaceName(controller: Models.Controller): String {
        return "I${controller.name[0].toUpperCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }

    fun convertServiceNameToVariableName(controller: Models.Controller): String {
        return "${controller.name[0].toLowerCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }

    fun convertServiceNameToJavaCall(controller: Models.Controller): String {
        return "get${controller.name[0].toUpperCase()}${controller.name.substring(1)}${CommonTokens.ServiceName}"
    }
}