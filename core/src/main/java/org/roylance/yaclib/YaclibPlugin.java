package org.roylance.yaclib;

import com.google.protobuf.Descriptors;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.enums.CommonTokens;
import org.roylance.yaclib.core.models.DependencyDescriptor;
import org.roylance.yaclib.core.services.PluginLogic;

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

        final Descriptors.FileDescriptor controllerDescriptor = DependencyDescriptor.buildFileDescriptor(this.mainController);

        return new PluginLogic(
                this.location,
                mainDependency.toBuilder()
                        .setName(CommonTokens.ApiName)
                        .setGroup(controllerDescriptor.getPackage())
                        .build(),
                controllerDescriptor,
                this.dependencyDescriptors,
                this.thirdPartyServerDependencies,
                this.nugetKey).build();
    }

    private static boolean nullChecker(Object item, String name) {
        if (item == null) {
            System.out.println(name + " is null");
            return false;
        }
        return true;
    }
}
