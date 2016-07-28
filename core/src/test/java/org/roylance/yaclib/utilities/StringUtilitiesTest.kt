package org.roylance.yaclib.utilities

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.TestController
import org.roylance.yaclib.TestModel
import org.roylance.yaclib.core.utilities.StringUtilities

class StringUtilitiesTest {
    @Test
    fun simpleTest() {
        // arrange

        // act
        val testModelsResult = StringUtilities.convertProtoFileNameToJavaClassName(TestModel.getDescriptor())
        val testControllersResult = StringUtilities.convertProtoFileNameToJavaClassName(TestController.getDescriptor())

        // assert
        Assert.assertTrue(TestModel::class.java.simpleName.equals(testModelsResult))
        Assert.assertTrue(TestController::class.java.simpleName.equals(testControllersResult))
    }
}