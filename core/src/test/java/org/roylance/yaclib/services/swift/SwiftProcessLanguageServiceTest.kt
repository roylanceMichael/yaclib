package org.roylance.yaclib.services.swift

import org.junit.Assert
import org.junit.Test
import org.naru.park.ParkController
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.plugins.client.SwiftBuilder
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.swift.SwiftProcessLanguageService

class SwiftProcessLanguageServiceTest {
    // integration test
    //    @Test
    fun simpleTest() {
        // arrange
        val testLocation = "<sample location of yaclib project>"
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(ParkController.getDescriptor())
        val processLanguageService = SwiftProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("park")
                .setName("models")
                .setMinorVersion("14")
                .setAuthorName("Mike Roylance")
                .setNpmRepository(YaclibModel.Repository.newBuilder().setRepositoryType(YaclibModel.RepositoryType.PRIVATE_NPM).setNpmScope("@mroylance"))
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder()
                .addControllerDependencies(controllerDependencies)
                .build()

        // act
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()
        val item = processLanguageService.buildInterface(projectInformation)

        val filePersistService = FilePersistService()
        filePersistService.persistFiles(testLocation, item)

        SwiftBuilder(testLocation, projectInformation, true).build()
        // assert
//        item.filesList.forEach {
//            System.out.println(it.fileToWrite)
//            System.out.println("----------------")
//        }
        Assert.assertTrue(true)
    }
}