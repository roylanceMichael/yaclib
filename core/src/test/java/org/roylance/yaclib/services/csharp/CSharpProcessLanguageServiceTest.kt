package org.roylance.yaclib.services.csharp

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.csharp.CSharpProcessLanguageService
import java.util.*

class CSharpProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(Controllers.getDescriptor())
        val processLanguageService = CSharpProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("park")
                .setName("models")
                .setVersion(14)
                .setNodeAliasName("@mroylance")
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder()
                .addControllerDependencies(controllerDependencies)
                .build()

        // act
        val item = processLanguageService.buildInterface(all, dependency, ArrayList())

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}