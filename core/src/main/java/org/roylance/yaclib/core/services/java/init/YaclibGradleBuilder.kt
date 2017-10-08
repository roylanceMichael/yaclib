package org.roylance.yaclib.core.services.java.init

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CLITokens
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.StringUtilities

class YaclibGradleBuilder(
    dependency: YaclibModel.Dependency) : IBuilder<YaclibModel.File> {
  private val initialTemplate = """import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.YaclibPlugin

group '${dependency.group}'
version = "$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"

apply plugin: 'java'


buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven { url ${CLITokens.RepoUrlName} }
    }
    dependencies {
        classpath "org.roylance:yaclib.core:$${CLITokens.YaclibMajorName}.$${CLITokens.YaclibMinorName}"
        classpath "${dependency.group}:${dependency.name}:$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"
    }
}

task execute(type: YaclibPlugin) {
    location = new File(System.getProperty("user.dir")).parentFile.toString()

    mainModel = "${dependency.group}.${StringUtilities.convertToPascalCase(dependency.name)}Model"
    mainController = "${dependency.group}.${StringUtilities.convertToPascalCase(
      dependency.name)}MainController"

    dependencyDescriptors = new ArrayList<>()
    thirdPartyServerDependencies = new ArrayList<>()
    nugetKey = System.getenv('NUGET_KEY')

    mainDependency = YaclibModel.Dependency.newBuilder()
        .setGroup("${dependency.group}")
        .setName("${dependency.name}")
        .setAuthorName(${CLITokens.AuthorName})
        .setLicense(${CLITokens.LicenseName})
        .setGithubRepo(${CLITokens.GithubRepoName})
        .setMajorVersion(${JavaUtilities.MajorName}.toInteger())
        .setMinorVersion(${JavaUtilities.MinorName}.toInteger())
        .setTypescriptModelFile("${StringUtilities.convertToPascalCase(dependency.name)}Model")
        .setNugetRepository(YaclibModel.Repository.newBuilder()
            .setRepositoryType(YaclibModel.RepositoryType.NUGET)
            .build())
        .setNpmRepository(YaclibModel.Repository.newBuilder()
            .setRepositoryType(YaclibModel.RepositoryType.NPMJS)
            .build())
        .setMavenRepository(YaclibModel.Repository.newBuilder()
            .setName(${CLITokens.RepoNameName})
            .setUrl(${CLITokens.RepoUrlName})
            .setRepositoryType(YaclibModel.RepositoryType.${dependency.mavenRepository.name}).build())
            .build()
}
"""

  override fun build(): YaclibModel.File {
    return YaclibModel.File.newBuilder()
        .setFileToWrite(initialTemplate)
        .setFileName("yaclib")
        .setFileExtension(YaclibModel.FileExtension.GRADLE_EXT)
        .setFileUpdateType(YaclibModel.FileUpdateType.WRITE_IF_NOT_EXISTS)
        .setFullDirectoryLocation("")
        .build()
  }
}