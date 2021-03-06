package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
import java.util.*

object GradleUtilities : IProjectBuilderServices {
  override fun incrementVersion(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
    if (!propertiesFile.exists()) {
      propertiesFile.createNewFile()
    }

    val properties = Properties()
    val inputStream = FileInputStream(propertiesFile)
    inputStream.use { it ->
      properties.load(it)
    }

    val minorVersion = properties.getProperty(JavaUtilities.MinorName)
    properties.setProperty(JavaUtilities.MinorName, minorVersion.toString())
    val majorVersion = properties.getProperty(JavaUtilities.MajorName)

    val outputStream = FileOutputStream(propertiesFile)
    try {
      properties.store(outputStream,
          "incrementVersion: ${dependency.majorVersion}.${dependency.minorVersion}")
    } finally {
      outputStream.close()
    }

    return YaclibModel.ProcessReport.newBuilder()
        .setNewMinor(minorVersion)
        .setNewMajor(majorVersion)
        .build()
  }

  override fun updateDependencyVersion(location: String,
      otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
    if (!propertiesFile.exists()) {
      propertiesFile.createNewFile()
    }

    if (!propertiesFile.exists()) {
      return YaclibModel.ProcessReport.newBuilder().setIsError(true).build()
    }
    val properties = Properties()
    val inputStream = FileInputStream(propertiesFile)
    try {
      properties.load(inputStream)
    } finally {
      inputStream.close()
    }

    // double check this logic, may need more info
    properties.setProperty(JavaUtilities.buildPackageVariableName(otherDependency
        .toBuilder()
        .setName("${otherDependency.name}${CommonTokens.ClientSuffix}").build()),
        "${otherDependency.majorVersion}.${otherDependency.minorVersion}")

    val variableName = JavaUtilities.buildPackageVariableName(otherDependency)
    if (otherDependency.thirdPartyDependencyVersion.isEmpty()) {
      val version = "${otherDependency.majorVersion}.${otherDependency.minorVersion}"
      properties.setProperty(variableName, version)
    } else {
      properties.setProperty(variableName, otherDependency.thirdPartyDependencyVersion)
    }

    val outputStream = FileOutputStream(propertiesFile)
    try {
      properties.store(outputStream, "update dependency: $variableName")
    } finally {
      outputStream.close()
    }

    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  override fun getVersion(location: String): YaclibModel.ProcessReport {
    val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
    if (!propertiesFile.exists()) {
      propertiesFile.createNewFile()
    }

    val properties = Properties()
    val inputStream = FileInputStream(propertiesFile)
    try {
      properties.load(inputStream)
      return YaclibModel.ProcessReport.newBuilder()
          .setNewMinor(properties.getProperty(JavaUtilities.MinorName))
          .setNewMajor(properties.getProperty(JavaUtilities.MajorName))
          .build()
    } finally {
      inputStream.close()
    }
  }

  override fun setVersion(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    val propertiesFile = Paths.get(location, JavaUtilities.PropertiesFileName).toFile()
    if (!propertiesFile.exists()) {
      propertiesFile.createNewFile()
    }

    val properties = Properties()
    val inputStream = FileInputStream(propertiesFile)
    try {
      properties.load(inputStream)
    } finally {
      inputStream.close()
    }

    properties.setProperty(JavaUtilities.MajorName, dependency.majorVersion.toString())
    properties.setProperty(JavaUtilities.MinorName, dependency.minorVersion.toString())

    val outputStream = FileOutputStream(propertiesFile)
    try {
      properties.store(outputStream,
          "set version: ${dependency.majorVersion}.${dependency.minorVersion}")
    } finally {
      outputStream.close()
    }

    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  override fun clean(location: String): YaclibModel.ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "clean")
  }

  override fun build(location: String): YaclibModel.ProcessReport {
    val buildReport = FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "build")
    val installReport = FileProcessUtilities.executeProcess(location, InitUtilities.Gradle,
        "install")

