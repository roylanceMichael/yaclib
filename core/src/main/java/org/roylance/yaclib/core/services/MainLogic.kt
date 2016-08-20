package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import java.nio.file.Paths

class MainLogic(
        private val typeScriptModelFile: String,
        private val nodeAliasName: String?,
        private val version: Int,
        private val location: String,
        private val mainModel: Descriptors.FileDescriptor,
        private val mainController: Descriptors.FileDescriptor,
        private val dependencyDescriptors: List<DependencyDescriptor>,
        private val thirdPartyServerDependencies: List<YaclibModel.Dependency>): IBuilder<Boolean> {
    override fun build(): Boolean {
        val mainDependency = YaclibModel.Dependency.newBuilder()
            .setName(CommonTokens.ApiName)
            .setVersion(this.version)
            .setTypescriptModelFile(this.typeScriptModelFile)
            .setGroup(this.mainModel.`package`)

        if (this.nodeAliasName != null) {
            mainDependency.nodeAliasName = this.nodeAliasName
        }

        val mainDependencyDescriptor = DependencyDescriptor(mainDependency.build(), this.mainController)

        val filePersistService = FilePersistService()
        val processFileDescriptorService = ProcessFileDescriptorService()

        val serverControllerDependencies = YaclibModel.AllControllerDependencies.newBuilder()
        val clientControllerDependencies = YaclibModel.AllControllerDependencies.newBuilder()

        val mainController = processFileDescriptorService.processFile(mainDependencyDescriptor.descriptor)
        val mainControllerDependencies = YaclibModel.ControllerDependency.newBuilder().setControllers(mainController)
                .setDependency(mainDependencyDescriptor.dependency)

        serverControllerDependencies.addControllerDependencies(mainControllerDependencies)
        clientControllerDependencies.addControllerDependencies(mainControllerDependencies)

        this.dependencyDescriptors.forEach {
            val controller = processFileDescriptorService.processFile(it.descriptor)
            val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setControllers(controller)
                    .setDependency(it.dependency)

            serverControllerDependencies.addControllerDependencies(controllerDependencies)
        }

        val serverFiles = JavaServerProcessLanguageService().buildInterface(serverControllerDependencies.build(),
                mainDependencyDescriptor.dependency,
                this.thirdPartyServerDependencies.toMutableList())
        filePersistService.persistFiles(Paths.get(this.location, CommonTokens.ServerApi).toString(), serverFiles)

        val javaClientFiles = JavaClientProcessLanguageService().buildInterface(clientControllerDependencies.build(), mainDependencyDescriptor.dependency)
        filePersistService.persistFiles(Paths.get(this.location, CommonTokens.ClientApi).toString(), javaClientFiles)

        val typeScriptFiles = TypeScriptProcessLanguageService().buildInterface(clientControllerDependencies.build(), mainDependencyDescriptor.dependency)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.JavaScriptName).toString(), typeScriptFiles)

        return true
    }
}