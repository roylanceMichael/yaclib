package org.roylance.yaclib.core.services.java.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.JavaUtilities

class GradleFileBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {
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

group "$${JavaUtilities.GroupName}"
version "$${JavaUtilities.FullVersionName}"

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

${if (projectInformation.mainDependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) buildArtifactory() else buildBintray()}

repositories {
    mavenCentral()
    maven { url '${JavaUtilities.DefaultRepository}'}
    ${this.buildRepository()}
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '${JavaUtilities.JUnitVersion}'
    compile 'com.squareup.retrofit2:retrofit:${JavaUtilities.RetrofitVersion}'

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

        projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
        workspace.append("""compile "${controllerDependency.dependency.group}:${controllerDependency.dependency.name}:$${JavaUtilities.buildPackageVariableName(controllerDependency.dependency)}"
""")
        }

        return workspace.toString()
    }

    private fun buildGithubRepo(): String {
        return projectInformation.mainDependency.githubRepo
    }

    private fun buildRepository(): String {
       if (projectInformation.mainDependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.PRIVATE_BINTRAY) {
            return """maven {
    url "${JavaUtilities.buildRepositoryUrl(projectInformation.mainDependency.mavenRepository)}"
    credentials {
        username System.getenv('${JavaUtilities.BintrayUserName}')
        password System.getenv('${JavaUtilities.BintrayKeyName}')
    }
}"""
        }
        else if (projectInformation.mainDependency.hasMavenRepository() && projectInformation.mainDependency.mavenRepository.url.length > 0) {
           return """maven {
    url "${JavaUtilities.buildRepositoryUrl(projectInformation.mainDependency.mavenRepository)}"
}"""
       }
        return ""
    }

    private fun buildBintray(): String {
        return """
bintray {
    user = System.getenv('${JavaUtilities.BintrayUserName}')
    key = System.getenv('${JavaUtilities.BintrayKeyName}')
    publications = ['mavenJava']
    publish = true
    pkg {
        repo = "${projectInformation.mainDependency.mavenRepository.name}"
        name = "${projectInformation.mainDependency.group}.${CommonTokens.ClientApi}"
        userOrg = user
        licenses = ['${projectInformation.mainDependency.license}']
        labels = [rootProject.name]
        publicDownloadNumbers = true
        vcsUrl = '${buildGithubRepo()}'
        version {
            name = "$${JavaUtilities.FullVersionName}"
        }
    }
}"""
    }

    private fun buildArtifactory(): String {
        return """artifactory {
    contextUrl = "${projectInformation.mainDependency.mavenRepository.url}"   //The base Artifactory URL if not overridden by the publisher/resolver
    publish {
        repository {
            repoKey = '${projectInformation.mainDependency.mavenRepository.name}'
            username = System.getenv('${JavaUtilities.ArtifactoryUserName}')
            password = System.getenv('${JavaUtilities.ArtifactoryPasswordName}')
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