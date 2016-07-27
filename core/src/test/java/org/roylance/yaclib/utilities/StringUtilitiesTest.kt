package org.roylance.yaclib.utilities

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.TestControllers
import org.roylance.yaclib.TestModels
import org.roylance.yaclib.core.utilities.StringUtilities

class StringUtilitiesTest {
    @Test
    fun simpleTest() {
        // arrange

        // act
        val testModelsResult = StringUtilities.convertProtoFileNameToJavaClassName(TestModels.getDescriptor())
        val testControllersResult = StringUtilities.convertProtoFileNameToJavaClassName(TestControllers.getDescriptor())

        // assert
        Assert.assertTrue(TestModels::class.java.simpleName.equals(testModelsResult))
        Assert.assertTrue(TestControllers::class.java.simpleName.equals(testControllersResult))
    }
}