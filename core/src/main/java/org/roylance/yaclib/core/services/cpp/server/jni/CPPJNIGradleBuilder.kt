package org.roylance.yaclib.core.services.cpp.server.jni

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.GradleUtilities
import org.roylance.yaclib.core.utilities.JavaUtilities

class CPPJNIGradleBuilder(private val projectInformation: YaclibModel.ProjectInformation,
                          private val projectName: String): IBuilder<YaclibModel.File> {
    private val projectDirName = "\$projectDir"

    private val InitialTemplate = """${CommonTokens.AutoGeneratedAlteringOkay}
buildscript {
    ext.kotlin_version = "$${JavaUtilities.KotlinName}"
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath "org.jfrog.buildinfo:build-info-extractor-gradle:$${JavaUtilities.ArtifactoryName}"
        classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:$${JavaUtilities.BintrayName}"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$${JavaUtilities.KotlinName}"
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
apply plugin: 'cpp'

sourceCompatibility = 1.8

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
}

println "Building on OS: " + System.properties['os.name']
println "Using JDK: " + System.properties['java.home']

def SYS_INCLUDE_DIR = this.properties['system.include.dir']
def SYS_LOCAL_INCLUDE_DIR = this.properties['system.local.include.dir']
def JNI_INCLUDE_DIR = this.properties['jni.include.dir']
def JNI_LIB_DIR = this.properties['jni.lib.dir']

println "Using system include directory: " + SYS_INCLUDE_DIR
println "Using system local include directory: " + SYS_LOCAL_INCLUDE_DIR
println "Using JNI include directory: " + JNI_INCLUDE_DIR
println "Using JNI lib directory: " + JNI_LIB_DIR

model {
    components {
        bridge(NativeLibrarySpec) {
            sources {
                cpp {
                    source {
                        srcDir 'src/main/jni'
                        include "**/*.cpp"
                    }
                }
            }
            buildTypes {
                debug
                release
            }
        }
    }
    toolChains {
        gcc(Gcc) {
            eachPlatform {
                if (System.properties['os.name'].equals('Mac OS X')) {
                    cppCompiler.withArguments { args ->
                        args << "-O2"
                        args << "-I" + SYS_INCLUDE_DIR
                        args << "-I" + SYS_LOCAL_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR + "/darwin"
                        args << "-std=c++11"
                    }
                    linker.withArguments { args ->
                        args << "-O2"
                        args << "-lc++"
                    }
                } else {
                    path "/opt/rh/devtoolset-2/root/usr/bin/gcc"
                    cppCompiler.withArguments { args ->
                        args << "-O2"
                        args << "-I" + SYS_INCLUDE_DIR
                        args << "-I" + SYS_LOCAL_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR + "/linux"
                        args << "-std=c++11"
                    }
                    linker.withArguments { args ->
                        args << "-O2"
                        args << "-lstdc++"
                    }
                }
            }
        }
        clang(Clang) {
            eachPlatform {
                if (System.properties['os.name'].equals('Mac OS X')) {
                    cppCompiler.withArguments { args ->
                        args << "-O2"
                        args << "-I" + SYS_INCLUDE_DIR
                        args << "-I" + SYS_LOCAL_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR + "/darwin"
                        args << "-std=c++11"
                    }
                    linker.withArguments { args ->
                        args << "-O2"
                        args << "-lc++"
                    }
                } else {
                    cppCompiler.withArguments { args ->
                        args << "-O2"
                        args << "-I" + SYS_INCLUDE_DIR
                        args << "-I" + SYS_LOCAL_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR
                        args << "-I" + JNI_INCLUDE_DIR + "/linux"
                        args << "-std=c++11"
                    }
                    linker.withArguments { args ->
                        args << "-O2"
                        args << "-lstdc++"
                    }
                }
            }
        }
    }
}

task copyLibBridge(type: Copy) {
    if (System.properties['os.name'].equals('Mac OS X')) {
        from "$projectDirName/build/binaries/bridgeSharedLibrary/libbridge.dylib"
    }
    else {
        from "$projectDirName/build/binaries/bridgeSharedLibrary/libbridge.so"
    }
    into 'libs'
}

${GradleUtilities.buildUploadMethod(projectInformation, projectName)}

repositories {
    mavenCentral()
    maven { url '${JavaUtilities.DefaultRepository}'}
    ${GradleUtilities.buildRepository(projectInformation.mainDependency.mavenRepository)}
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '${JavaUtilities.JUnitVersion}'
    compile "com.squareup.retrofit2:retrofit:$${JavaUtilities.RetrofitName}"

    compile "org.roylance:common:$${JavaUtilities.RoylanceCommonName}"
    ${GradleUtilities.buildDependencies(projectInformation)}
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