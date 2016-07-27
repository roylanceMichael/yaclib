package org.roylance.yaclib.services

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService

class FilePersistServiceTest {
    @Test
    fun simplePassThroughTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaServerProcessLanguageService()

        val dependency = Models.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setVersion("0.14-SNAPSHOT")
                .build()

        val allFiles = javaServiceLanguageProcess.buildInterface(controllers, "com.roylance.cooltest", "com.roylance", dependency)

        // act
        filePersistService.persistFiles("/home/mroylance/test_persist", allFiles)

        // assert
        Assert.assertTrue(true)
    }
}