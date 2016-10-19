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

    init {
        auxiliaryProjects.projectsList.forEach {
            auxiliaryProjectsMap[JavaUtilities.buildFullPackageName(it.targetDependency)] = it.toBuilder()
        }
    }

    override fun build(): Boolean {
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
        MainLogic(
                location,
                mainDependency,
                mainController,
                dependencyDescriptors,
                thirdPartyServerDependencies).build()

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

        // java server has dependencies on both java client and typescript client
        processPhase(YaclibModel.ExecutionPhase.BUILD_TYPESCRIPT_SERVER)
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_TYPESCRIPT_SERVER.name))
        TypeScriptClientServerBuilder(location).build()

        processPhase(YaclibModel.ExecutionPhase.BUILD_PACKAGE_JAVA_SERVER)
        println(InitUtilities.buildPhaseMessage(YaclibModel.ExecutionPhase.BUILD_PACKAGE_JAVA_SERVER.name))
        JavaServerBuilder(location).build()

        return true
    }

    private fun processPhase(phase: YaclibModel.ExecutionPhase) {
        auxiliaryProjectsMap.values.forEach { project ->
            if (project.handleBefore == phase) {
                processAuxiliaryProject(project)
            }
        }
    }

    private fun processAuxiliaryProject(project: YaclibModel.AuxiliaryProject.Builder) {
        if (!projectBuilderServices.containsKey(project.projectType)) {
            return
        }

        val projectService = projectBuilderServices[project.projectType]!!
        project.executionsList.forEach { execution ->
            if (executionTypes.containsKey(execution)) {
                executionTypes[execution]!!(project, projectService)
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
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_BUILD.name))
                printReportToConsole(service.build(projectLocation.toString()))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_PACKAGE] = { project, service ->
                val projectLocation = Paths.get(location, project.targetDependency.name)
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_PACKAGE.name))
                printReportToConsole(service.buildPackage(projectLocation.toString(), project.targetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_PUBLISH] = { project, service ->
                val projectLocation = Paths.get(location, project.targetDependency.name)
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_PUBLISH.name))
                printReportToConsole(service.publish(projectLocation.toString(), project.targetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_INCREMENT_VERSION] = { project, service ->
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_INCREMENT_VERSION.name))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                val report = service.incrementVersion(projectLocation, project.targetDependency)
                printReportToConsole(report)

                project.targetDependencyBuilder
                    .setMajorVersion(report.newMajor)
                    .setMinorVersion(report.newMinor)
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_UPDATE_DEPENDENCIES] = { project, service ->
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_UPDATE_DEPENDENCIES.name))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                project.fromDependenciesList.forEach { fromDependency ->
                    printReportToConsole(service.updateDependencyVersion(projectLocation, fromDependency))
                }
                project.toDependenciesList.forEach { toDependency ->
                    val toLocation = Paths.get(location, toDependency.name).toString()
                    printReportToConsole(service.updateDependencyVersion(toLocation, project.targetDependency))
                }
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_SET_VERSION] = { project, service ->
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_SET_VERSION.name))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                printReportToConsole(service.setVersion(projectLocation, project.targetDependency))
            }
            this[YaclibModel.CustomExecutionType.CUSTOM_CLEAN] = { project, service ->
                println(InitUtilities.buildPhaseMessage(YaclibModel.CustomExecutionType.CUSTOM_CLEAN.name))
                val projectLocation = Paths.get(location, project.targetDependency.name).toString()
                printReportToConsole(service.setVersion(projectLocation, project.targetDependency))
            }
        }
    }
}