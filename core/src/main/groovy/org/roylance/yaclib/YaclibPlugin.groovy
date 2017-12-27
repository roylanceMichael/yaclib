package org.roylance.yaclib

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete

class YaclibPlugin implements Plugin<Project> {

  @Override
  void apply(Project target) {
    println(
        "make sure the following variables are defined: yaclib_kotlin, yaclibMajor, yaclibMinor, repoName_, group, githubRepo_, major, minor, license")

    target.task('updateVersion', type: YaclibVersionTask) {}

    // just going to copy the protobuf files into the directory
    target.task('protoOutputCopyBuildSrc', type: Copy) {
      group = "yaclib"
      dependsOn(':generateProto')
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
