package org.roylance.yaclib.core.plugins

import com.google.protobuf.Descriptors
import org.apache.commons.io.FileUtils
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.plugins.client.CSharpBuilder
import org.roylance.yaclib.core.plugins.client.JavaClientBuilder
import org.roylance.yaclib.core.plugins.client.PythonBuilder
import org.roylance.yaclib.core.plugins.client.TypeScriptBuilder
import org.roylance.yaclib.core.plugins.server.JavaServerBuilder
import org.roylance.yaclib.core.plugins.server.TypeScriptClientServerBuilder
import org.roylance.yaclib.core.services.IProjectBuilderServices
import org.roylance.yaclib.core.utilities.*
import java.nio.file.Paths
import java.util.*

class PluginLogic(
        val location: String,
        val mainDependency: YaclibModel.Dependency,
        val mainController: Descriptors.FileDescriptor,
        val dependencyDescriptors: List<DependencyDescriptor>,
        val thirdPartyServerDependencies: List<YaclibModel.Dependency>,
        val nugetKey: String?,
        auxiliaryProjects: YaclibModel.AuxiliaryProjects): IBuilder<Boolean> {

    private val auxiliaryProjectsMap = HashMap<String, YaclibModel.AuxiliaryProject.Builder>()
    private val dependencyMap = HashMap<String, YaclibModel.Dependency>()

    private val roylanceCommonDependency = YaclibModel.Dependency.newBuilder()
            .setGroup("yaclib.roylance")
            .setName("common")
            .setThirdPartyDependencyVersion(JavaUtilities.RoylanceCommonVersion)
            .build()

    private val kotlinDependency = YaclibModel.Dependency.newBuilder()
            .setGroup("yaclib")
            .setName("kotlin")
            .setThirdPartyDependencyVersion(JavaUtilities.KotlinVersion)
            .build()

    init {
        auxiliaryProjects.projectsList.forEach {
            dependencyMap[JavaUtilities.buildFullPackageName(it.targetDependency)] = it.targetDependency
            it.fromDependenciesList.forEach {
                dependencyMap[JavaUtilities.buildFullPackageName(it)] = it
            }
            auxiliaryProjectsMap[JavaUtilities.buildFullPackageName(it.targetDependency)] = it.toBuilder()
        }

        thirdPartyServerDependencies.forEach {
            val packageName= JavaUtilities.buildFullPackageName(it)
            dependencyMap[packageName] = it
        }
    }

    override fun build(): Boolean {
        auxiliaryProjectsMap.values.forEach { project ->
            println("updating ${project.targetDependency.group}.${project.targetDependency.name} with roylance.common (${JavaUtilities.RoylanceCommonVersion}) and kotlin (${JavaUtilities.KotlinVersion})")

            val actualLocation = Paths.get(location, project.targetDependency.name).toString()
            GradleUtilities.updateDependencyVersion(actualLocation, roylanceCommonDependency)
            GradleUtilities.updateDependencyVersion(actualLocation, kotlinDependency)
        }
        processPhase(YaclibModel.ExecutionPhase.DELETE_DIRECTORIES)

        // delete phase
        println("deleting ${Paths.get(location, CommonTokens.JavaScriptName).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.JavaScriptName).toFile())
        println("deleting ${Paths.get(location, CommonTokens.ClientApi).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.ClientApi).toFile())
        println("deleting ${Paths.get(location, CommonTokens.CSharpName)}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.CSharpName).toFile())
        println("deleting ${Paths.get(location, CommonTokens.PythonName)}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.PythonName).toFile())

        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.GENERATE_CODE_FROM_PROTOBUFS.name))
        processPhase(YaclibModel.ExecutionPhase.GENERATE_CODE_FROM_PROTOBUFS)
        ClientLogic(
                location,
                mainDependency,
                mainController,
                dependencyDescriptors,
                thirdPartyServerDependencies.map { dependencyMap[JavaUtilities.buildFullPackageName(it)]!! }).build()

        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PUBLISH_CSHARP.name))
        processPhase(YaclibModel.ExecutionPhase.BUILD_PUBLISH_CSHARP)
        CSharpBuilder(location, mainDependency, nugetKey).build()
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PUBLISH_PYTHON.name))
        processPhase(YaclibModel.ExecutionPhase.BUILD_PUBLISH_PYTHON)
        PythonBuilder(location, mainDependency).build()

        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PUBLISH_JAVA_CLIENT.name))
        processPhase(YaclibModel.ExecutionPhase.BUILD_PUBLISH_JAVA_CLIENT)

        JavaClientBuilder(location, mainDependency).build()
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PUBLISH_TYPESCRIPT.name))
        processPhase(YaclibModel.ExecutionPhase.BUILD_PUBLISH_TYPESCRIPT)
        TypeScriptBuilder(location, mainDependency).build()

        // process server now
        ServerLogic(
                location,
                mainDependency,
                mainController,
                dependencyDescriptors,
                thirdPartyServerDependencies.map { dependencyMap[JavaUtilities.buildFullPackageName(it)]!! }).build()

        // java server has dependencies on both java client and typescript client
        processPhase(YaclibModel.ExecutionPhase.BUILD_TYPESCRIPT_SERVER)
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_TYPESCRIPT_SERVER.name))
        TypeScriptClientServerBuilder(location).build()

        processPhase(YaclibModel.ExecutionPhase.BUILD_PACKAGE_JAVA_SERVER)
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PACKAGE_JAVA_SERVER.name))
        JavaServerBuilder(location, mainDependency.serverType).build()

        return true
    }

    private fun processPhase(phase: YaclibModel.ExecutionPhase) {
        auxiliaryProjectsMap.keys.filter {
            auxiliaryProjectsMap[it]!!.handleBefore == phase
        }.forEach {
            processAuxiliaryProject(it)
        }
    }

    private fun processAuxiliaryProject(projectKey: String) {
        val actualProject = auxiliaryProjectsMap[projectKey]!!
        if (!projectBuilderServices.containsKey(actualProject.projectType)) {
            return
        }

        val projectService = projectBuilderServices[actualProject.projectType]!!
        val executions = actualProject.executionsList

        executions.forEach { execution ->
            if (executionTypes.containsKey(execution)) {
                val referencedProject = auxiliaryProjectsMap[projectKey]!!
                executionTypes[execution]!!(referencedProject, projectService)
            }
        }
    }

    private fun printReportToConsole(report: YaclibModel.ProcessReport) {
        println(report.normalOutput)
        println(report.errorOutput)
    }

    private val projectBuilderServices = object: HashMap<YaclibModel.ProjectType, IProjectBuilderServices>() {
        init {
            this[YaclibModel.ProjectType.GRADLE_PROJECT_TYPE] = GradleUtilities
            this[YaclibModel.ProjectType.MAVEN_PROJECT_TYPE] = MavenUtilities
            this[YaclibModel.ProjectType.NPM_PROJECT_TYPE] = TypeScriptUtilities
            this[YaclibModel.ProjectType.DOTNET_PROJECT_TYPE] = CSharpUtilities
            this[YaclibModel.ProjectType.PIP_PROJECT_TYPE] = PythonUtilities
        }
    }

    private val executionTypes = object: HashMap<YaclibModel.CustomExecutionType, (project: YaclibModel.AuxiliaryProject.Builder, service: IProjectBuilderServices) -> Unit>() {
        init {
            this[YaclibModel.CustomExecutionType.CUSTOM_BUILD] = { project, service ->
                val projectLocation = Paths.get(location, project.targetDependency.name)

                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_BUILD.name + " " + JavaUtilities.buildFullPackageName(project.targetDependency)))
                printReportToConsole(service.build(projectLocation.toString()))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_PACKAGE] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                val projectLocation = Paths.get(location, project.targetDependency.name)

                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_PACKAGE.name + " " + packageName))
                printReportToConsole(service.buildPackage(projectLocation.toString(), actualTargetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_PUBLISH] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                val projectLocation = Paths.get(location, project.targetDependency.name)

                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_PUBLISH.name + " " + packageName))
                printReportToConsole(service.publish(projectLocation.toString(), actualTargetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_INCREMENT_VERSION] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_INCREMENT_VERSION.name + " " + packageName))

                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                val report = service.incrementVersion(projectLocation, actualTargetDependency)
                printReportToConsole(report)

                project.targetDependencyBuilder
                    .setMajorVersion(report.newMajor)
                    .setMinorVersion(report.newMinor)

                dependencyMap[JavaUtilities.buildFullPackageName(project.targetDependency)] = project.targetDependency
                auxiliaryProjectsMap[JavaUtilities.buildFullPackageName(project.targetDependency)] = project
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_UPDATE_DEPENDENCIES] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_UPDATE_DEPENDENCIES.name + " " + packageName))
                val projectLocation = Paths.get(location, actualTargetDependency.name).toString()
                project.fromDependenciesList.forEach { fromDependency ->
                    val actualFromDependency = dependencyMap[JavaUtilities.buildFullPackageName(fromDependency)]!!
                    printReportToConsole(service.updateDependencyVersion(projectLocation, actualFromDependency))
                }
                project.toDependenciesList.forEach { toDependency ->
                    val toLocation = Paths.get(location, toDependency.name).toString()
                    printReportToConsole(service.updateDependencyVersion(toLocation, actualTargetDependency))
                }
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_SET_VERSION] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_SET_VERSION.name + " " + packageName))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                printReportToConsole(service.setVersion(projectLocation, actualTargetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_CLEAN] = { project, service ->
                val packageName = JavaUtilities.buildFullPackageName(project.targetDependency)
                val actualTargetDependency = dependencyMap[packageName]!!
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_CLEAN.name + " " + packageName))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                printReportToConsole(service.setVersion(projectLocation, actualTargetDependency))
            }
        }
    }
}