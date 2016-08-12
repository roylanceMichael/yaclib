package org.roylance.yaclib


import com.google.protobuf.Descriptors
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.PluginLogic

class YaclibPlugin extends DefaultTask {
    def String typeScriptModelFile
    def String nodeAliasName
    def int version
    def String location
    def Descriptors.FileDescriptor mainDescriptor
    def ArrayList<DependencyDescriptor> dependencyDescriptors
    def ArrayList<YaclibModel.Dependency> thirdPartyServerDependencies

    @TaskAction
    def buildDefinitions() {
        if (!nullChecker(typeScriptModelFile, "typeScriptModelFile")) {
            return false
        }
        if (!nullChecker(version, "version")) {
            return false
        }
        if (!nullChecker(location, "location")) {
            return false
        }
        if (!nullChecker(mainDescriptor, "mainDescriptor")) {
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
                this.mainDescriptor,
                this.dependencyDescriptors,
                this.thirdPartyServerDependencies
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
