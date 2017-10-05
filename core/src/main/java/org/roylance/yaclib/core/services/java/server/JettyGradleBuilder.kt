package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.JavaUtilities

class JettyGradleBuilder(
    private val projectInformation: YaclibModel.ProjectInformation) : IBuilder<YaclibModel.File> {
  private val InitialTemplate = """${CommonTokens.AutoGeneratedAlteringOkay}
import org.roylance.yaclib.YaclibPackagePlugin;

buildscript {
    ext.kotlin_version = "$${JavaUtilities.KotlinName}"
    repositories {
        jcenter()
        mavenCentral()
        maven { url '${JavaUtilities.DefaultRepository}'}
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$${JavaUtilities.KotlinName}"
        classpath "org.roylance:yaclib.core:$${JavaUtilities.YaclibVersionName}"
    }
}

group "$${JavaUtilities.GroupName}"
version "$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"

apply plugin: 'java'
apply plugin: 'kotlin'
apply plugin: 'application'

sourceCompatibility = 1.8
mainClassName="${projectInformation.mainDependency.group}.Main"

repositories {
    mavenCentral()
    maven { url '${JavaUtilities.DefaultRepository}'}
    ${GradleUtilities.buildRepository(projectInformation.mainDependency.mavenRepository)}
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '${JavaUtilities.JUnitVersion}'

    compile "org.eclipse.jetty:jetty-server:$${JavaUtilities.JettyServerName}"
    compile "org.eclipse.jetty:jetty-servlet:$${JavaUtilities.JettyServerName}"
    compile "org.eclipse.jetty:jetty-webapp:$${JavaUtilities.JettyServerName}"

    compile "org.glassfish.jersey.core:jersey-server:$${JavaUtilities.JerseyMediaName}"
    compile "org.glassfish.jersey.containers:jersey-container-servlet-core:$${JavaUtilities.JerseyMediaName}"
    compile "org.glassfish.jersey.containers:jersey-container-servlet:$${JavaUtilities.JerseyMediaName}"
    compile "org.glassfish.jersey.media:jersey-media-multipart:$${JavaUtilities.JerseyMediaName}"

    compile "org.apache.httpcomponents:httpclient:$${JavaUtilities.HttpComponentsName}"

${buildDependencies()}
}

task packageApp(type: YaclibPackagePlugin) {
    appName = rootProject.name
    serverVersion = "$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"
    maintainerInfo = "${projectInformation.mainDependency.authorName}"
    serverPort = "$${JavaUtilities.ServerPortName}".toInteger()
}

packageApp.dependsOn(installDist)
"""

  override fun build(): YaclibModel.File {
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(InitialTemplate.trim())
        .setFileExtension(YaclibModel.FileExtension.GRADLE_EXT)
        .setFileName("build")
        .setFullDirectoryLocation("")
        .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
        .build()

    return returnFile
  }

  private fun buildDependencies(): String {
    val workspace = StringBuilder()

    projectInformation.thirdPartyDependenciesList
        .filter { it.type == YaclibModel.DependencyType.JAVA }
        .forEach { dependency ->
          workspace.append(
              """    compile "${dependency.group}:${dependency.name}:$${JavaUtilities.buildPackageVariableName(
                  dependency)}"
""")
        }

    projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
      workspace.append(
          """    compile "${controllerDependency.dependency.group}:c${controllerDependency.dependency.name}:$${JavaUtilities.buildPackageVariableName(
              controllerDependency.dependency)}"
""")
    }

    return workspace.toString()
  }
}