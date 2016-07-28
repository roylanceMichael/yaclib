package org.roylance.yaclib.services

import org.junit.Assert
import org.junit.Test
import org.naru.park.Controllers
import org.roylance.yaclib.core.services.ProcessFileDescriptorService

class ProcessFileDescriptorServiceTest {
    @Test
    fun simplePassThroughTest() {
        // arrange
        val service = ProcessFileDescriptorService()

        // act
        val controllers = service.processFile(Controllers.getDescriptor())

        // assert
        System.out.println(controllers)
        Assert.assertTrue(true)
    }
}