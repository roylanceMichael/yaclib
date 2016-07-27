package org.roylance.yaclib.services.typescript

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService

class TypeScriptProcessLanguageServiceTest {
    @Test
    fun simpleTest() {
        // arrange
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(Controllers.getDescriptor())
        val processLanguageService = TypeScriptProcessLanguageService()

        val dependency = Models.Dependency.newBuilder()
                .setGroup("@mroylance/park")
                .setName("models")
                .setVersion("0.0.14")
                .build()

        // act
        val item = processLanguageService.buildInterface(controllers, dependency)

        // assert
        item.filesList.forEach {
            System.out.println(it.fileToWrite)
            System.out.println("----------------")
        }
        Assert.assertTrue(true)
    }
}