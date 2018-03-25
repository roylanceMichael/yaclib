package org.roylance.yaclib;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.plugins.InitLogic;
import org.roylance.yaclib.core.utilities.FileProcessUtilities;
import org.roylance.yaclib.core.utilities.InitUtilities;

import java.io.File;

public class YaclibInitTask extends DefaultTask {
  public YaclibModel.Dependency dependency;
  public YaclibModel.Dependency yaclibDependency;

  public String initialLocation;

  @Override public String getGroup() {
    return "yaclib";
  }

  @TaskAction public Boolean buildProject() {
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
    System.out.println(
        "xcodebuild: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.XCodeBuild));
    System.out.println(
        "carthage: " + FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.Carthage));
    System.out.println(
        "protoc-gen-swift: " + FileProcessUtilities.INSTANCE.getActualLocation("protoc-gen-swift"));
    System.out.println("is not windows: " + InitUtilities.INSTANCE.isNotWindows());

    if (!InitUtilities.INSTANCE.hasMinimumRequired()) {
      System.out.println(InitUtilities.MinimumRequiredErrorMessage);
      return false;
    }

    if (initialLocation == null || initialLocation.length() == 0) {
      System.out.println("initial location required");
      return false;
    }

    final File actualInitialLocation = new File(initialLocation);
    if (!actualInitialLocation.isDirectory()) {
      System.out.println("initial location must be a directory, or not exist so we can create");
    }

    if (!actualInitialLocation.exists()) {
      actualInitialLocation.mkdirs();
    }

    return new InitLogic(initialLocation, dependency, yaclibDependency).build();
  }
}
