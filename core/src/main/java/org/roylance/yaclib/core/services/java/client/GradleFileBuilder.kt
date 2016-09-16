package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.JavaUtilities

class GradleFileBuilder(private val controllerDependencies: YaclibModel.AllControllerDependencies,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """
buildscript {
    ext.kotlin_version = '${JavaUtilities.KotlinVersion}'
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${JavaUtilities.KotlinVersion}"
    }
}

group '${mainDependency.group}'
version '${mainDependency.majorVersion}.${mainDependency.minorVersion}'

apply plugin: 'java'
apply plugin: 'kotlin'
apply plugin: 'com.jfrog.bintray'
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

bintray {
    user = System.getenv('BINTRAY_USER')
    key = System.getenv('BINTRAY_KEY')
    publications = ['mavenJava']
    publish = true
    pkg {
        repo = 'maven'
        name = "${mainDependency.group}.${CommonTokens.ClientApi}"
        userOrg = user
        licenses = ['Apache-2.0']
        labels = [rootProject.name]
        publicDownloadNumbers = true
        vcsUrl = '${buildGithubRepo()}'
        version {
            name = "${mainDependency.majorVersion}.${mainDependency.minorVersion}"
        }
    }
}

repositories {
    mavenCentral()
    maven { url 'http://dl.bintray.com/roylancemichael/maven' }
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'

    compile 'org.roylance:common:${JavaUtilities.RoylanceCommonVersion}'
    ${this.buildDependencies()}
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileName("build")
                .setFileExtension(YaclibModel.FileExtension.GRADLE_EXT)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }

    private fun buildDependencies():String {
        val workspace = StringBuilder()

        this.controllerDependencies.controllerDependenciesList.forEach { controllerDependency ->
        workspace.append("""compile '${controllerDependency.dependency.group}:${controllerDependency.dependency.name}:${controllerDependency.dependency.majorVersion}.${controllerDependency.dependency.minorVersion}'
""")
        }

        return workspace.toString()
    }

    private fun buildGithubRepo(): String {
        if (this.mainDependency.githubRepo.length > 0) {
            return this.mainDependency.githubRepo
        }

        return "https://github.com/roylanceMichael/Roylance.Common.git"
    }
}