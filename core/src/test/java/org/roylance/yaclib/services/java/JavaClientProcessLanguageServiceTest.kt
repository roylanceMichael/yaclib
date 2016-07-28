package org.roylance.yaclib.services.java

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService

class JavaClientProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaClientProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setVersion("0.14-SNAPSHOT")
                .build()

        // act
        val item = javaServiceLanguageProcess.buildInterface(controllers, dependency)

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}