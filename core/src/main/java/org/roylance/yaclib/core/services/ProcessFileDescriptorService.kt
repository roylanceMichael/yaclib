package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities

class ProcessFileDescriptorService: IProcessFileDescriptorService {
    override fun processFile(fileDescriptor: Descriptors.FileDescriptor): YaclibModel.AllControllers {
        val returnItem = YaclibModel.AllControllers.newBuilder()

        fileDescriptor.messageTypes.forEach { messageType ->
            if (messageType.name.endsWith(CommonTokens.ControllerSuffix)) {
                // process
                val foundController = this.processControllerMessageType(messageType) ?: return@forEach
                returnItem.addControllers(foundController)
            }
        }

        return returnItem.build()
    }

    private fun processControllerMessageType(messageType: Descriptors.Descriptor): YaclibModel.Controller? {
        val controllerName = messageType.name.substringBefore(CommonTokens.ControllerSuffix)
        val returnController = YaclibModel.Controller.newBuilder().setName(controllerName)

        messageType.fields.filter { field ->
                    CommonTokens.ProtoMessageType.equals(field.type.name) &&
                            field.messageType.name.endsWith(CommonTokens.ActionSuffix)
        }.forEach { actionField ->
            val foundAction = this.processActionMessageType(actionField.messageType, actionField.name) ?: return@forEach
            returnController.addActions(foundAction)
        }

        if (returnController.actionsCount > 0) {
            return returnController.build()
        }
        return null
    }

    private fun processActionMessageType(messageType: Descriptors.Descriptor, fieldName: String): YaclibModel.Action? {
        val foundInputOutputTypes = messageType.fields.filter { field ->
            CommonTokens.ProtoMessageType.equals(field.type.name)
        }.sortedBy { it.number }

        if (foundInputOutputTypes.size == 0) {
            return null
        }

        val returnItem = YaclibModel.Action.newBuilder().setName(fieldName)
        val returnType = foundInputOutputTypes[foundInputOutputTypes.size - 1]

        val outputMessage = YaclibModel.Message.newBuilder()
            .setArgumentName(returnType.name)
            .setFilePackage(returnType.messageType.file.`package`)
            .setFileClass(StringUtilities.convertProtoFileNameToJavaClassName(returnType.messageType.file))
            .setMessagePackage(returnType.messageType.file.`package`)
            .setMessageClass(returnType.messageType.name)

        returnItem.output = outputMessage.build()

        foundInputOutputTypes.filter { !it.number.equals(returnType.number) }
            .forEach {
                val inputMessage = YaclibModel.Message.newBuilder()
                    .setArgumentName(it.name)
                    .setFilePackage(it.messageType.file.`package`)
                    .setFileClass(StringUtilities.convertProtoFileNameToJavaClassName(it.messageType.file))
                    .setMessagePackage(it.messageType.file.`package`)
                    .setMessageClass(it.messageType.name)

                returnItem.addInputs(inputMessage)
            }

        return returnItem.build()
    }
}