package org.roylance.yaclib.services.python

import org.junit.Assert
import org.junit.Test
import org.naru.park.ParkController
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.python.PythonProcessLanguageService
import java.util.*

class PythonProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(ParkController.getDescriptor())
        val processLanguageService = PythonProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("park")
                .setName("models")
                .setMinorVersion(14)
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

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}