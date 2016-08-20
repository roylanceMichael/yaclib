package org.roylance.yaclib.utilities

import com.google.protobuf.Descriptors
import org.junit.Assert
import org.junit.Test

class ReflectionTest {
    @Test
    fun simpleRunThrough() {
        // arrange
        val yaclibModel = Class.forName("org.roylance.yaclib.YaclibModel")

        // act
        // assert
        System.out.println(yaclibModel)
        val getDescriptor = yaclibModel.getMethod("getDescriptor")
        System.out.println(getDescriptor)

        val fileDescriptor = getDescriptor.invoke(yaclibModel) as Descriptors.FileDescriptor
        System.out.println(fileDescriptor.name)
        Assert.assertTrue(true)
    }
}