package org.roylance.yaclib.core.plugins

import com.google.protobuf.Descriptors
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.csharp.CSharpProcessLanguageService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.python.PythonProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import java.nio.file.Paths

class ClientLogic(
        private val location: String,
        private val mainDependency: YaclibModel.Dependency,
        private val mainController: Descriptors.FileDescriptor,
        private val dependencyDescriptors: List<DependencyDescriptor>,
        private val thirdPartyServerDependencies: List<YaclibModel.Dependency>): IBuilder<Boolean> {
    override fun build(): Boolean {
        val mainDependencyDescriptor = DependencyDescriptor(mainDependency, this.mainController)

        val filePersistService = FilePersistService()
        val processFileDescriptorService = ProcessFileDescriptorService()

        val serverControllerDependencies = YaclibModel.AllControllerDependencies.newBuilder()

        val mainController = processFileDescriptorService.processFile(mainDependencyDescriptor.descriptor)
        val mainControllerDependencies = YaclibModel.ControllerDependency.newBuilder()
                .setControllers(mainController)
                .setDependency(mainDependencyDescriptor.dependency)

        serverControllerDependencies.addControllerDependencies(mainControllerDependencies)

        this.dependencyDescriptors.forEach {
            val controller = processFileDescriptorService.processFile(it.descriptor)
            val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setControllers(controller)
                    .setDependency(it.dependency)
            serverControllerDependencies.addControllerDependencies(controllerDependencies)
        }

        val projectInformation = YaclibModel.ProjectInformation.newBuilder()
            .addAllThirdPartyDependencies(thirdPartyServerDependencies)
            .setMainDependency(mainDependencyDescriptor.dependency)
            .setControllers(serverControllerDependencies.build())
            .build()

        val javaClientFiles = JavaClientProcessLanguageService().buildInterface(projectInformation)
        filePersistService.persistFiles(Paths.get(this.location, CommonTokens.ClientApi).toString(), javaClientFiles)

        val typeScriptFiles = TypeScriptProcessLanguageService().buildInterface(projectInformation)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.JavaScriptName).toString(), typeScriptFiles)

        val csharpFiles = CSharpProcessLanguageService().buildInterface(projectInformation)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.CSharpName).toString(), csharpFiles)

        val pythonFiles = PythonProcessLanguageService().buildInterface(projectInformation)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.PythonName).toString(), pythonFiles)

        return true
    }
}