package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.csharp.CSharpProcessLanguageService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import java.nio.file.Paths

class MainLogic(
        private val location: String,
        private val mainDependency: YaclibModel.Dependency,
        private val mainController: Descriptors.FileDescriptor,
        private val dependencyDescriptors: List<DependencyDescriptor>,
        private val thirdPartyServerDependencies: List<YaclibModel.Dependency>,
        private val processCSharp: Boolean): IBuilder<Boolean> {
    override fun build(): Boolean {
        val mainDependencyDescriptor = DependencyDescriptor(mainDependency, this.mainController)

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

        if (this.processCSharp) {
            val csharpFiles = CSharpProcessLanguageService().buildInterface(clientControllerDependencies.build(), mainDependencyDescriptor.dependency)
            filePersistService.persistFiles(Paths.get(location, CommonTokens.CSharpName).toString(), csharpFiles)
        }

        return true
    }
}