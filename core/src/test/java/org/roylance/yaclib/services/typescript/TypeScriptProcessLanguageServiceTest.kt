package org.roylance.yaclib.services.typescript

import org.junit.Assert
import org.junit.Test
import org.naru.park.ParkController
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class TypeScriptProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(ParkController.getDescriptor())
        val processLanguageService = TypeScriptProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("@mroylance/park")
                .setName("models")
                .setMinorVersion("14")
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