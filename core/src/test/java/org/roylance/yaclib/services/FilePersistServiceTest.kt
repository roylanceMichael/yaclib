package org.roylance.yaclib.services

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.csharp.CSharpProcessLanguageService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.services.python.PythonProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import org.roylance.yaclib.core.utilities.CSharpUtilities

class FilePersistServiceTest {
//    @Test
    fun simplePassThroughTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.ParkController.getDescriptor())
        val javaServiceLanguageProcess = JavaServerProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setMinorVersion("14")
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()

        val allFiles = javaServiceLanguageProcess.buildInterface(projectInformation)

        // act
        filePersistService.persistFiles("/home/mroylance/park/sapi", allFiles)

        // assert
        Assert.assertTrue(true)
    }

//    @Test
    fun simplePassThroughPythonTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.ParkController.getDescriptor())
        val javaServiceLanguageProcess = PythonProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setMinorVersion("14")
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()

        val allFiles = javaServiceLanguageProcess.buildInterface(projectInformation)

        // act
        filePersistService.persistFiles("/Users/mikeroylance/park/python", allFiles)

        // assert
        Assert.assertTrue(true)
    }

//    @Test
    fun simplePassThroughClientTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.ParkController.getDescriptor())
        val javaServiceLanguageProcess = JavaClientProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("org.naru.park")
                .setName("api")
                .setMinorVersion("14")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
            .setControllers(controllers)
            .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()

        val allFiles = javaServiceLanguageProcess.buildInterface(projectInformation)

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
        val controllers = service.processFile(org.naru.park.ParkController.getDescriptor())
        val javaServiceLanguageProcess = TypeScriptProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("@mroylance/park")
                .setName("models")
                .setMinorVersion("14")
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency)
            .setControllers(controllers)
            .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()

        val allFiles = javaServiceLanguageProcess.buildInterface(projectInformation)

        // act
        filePersistService.persistFiles("/home/mroylance/park/api/javascript", allFiles)

        // assert
        Assert.assertTrue(true)
    }

//    @Test
    fun simplePassThroughCSharpClientTest() {
        // arrange
        val filePersistService = FilePersistService()
        val service = ProcessFileDescriptorService()
        val controllers = service.processFile(org.naru.park.ParkController.getDescriptor())
        val javaServiceLanguageProcess = CSharpProcessLanguageService()

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup("park")
                .setName("models")
                .setMinorVersion("14")
                .setNpmRepository(YaclibModel.Repository.newBuilder().setRepositoryType(YaclibModel.RepositoryType.PRIVATE_NPM).setNpmScope("@mroylance"))
                .setTypescriptModelFile("ParkModel")
                .build()

        val controllerDependencies = YaclibModel.ControllerDependency.newBuilder()
                .setDependency(dependency)
                .setControllers(controllers)
                .build()

        val all = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependencies).build()
        val projectInformation = YaclibModel.ProjectInformation.newBuilder().setControllers(all).setMainDependency(dependency).build()

        val allFiles = javaServiceLanguageProcess.buildInterface(projectInformation)

        // act
        filePersistService.persistFiles("/Users/mikeroylance/park/csharp", allFiles)

        CSharpUtilities.buildProtobufs("/Users/mikeroylance/park", dependency)

        // assert
        Assert.assertTrue(true)
    }
}