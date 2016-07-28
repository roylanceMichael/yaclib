package org.roylance.yaclib.services

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService

class FilePersistServiceTest {
//    @Test
    fun simplePassThroughTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaServerProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setVersion("0.14-SNAPSHOT")
                .build()

        val allFiles = javaServiceLanguageProcess.buildInterface(controllers, dependency)

        // act
        filePersistService.persistFiles("/home/mroylance/park/sapi", allFiles)

        // assert
        Assert.assertTrue(true)
    }

//    @Test
    fun simplePassThroughClientTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.Controllers.getDescriptor())
        val javaServiceLanguageProcess = JavaClientProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setVersion("0.14-SNAPSHOT")
                .build()

        val allFiles = javaServiceLanguageProcess.buildInterface(controllers, dependency)

        // act
        filePersistService.persistFiles("/home/mroylance/park/capi", allFiles)

        // assert
        Assert.assertTrue(true)
    }

//    @Test
    fun simplePassThroughTypeScriptClientTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.Controllers.getDescriptor())
        val javaServiceLanguageProcess = TypeScriptProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("@mroylance/park")
                .setName("models")
                .setVersion("0.0.14")
                .setTypescriptModelFile("./model")
                .build()

        val allFiles = javaServiceLanguageProcess.buildInterface(controllers, dependency)

        // act
        filePersistService.persistFiles("/home/mroylance/park/api/javascript", allFiles)

        // assert
        Assert.assertTrue(true)
    }
}