    return buildReport.toBuilder()
        .setContent(buildReport.content + installReport.content)
        .setErrorOutput(buildReport.errorOutput + installReport.errorOutput)
        .build()
  }

  override fun buildPackage(location: String,
      dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "packageApp")
  }

  override fun publish(location: String,
      dependency: YaclibModel.Dependency,
      apiKey: String): YaclibModel.ProcessReport {
    if (dependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
      return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle,
          "artifactoryPublish")
    } else if (dependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.STANDARD_MAVEN) {
      return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "upload")
    } else {
      return FileProcessUtilities.executeProcess(location, InitUtilities.Gradle, "bintrayUpload")
    }
  }

  override fun restoreDependencies(location: String,
      doAnonymously: Boolean): YaclibModel.ProcessReport {
    return YaclibModel.ProcessReport.getDefaultInstance()
  }

  fun buildRepository(repository: YaclibModel.Repository): String {
    if (repository.repositoryType == YaclibModel.RepositoryType.PRIVATE_BINTRAY) {
      return """maven {
    url "${JavaUtilities.buildRepositoryUrl(repository)}"
    credentials {
        username System.getenv('${JavaUtilities.BintrayUserName}')
        password System.getenv('${JavaUtilities.BintrayKeyName}')
    }
}"""
    } else if (repository.repositoryType == YaclibModel.RepositoryType.STANDARD_MAVEN) {
      return """maven {
    url "${JavaUtilities.buildRepositoryUrl(repository)}"
    credentials {
        username System.getenv('${JavaUtilities.StandardMavenUserName}')
        password System.getenv('${JavaUtilities.StandardMavenPassword}')
    }
}"""
    } else if (repository.url.isNotEmpty()) {
      return """maven {
    url "${JavaUtilities.buildRepositoryUrl(repository)}"
}"""
    }
    return ""
  }

  fun buildDependencies(projectInformation: YaclibModel.ProjectInformation): String {
    val workspace = StringBuilder()

    projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
      workspace.append(
          """compile "${controllerDependency.dependency.group}:${controllerDependency.dependency.name}:$${JavaUtilities.buildPackageVariableName(
              controllerDependency.dependency)}"
""")
    }

    return workspace.toString()
  }

  fun buildUploadMethod(projectInformation: YaclibModel.ProjectInformation,
      projectName: String): String {
    if (!projectInformation.mainDependency.hasMavenRepository()) {
      return ""
    }
    if (projectInformation.mainDependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY) {
      return buildArtifactory(projectInformation)
    } else if (projectInformation.mainDependency.mavenRepository.repositoryType == YaclibModel.RepositoryType.BINTRAY) {
      return buildBintray(projectInformation, projectName)
    }
    return buildStandardMaven(projectInformation, projectName)
  }

  fun buildBintray(projectInformation: YaclibModel.ProjectInformation,
      projectName: String): String {
    return """
bintray {
    user = System.getenv('${JavaUtilities.BintrayUserName}')
    key = System.getenv('${JavaUtilities.BintrayKeyName}')
    publications = ['mavenJava']
    publish = true
    pkg {
        repo = "${projectInformation.mainDependency.mavenRepository.name}"
        name = "${projectInformation.mainDependency.group}.$projectName"
        userOrg = user
        licenses = ['${projectInformation.mainDependency.license}']
        labels = [rootProject.name]
        publicDownloadNumbers = true
        vcsUrl = '${buildGithubRepo(projectInformation)}'
        version {
            name = "${projectInformation.mainDependency.majorVersion}.${projectInformation.mainDependency.minorVersion}"
        }
    }
}"""
  }

  fun buildGithubRepo(projectInformation: YaclibModel.ProjectInformation): String {
    return projectInformation.mainDependency.githubRepo
  }

  fun buildArtifactory(projectInformation: YaclibModel.ProjectInformation): String {
    return """artifactory {
    contextUrl = "${projectInformation.mainDependency.mavenRepository.url}"   // The base Artifactory URL if not overridden by the publisher/resolver
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

  fun buildStandardMaven(projectInformation: YaclibModel.ProjectInformation,
      projectName: String): String {
    return """uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: "${projectInformation.mainDependency.mavenRepository.url}") {
                authentication(userName: System.getenv('${JavaUtilities.StandardMavenUserName}'), password: System.getenv('${JavaUtilities.StandardMavenPassword}'))
            }
            pom.version = "$${JavaUtilities.MajorName}.$${JavaUtilities.MinorName}"
            pom.artifactId = "$projectName"
            pom.groupId = "${projectInformation.mainDependency.group}"
        }
    }
}
"""
  }
}