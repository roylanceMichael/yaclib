package org.roylance.yaclib.services.java

import org.junit.Assert
import org.junit.Test
import org.naru.park.ParkController
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService

class JavaClientProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(ParkController.getDescriptor())
        val javaServiceLanguageProcess = JavaClientProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setMinorVersion(14)
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()

        // act
        val item = javaServiceLanguageProcess.buildInterface(all, dependency)

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}