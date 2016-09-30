package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.JavaUtilities

class GradleFileBuilder(private val controllerDependencies: YaclibModel.AllControllerDependencies,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """${CommonTokens.DoNotAlterMessage}
buildscript {
    ext.kotlin_version = '${JavaUtilities.KotlinVersion}'
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath "org.jfrog.buildinfo:build-info-extractor-gradle:${JavaUtilities.ArtifactoryVersion}"
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:${JavaUtilities.BintrayVersion}'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${JavaUtilities.KotlinVersion}"
    }
}

group '${mainDependency.group}'
version '${mainDependency.majorVersion}.${mainDependency.minorVersion}'

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

${if (mainDependency.repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) buildArtifactory() else buildBintray()}

repositories {
    mavenCentral()
    maven { url '${JavaUtilities.DefaultRepository}'}
    ${this.buildRepository()}
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
        return this.mainDependency.githubRepo
    }

    private fun buildRepository(): String {
        if (this.mainDependency.hasRepository() &&
            this.mainDependency.repository.isPrivate) {
            if (mainDependency.repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
                return """maven {
    url "${JavaUtilities.buildRepositoryUrl(mainDependency.repository)}"
    credentials {
        username System.getenv('ARTIFACTORY_USER')
        password System.getenv('ARTIFACTORY_PASSWORD')
    }
}
"""
            }
            return """maven {
    url "${JavaUtilities.buildRepositoryUrl(mainDependency.repository)}"
    credentials {
        username System.getenv('BINTRAY_USER')
        password System.getenv('BINTRAY_KEY')
    }
}
"""
        }
        else if (this.mainDependency.hasRepository()) {
            if (mainDependency.repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
                return """maven {
    url "${JavaUtilities.buildRepositoryUrl(mainDependency.repository)}"
}
"""
            }
            return """maven {
    url "${JavaUtilities.buildRepositoryUrl(mainDependency.repository)}"
}
"""
        }

        return ""
    }

    private fun buildBintray(): String {
        return """
bintray {
    user = System.getenv('BINTRAY_USER')
    key = System.getenv('BINTRAY_KEY')
    publications = ['mavenJava']
    publish = true
    pkg {
        repo = '${mainDependency.repository.name}'
        name = "${mainDependency.group}.${CommonTokens.ClientApi}"
        userOrg = user
        licenses = ['${mainDependency.license}']
        labels = [rootProject.name]
        publicDownloadNumbers = true
        vcsUrl = '${buildGithubRepo()}'
        version {
            name = "${mainDependency.majorVersion}.${mainDependency.minorVersion}"
        }
    }
}"""
    }

    private fun buildArtifactory(): String {
        return """artifactory {
    contextUrl = "${mainDependency.repository.url}"   //The base Artifactory URL if not overridden by the publisher/resolver
    publish {
        repository {
            repoKey = '${mainDependency.repository.name}'
            username = System.getenv('ARTIFACTORY_USER')
            password = System.getenv('ARTIFACTORY_PASSWORD')
            maven = true

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
"""
    }
}