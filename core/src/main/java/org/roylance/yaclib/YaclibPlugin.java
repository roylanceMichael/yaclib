package org.roylance.yaclib;

import com.google.protobuf.Descriptors;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.enums.CommonTokens;
import org.roylance.yaclib.core.models.DependencyDescriptor;
import org.roylance.yaclib.core.plugins.PluginLogic;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class YaclibPlugin extends DefaultTask {
    public YaclibModel.Dependency mainDependency;
    public String location;
    public String mainModel;
    public String mainController;
    public List<DependencyDescriptor> dependencyDescriptors;
    public List<YaclibModel.Dependency> thirdPartyServerDependencies;
    public String nugetKey;
    public YaclibModel.AuxiliaryProjects auxiliaryProjects;

    @TaskAction
    public Boolean buildDefinitions() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
        final YaclibModel.AuxiliaryProjects.Builder projectsToUse = YaclibModel.AuxiliaryProjects.newBuilder();
        if (auxiliaryProjects != null) {
            projectsToUse.addAllProjects(auxiliaryProjects.getProjectsList());
        }

        final Descriptors.FileDescriptor controllerDescriptor = DependencyDescriptor.buildFileDescriptor(mainController);

        System.out.println("executing plugin now!");
        return new PluginLogic(
                location,
                mainDependency.toBuilder()
                        .setName(CommonTokens.ApiName)
                        .setGroup(controllerDescriptor.getPackage())
                        .build(),
                controllerDescriptor,
                dependencyDescriptors,
                thirdPartyServerDependencies,
                nugetKey,
                projectsToUse.build()).build();
    }

    private static boolean nullChecker(Object item, String name) {
        if (item == null) {
            System.out.println(name + " is null");
            return false;
        }
        return true;
    }
}
