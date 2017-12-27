package org.roylance.yaclib.utilities

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.plugins.client.CSharpBuilder
import org.roylance.yaclib.core.utilities.FileProcessUtilities

class FileProcessUtilitiesTest {
    @Test
    fun simpleRunThrough() {
        // arrange
        val protocLocation = FileProcessUtilities.getActualLocation("protoc")
        // act
        // assert
        println(protocLocation)
        val report = FileProcessUtilities.executeProcess("~", "echo", "\$PATH")
        println(report.normalOutput)
//        assert(nugetLocation.length > 0)
        Assert.assertTrue(report.normalOutput.isNotEmpty())
    }

//    @Test
    fun runCSharpProtoc() {
        // arrange
        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("com.artemis.leto")
                .setName("api")
                .setAuthorName("roylance.michael@gmail.com")
                .setLicense("MIT")
                .setGithubRepo("")
                .setMajorVersion("0")
                .setMinorVersion("255")
                .setTypescriptModelFile("LetoModel")
                .setMavenRepository(YaclibModel.Repository.newBuilder()
                        .setName("ajwklefawef")
                        .setUrl("wefawefa")
                        .setRepositoryType(YaclibModel.RepositoryType.ARTIFACTORY).build())
                .setNpmRepository(YaclibModel.Repository.newBuilder()
                        .setName("ajwklefawef")
                        .setUrl("wefawefa")
                        .setRepositoryType(YaclibModel.RepositoryType.ARTIFACTORY_NPM).build())
                .setPipRepository(YaclibModel.Repository.newBuilder()
                        .setRepositoryType(YaclibModel.RepositoryType.ARTIFACTORY_PYTHON)
                        .setName("ajwklefawef")
                        .setUrl("wefawefa")
                        .build())
                .build()

        val csharpBuilder = CSharpBuilder("/Users/mikeroylance/src/github.com/artemishealth/leto/v2/",
                dependency,
                null)

        // act
        csharpBuilder.build()

        // assert
        assert(true)
    }
}