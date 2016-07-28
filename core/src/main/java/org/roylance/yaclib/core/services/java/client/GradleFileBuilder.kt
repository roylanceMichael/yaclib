package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.services.IBuilder

class GradleFileBuilder(dependency: Models.Dependency): IBuilder<Models.File> {
    private val InitialTemplate = """group '${dependency.group}'
version '${dependency.version}'

apply plugin: 'java'
apply plugin: 'kotlin'
apply plugin: 'com.jfrog.artifactory'
apply plugin: 'maven-publish'

sourceCompatibility = 1.7

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
}

artifactory {
    contextUrl = 'http://chaperapp.dyndns-server.com:8081/artifactory'
    publish {
        repository {
            repoKey = 'libs-snapshot-local'
            username = System.getenv('ARTIFACTORY_USER')
            password = System.getenv('ARTIFACTORY_PASSWORD')
        }
        defaults {
            publications('mavenJava')
            publishBuildInfo = true
            publishArtifacts = true
            properties = ['qa.level': 'basic', 'dev.team' : 'core']
            publishPom = true
        }
    }
    resolve {
        repository {
            repoKey = 'repo'
        }
    }
}

buildscript {
    ext.kotlin_version = '$kotlin_version'
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath "org.jfrog.buildinfo:build-info-extractor-gradle:3.1.1"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

repositories {
    mavenCentral()
    maven { url 'http://mikeapps.org:8081/artifactory/ext-release-local' }
    maven { url 'http://chaperapp.dyndns-server.com:8081/artifactory/libs-snapshot-local' }
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'

    compile 'com.google.protobuf:protobuf-java:3.0.0-beta-3'
    compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

    compile 'org.roylance:common:0.3-SNAPSHOT'
    compile '${dependency.group}:${dependency.name}:${dependency.version}'
}
"""

    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileName("build")
                .setFileExtension(Models.FileExtension.GRADLE_EXT)
                .setFullDirectoryLocation("")

        return returnFile.build()
    }

    companion object {
        private const val kotlin_version = "1.0.3"
        private const val ArtifactoryUser = "\$System.env.ARTIFACTORY_USER"
        private const val ArtifactoryPassword = "\$System.env.ARTIFACTORY_PASSWORD"
    }
}