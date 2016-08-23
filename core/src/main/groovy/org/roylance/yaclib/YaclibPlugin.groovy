package org.roylance.yaclib


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.PluginLogic

class YaclibPlugin extends DefaultTask {
    def String typeScriptModelFile
    def String nodeAliasName
    def int version
    def String location
    def String mainModel
    def String mainController
    def ArrayList<DependencyDescriptor> dependencyDescriptors
    def ArrayList<YaclibModel.Dependency> thirdPartyServerDependencies
    def String nugetKey

    @TaskAction
    def buildDefinitions() {
        println("executing yaclib...")
        if (!nullChecker(typeScriptModelFile, "typeScriptModelFile")) {
            return false
        }
        if (!nullChecker(version, "version")) {
            return false
        }
        if (!nullChecker(location, "location")) {
            return false
        }
        if (!nullChecker(mainModel, "mainModel")) {
            return false
        }
        if (!nullChecker(mainController, "mainController")) {
            return false
        }
        if (!nullChecker(dependencyDescriptors, "dependencyDescriptors")) {
            return false
        }
        if (!nullChecker(thirdPartyServerDependencies, "thirdPartyServerDependencies")) {
            return false
        }

        return new PluginLogic(
                this.typeScriptModelFile,
                this.nodeAliasName,
                version,
                this.location,
                DependencyDescriptor.buildFileDescriptor(this.mainModel),
                DependencyDescriptor.buildFileDescriptor(this.mainController),
                this.dependencyDescriptors,
                this.thirdPartyServerDependencies,
                this.nugetKey
        ).build()
    }

    private static def nullChecker(Object item, String name) {
        if (item == null) {
            println(name + " is null")
            return false
        }
        return true
    }
}
