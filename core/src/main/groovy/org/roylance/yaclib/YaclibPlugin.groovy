package org.roylance.yaclib


import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.services.PluginLogic

class YaclibPlugin extends DefaultTask {
    public def String typeScriptModelFile
    public def String nodeAliasName
    public def int majorVersion
    public def int minorVersion
    public def String location
    public def String mainModel
    public def String mainController
    public def ArrayList<DependencyDescriptor> dependencyDescriptors
    public def ArrayList<YaclibModel.Dependency> thirdPartyServerDependencies
    public def String nugetKey
    public def String githubRepo
    public def String repoUrl
    public def String repoUser
    public def String repoName
    public def String license
    public def String author

    @TaskAction
    def buildDefinitions() {
        println("executing yaclib...")
        if (!nullChecker(typeScriptModelFile, "typeScriptModelFile")) {
            return false
        }
        if (!nullChecker(majorVersion, "majorVersion")) {
            return false
        }
        if (!nullChecker(minorVersion, "minorVersion")) {
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
        if (!nullChecker(githubRepo, "githubRepo")) {
            return false
        }
        if (!nullChecker(repoUrl, "repoUrl")) {
            return false
        }
        if (!nullChecker(repoUser, "repoUser")) {
            return false
        }
        if (!nullChecker(license, "license")) {
            return false
        }
        if (!nullChecker(author, "author")) {
            return false
        }
        if (!nullChecker(repoName, "repoName")) {
            return false
        }

        return new PluginLogic(
                this.typeScriptModelFile,
                this.nodeAliasName,
                majorVersion,
                minorVersion,
                this.location,
                DependencyDescriptor.buildFileDescriptor(this.mainModel),
                DependencyDescriptor.buildFileDescriptor(this.mainController),
                this.dependencyDescriptors,
                this.thirdPartyServerDependencies,
                this.nugetKey,
                this.githubRepo,
                this.repoUrl,
                this.repoName,
                this.repoUser,
                this.license,
                this.author
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
