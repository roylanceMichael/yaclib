package org.roylance.yaclib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.roylance.BuildConfig

class YaclibPlugin implements Plugin<Project> {
  @Override
  void apply(Project target) {
    println(
        "make sure the following variables are defined: yaclib_kotlin, yaclib_version, repoName_, group, githubRepo_, license")

    target.task('updateVersion', type: YaclibVersionTask) {}

    // just going to copy the protobuf files into the directory
    target.task('protoCreateBuildSrc') {
      group = "yaclib"
      dependsOn(':generateProto')
      doLast {
        def buildSrc = target.file("${target.projectDir}/buildSrc/src/main/java")
        if (!buildSrc.exists()) {
          buildSrc.mkdirs()
        }

        def buildSrcBuildGradle = """repositories {
  mavenCentral()
}
dependencies {
  compile "com.google.protobuf:protobuf-java:${BuildConfig.PROTOBUF_VERSION}"
}
"""
        target.file("${target.projectDir}/buildSrc/build.gradle").write(buildSrcBuildGradle)
      }
    }

    target.task('protoOutputCopyBuildSrc', type: Copy) {
      group = "yaclib"
      dependsOn(':protoCreateBuildSrc')
      from target.file("${target.projectDir}/build/generated/source/proto/main/java")
      into target.file("${target.projectDir}/buildSrc/src/main/java")
    }

    target.task('protoOutputCopy', type: Copy) {
      group = "yaclib"
      dependsOn(':protoOutputCopyBuildSrc')
      from target.file("${target.projectDir}/build/generated/source/proto/main/java")
      into target.file("${target.projectDir}/src/main/java")
    }

    // delete the generated ones
    target.task('deleteGenerated', type: Delete) {
      group = "yaclib"
      dependsOn(':protoOutputCopy')
      delete target.file("${target.projectDir}/build/generated/source/proto/main/java");
    }

    //     compile kotlin as normal
    target.compileKotlin.dependsOn("deleteGenerated")
  }
}
