package org.roylance.yaclib

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class YaclibVersionTask extends DefaultTask {
  @Override
  String getGroup() {
    return "yaclib"
  }

  @TaskAction Boolean buildVersion() {
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int minute = Calendar.getInstance().get(Calendar.MINUTE);

    final String majorVersion = Integer.toString(year);
    final String minorVersion = month + "." + day + "." + hour + "." + minute;

    final Properties properties = new Properties();
    properties.load(project.rootProject.file("gradle.properties").newDataInputStream());
    properties.setProperty("major", majorVersion);
    properties.setProperty("minor", minorVersion);
    final OutputStream outputStream =
        project.rootProject.file("gradle.properties").newDataOutputStream();
    properties.store(outputStream,
        "YACLIB: updated version to " + majorVersion + "." + minorVersion);
    outputStream.flush();
    outputStream.close();

    return true;
  }
}
