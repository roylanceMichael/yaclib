package org.roylance.yaclib;

import com.google.protobuf.Descriptors;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.models.DependencyDescriptor;
import org.roylance.yaclib.core.plugins.PluginLogic;
import org.roylance.yaclib.core.utilities.FileProcessUtilities;
import org.roylance.yaclib.core.utilities.InitUtilities;
import org.roylance.yaclib.core.utilities.JavaUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class YaclibPluginTask extends DefaultTask {
  public YaclibModel.Dependency mainDependency;
  public String location;
  public String mainModel;
  public String mainController;
  public List<DependencyDescriptor> dependencyDescriptors;
  public List<YaclibModel.Dependency> thirdPartyServerDependencies;
  public String nugetKey;
  public YaclibModel.AuxiliaryProjects auxiliaryProjects;
  public boolean processSwift;

  @Override public String getGroup() {
    return "yaclib";
  }

  @TaskAction public Boolean buildDefinitions()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException {

    System.out.println(
        "standard maven username: " + System.getenv(JavaUtilities.StandardMavenUserName));
    System.out.println("artifactory username: " + System.getenv(JavaUtilities.ArtifactoryUserName));
    System.out.println("artifactory bintray: " + System.getenv(JavaUtilities.BintrayUserName));

    System.out.println(
        "gradle: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Gradle));
    System.out.println(
        "maven: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Maven));
    System.out.println(
        "npm: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.NPM));
    System.out.println("tsc: " + FileProcessUtilities.INSTANCE.getActualLocation(
        InitUtilities.TypeScriptCompiler));
    System.out.println(
        "python: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Python));
    System.out.println(
        "protoc: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Protoc));
    System.out.println(
        "nuget: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Nuget));
    System.out.println(
        "dotnet: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.DotNet));
    System.out.println("is not windows: " + InitUtilities.INSTANCE.isNotWindows());

    if (!InitUtilities.INSTANCE.hasMinimumRequired()) {
      System.out.println(InitUtilities.MinimumRequiredErrorMessage);
      return false;
    }

    if (nullChecker(location, "location")) {
      return false;
    }
    if (nullChecker(mainModel, "mainModel")) {
      return false;
    }
    if (nullChecker(mainController, "mainController")) {
      return false;
    }
    if (nullChecker(dependencyDescriptors, "dependencyDescriptors")) {
      return false;
    }
    if (nullChecker(thirdPartyServerDependencies, "thirdPartyServerDependencies")) {
      return false;
    }
    final YaclibModel.AuxiliaryProjects.Builder projectsToUse =
        YaclibModel.AuxiliaryProjects.newBuilder();
    if (auxiliaryProjects != null) {
      projectsToUse.addAllProjects(auxiliaryProjects.getProjectsList());
    }

    final Descriptors.FileDescriptor controllerDescriptor =
        DependencyDescriptor.buildFileDescriptor(mainController);

    System.out.println("executing plugin now!");
    return new PluginLogic(location, mainDependency.toBuilder()
        .setName(mainDependency.getName())
        .setGroup(mainDependency.getGroup())
        .build(), controllerDescriptor, dependencyDescriptors, thirdPartyServerDependencies,
        nugetKey, projectsToUse.build(), processSwift).build();
  }

  private static boolean nullChecker(Object item, String name) {
    if (item == null) {
      System.out.println(name + " is null");
      return true;
    }
    return false;
  }
}
