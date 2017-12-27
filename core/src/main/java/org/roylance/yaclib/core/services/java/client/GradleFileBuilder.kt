package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.JavaUtilities

class GradleFileBuilder(projectInformation: YaclibModel.ProjectInformation,
    projectName: String) : IBuilder<YaclibModel.File> {
  private val InitialTemplate = """${CommonTokens.AutoGeneratedAlteringOkay}
buildscript {
    ext.kotlin_version = "$${JavaUtilities.KotlinName}"
    repositories {
        jcenter()
        mavenCentral()
        maven { url '${JavaUtilities.DefaultRepository}'}
    }
    dependencies {
        classpath "org.jfrog.buildinfo:build-info-extractor-gradle:$${JavaUtilities.ArtifactoryName}"
        classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:$${JavaUtilities.BintrayName}"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$${JavaUtilities.KotlinName}"
        classpath "org.roylance:yaclib.core:${'$'}${JavaUtilities.YaclibVersionName}"
    }
}

group "$${JavaUtilities.GroupName}"
version "$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"

apply plugin: 'java'
apply plugin: 'kotlin'
apply plugin: 'com.jfrog.bintray'
apply plugin: "com.jfrog.artifactory"
apply plugin: 'maven'
apply plugin: 'maven-publish'

sourceCompatibility = 1.7

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
}

${GradleUtilities.buildUploadMethod(projectInformation, projectName)}

repositories {
    mavenCentral()
    maven { url '${JavaUtilities.DefaultRepository}'}
    ${GradleUtilities.buildRepository(projectInformation.mainDependency.mavenRepository)}
}

// pull in models
sourceSets {
    main {
      java.srcDirs = ['src/main/java',
                      '../${projectInformation.mainDependency.name}/src/main/java']
    }
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '${YaclibStatics.JUnitVersion}'

    compile "org.roylance:roylance.common:${'$'}${'{'}YaclibStatics.RoylanceCommonVersion${'}'}"
}
"""

  override fun build(): YaclibModel.File {
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(InitialTemplate)
        .setFileName("build")
        .setFileExtension(YaclibModel.FileExtension.GRADLE_EXT)
        .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
        .setFullDirectoryLocation("")

    return returnFile.build()
  }
}