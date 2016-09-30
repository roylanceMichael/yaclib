package org.roylance.yaclib;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.models.DependencyDescriptor;
import org.roylance.yaclib.core.services.PluginLogic;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class YaclibPlugin extends DefaultTask {
    public String typeScriptModelFile;
    public String nodeAliasName;
    public Integer majorVersion;
    public Integer minorVersion;
    public String location;
    public YaclibModel.RepositoryType repositoryType;
    public String mainModel;
    public String mainController;
    public List<DependencyDescriptor> dependencyDescriptors;
    public List<YaclibModel.Dependency> thirdPartyServerDependencies;
    public String nugetKey;
    public String githubRepo;
    public String repoUrl;
    public String repoName;
    public String repoUser;
    public String license;
    public String author;

    @TaskAction
    public Boolean buildDefinitions() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!nullChecker(typeScriptModelFile, "typeScriptModelFile")) {
            return false;
        }
        if (!nullChecker(majorVersion, "majorVersion")) {
            return false;
        }
        if (!nullChecker(minorVersion, "minorVersion")) {
            return false;
        }
        if (!nullChecker(location, "location")) {
            return false;
        }
        if (!nullChecker(mainModel, "mainModel")) {
            return false;
        }
        if (!nullChecker(mainController, "mainController")) {
            return false;
        }
        if (!nullChecker(dependencyDescriptors, "dependencyDescriptors")) {
            return false;
        }
        if (!nullChecker(thirdPartyServerDependencies, "thirdPartyServerDependencies")) {
            return false;
        }
        if (!nullChecker(githubRepo, "githubRepo")) {
            return false;
        }
        if (!nullChecker(repoUrl, "repoUrl")) {
            return false;
        }
        if (!nullChecker(repoUser, "repoUser")) {
            return false;
        }
        if (!nullChecker(license, "license")) {
            return false;
        }
        if (!nullChecker(author, "author")) {
            return false;
        }
        if (!nullChecker(repoName, "repoName")) {
            return false;
        }

        return new PluginLogic(
                this.typeScriptModelFile,
                this.nodeAliasName,
                majorVersion,
                minorVersion,
                this.location,
                this.repositoryType,
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
                this.author).build();
    }

    private static boolean nullChecker(Object item, String name) {
        if (item == null) {
            System.out.println(name + " is null");
            return false;
        }
        return true;
    }
}
