package org.roylance.yaclib.services.java

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService

class JavaServerProcessLanguageServiceTest {
    @Test
    fun simplePassThroughTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaServerProcessLanguageService()

        val dependency = Models.Dependency.newBuilder()
            .setGroup("org.naru.park")
            .setName("api")
            .setVersion("0.14-SNAPSHOT")
            .build()

        // act
        val item = javaServiceLanguageProcess.buildInterface(controllers, "org.naru.park", "org.naru.park", dependency)

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}