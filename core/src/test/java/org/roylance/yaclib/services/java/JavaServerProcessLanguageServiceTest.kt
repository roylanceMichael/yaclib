package org.roylance.yaclib.services.java

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.utilities.TypeScriptUtilities

class JavaServerProcessLanguageServiceTest {
    @Test
    fun simplePassThroughTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaServerProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
            .setGroup("org.naru.park")
            .setName("api")
            .setVersion(14)
            .setTypescriptModelFile("NaruPark")
            .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()

        // act
        val item = javaServiceLanguageProcess.buildInterface(all, dependency, TypeScriptUtilities.baseServerTypeScriptKit)

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}