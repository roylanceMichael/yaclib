package org.roylance.yaclib;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.roylance.yaclib.core.enums.CommonStringsHelper;
import org.roylance.yaclib.core.plugins.debian.*;
import org.roylance.yaclib.core.plugins.server.TypeScriptClientServerBuilder;
import org.roylance.yaclib.core.utilities.FileProcessUtilities;
import org.roylance.yaclib.core.utilities.InitUtilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YaclibPackageTask extends DefaultTask {
  public String appName;
  public String serverVersion;
  public String maintainerInfo;
  public int serverPort;
  public List<String> includeFiles;

  @Override public String getGroup() {
    return "yaclib";
  }

  @TaskAction public Boolean packageApp() throws IOException, InterruptedException {
    if (appName == null) {
      System.out.println("appName is null");
      return false;
    }
    if (serverVersion == null) {
      System.out.println("serverVersion is null");
      return false;
    }
    if (maintainerInfo == null) {
      System.out.println("maintainerInfo is null");
      return false;
    }
    final String debianPackageLocation =
        FileProcessUtilities.INSTANCE.getActualLocation(InitUtilities.DpkgDeb);
    if (debianPackageLocation.length() == 0) {
      System.out.println("unable to create debian file, please install " + InitUtilities.DpkgDeb);
      return false;
    }

    final String currentDirectory = System.getProperty("user.dir");

    FileUtils.copyFile(new File("custom.sh"), new File("build/install/" + appName + "/custom.sh"));
    FileUtils.copyFile(new File("proc.sh"), new File("build/install/" + appName + "/proc.sh"));

    if (includeFiles != null) {
      for (final String file : includeFiles) {
        final File fileToCopy = new File(file);
        FileUtils.copyFile(fileToCopy,
            new File("build/install/" + appName + "/" + fileToCopy.getName()));
      }
    }

    // this will build the JavaScript UI
    new TypeScriptClientServerBuilder(currentDirectory, appName).build();

    // copy /src/main/webapp into /build/install/appname
    FileUtils.copyDirectory(new File("src/main/webapp"),
        new File("build/install/" + appName + "/webapp"));

    // create debian structure
    buildDebianStructure();

    new RunBuilder(appName,
        CommonStringsHelper.INSTANCE.buildProjectRunScriptLocation(appName)).build();
    new RunBuilder(appName,
        CommonStringsHelper.INSTANCE.buildSystemRunScriptLocation(appName)).build();

    new StopBuilder(serverPort,
        CommonStringsHelper.INSTANCE.buildProjectStopScriptLocation(appName)).build();
    new StopBuilder(serverPort, CommonStringsHelper.INSTANCE.buildSystemStopScriptLocation(appName))
        .build();

    new InitDBuilder(appName, serverPort,
        CommonStringsHelper.INSTANCE.buildDebianEtcPath(appName)).build();

    new DebianPackageBuilder(appName, serverVersion, maintainerInfo,
        CommonStringsHelper.DebianBuildBuild + "DEBIAN/control").build();

    new CreateInstallPackageBuilder(serverVersion, appName,
        CommonStringsHelper.DebianBuild + CommonStringsHelper.CreateAndInstallPackageName).build();

    FileUtils.copyDirectory(new File(CommonStringsHelper.BuildInstallPath + appName),
        new File(CommonStringsHelper.DebianBuildBuild + "opt/" + appName));

    final File debianDirectory =
        new File(CommonStringsHelper.DebianBuild + appName + "_" + serverVersion + "_all");
    FileUtils.moveDirectory(new File(CommonStringsHelper.DebianBuildBuild), debianDirectory);

    final Process process;
    process = Runtime.getRuntime().exec("chmod -R 755 " + debianDirectory.getAbsolutePath());
    process.waitFor();

    final YaclibModel.ProcessReport report =
        FileProcessUtilities.INSTANCE.executeProcess(currentDirectory, InitUtilities.DpkgDeb,
            "--build " + debianDirectory);
    System.out.println(report.getNormalOutput());
    System.out.println(report.getErrorOutput());

    return true;
  }

  private void buildDebianStructure() {
    // make directory structure for debian package
    new File(CommonStringsHelper.DebianBuild).delete();
    new File(CommonStringsHelper.DebianBuild).mkdir();
    new File(CommonStringsHelper.DebianBuildBuild).mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "usr").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "usr/sbin").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "opt").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "opt/" + appName).mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "opt/" + appName + "/webapp").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "etc").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "etc/init.d").mkdir();
    new File(CommonStringsHelper.DebianBuildBuild + "DEBIAN").mkdir();
  }
}
