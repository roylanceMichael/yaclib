package org.roylance.yaclib.core.services.java.server

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.MavenUtilities
import java.util.*

class POMFileBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {
    private val initialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    ${buildProperties()}

    <groupId>${projectInformation.mainDependency.group}</groupId>
    <artifactId>${CommonTokens.ServerApi}</artifactId>
    <version>${projectInformation.mainDependency.majorVersion}.${projectInformation.mainDependency.minorVersion}</version>

    <repositories>
        ${buildRepositories()}
        $DefaultSonaTypeOSSRepo
    </repositories>

    $DefaultSonaTypeOSSPlugin

    <dependencies>
        ${this.buildDependencies()}
        <dependency>
            <groupId>org.roylance</groupId>
            <artifactId>common</artifactId>
            <version>${JavaUtilities.RoylanceCommonVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.squareup.retrofit2</groupId>
            <artifactId>retrofit</artifactId>
            <version>${JavaUtilities.RetrofitVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-logging-juli</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper-el</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jsp-api</artifactId>
            <version>${JavaUtilities.TomcatVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>${JavaUtilities.GsonVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>${JavaUtilities.KotlinVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz</artifactId>
            <version>${JavaUtilities.QuartzVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz-jobs</artifactId>
            <version>${JavaUtilities.QuartzVersion}</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${JavaUtilities.JUnitVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet</artifactId>
            <version>${JavaUtilities.JerseyMediaVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-multipart</artifactId>
            <version>${JavaUtilities.JerseyMediaVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-json</artifactId>
            <version>${JavaUtilities.JerseyJsonVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.intellij</groupId>
            <artifactId>annotations</artifactId>
            <version>${JavaUtilities.IntellijAnnotationsVersion}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>${JavaUtilities.HttpComponentsVersion}</version>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>${JavaUtilities.ProtobufVersion}</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>${JavaUtilities.CommonsIOVersion}</version>
        </dependency>
    </dependencies>

    <build>
        <directory>target</directory>
        <outputDirectory>target/classes</outputDirectory>
        <finalName>latest</finalName>
        <testOutputDirectory>target/test-classes</testOutputDirectory>
        <sourceDirectory>src/main/java</sourceDirectory>

        <resources>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${JavaUtilities.MavenCompilerPluginVersion}</version>
                <configuration>
                    <verbose>true</verbose>
                    <fork>true</fork>
                    <compilerVersion>${JavaUtilities.JavaServerJdkVersion}</compilerVersion>
                    <source>${JavaUtilities.JavaServerJdkVersion}</source>
                    <target>${JavaUtilities.JavaServerJdkVersion}</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>${JavaUtilities.MavenWarPluginVersion}</version>
                <configuration>
                    <warName>ROOT</warName>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>appassembler-maven-plugin</artifactId>
                <version>${JavaUtilities.CodeHausAppPluginVersion}</version>
                <configuration>
                    <assembleDirectory>target</assembleDirectory>
                    <programs>
                        <program>
                            <mainClass>launch.Main</mainClass>
                            <name>webapp</name>
                        </program>
                    </programs>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>assemble</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${JavaUtilities.MavenSurefirePluginVersion}</version>
            </plugin>

            <plugin>
                <artifactId>kotlin-maven-plugin</artifactId>
                <groupId>org.jetbrains.kotlin</groupId>
                <version>${JavaUtilities.KotlinVersion}</version>

                <executions>
                    <execution>
                        <id>compile</id>
                        <phase>process-sources</phase>
                        <goals> <goal>compile</goal> </goals>
                    </execution>

                    <execution>
                        <id>test-compile</id>
                        <phase>process-test-sources</phase>
                        <goals> <goal>test-compile</goal> </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>com.heroku.sdk</groupId>
                <artifactId>heroku-maven-plugin</artifactId>
                <version>${JavaUtilities.HerokuPluginVersion}</version>
                <configuration>
                    <jdkVersion>${JavaUtilities.JavaServerJdkVersion}</jdkVersion>
                    <appName>${projectInformation.mainDependency.group}</appName>
                    <processTypes>
                        <web>sh proc.sh</web>
                    </processTypes>
                    <includes>
                        <include>src/main/webapp</include>
                        <include>proc.sh</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>

    </build>

</project>
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
            .setFileToWrite(initialTemplate.trim())
            .setFileExtension(YaclibModel.FileExtension.POM_EXT)
            .setFileName(MavenUtilities.PomName)
            .setFullDirectoryLocation("")
            .build()

        return returnFile
    }

    private fun buildDependencies():String {
        val workspace = StringBuilder()

        projectInformation.thirdPartyDependenciesList
                .filter { it.type == YaclibModel.DependencyType.JAVA }
                .forEach { dependency ->
                    workspace.append("""
        <dependency>
            <groupId>${dependency.group}</groupId>
            <artifactId>${dependency.name}</artifactId>
            <version>${buildMavenPropertyReference(JavaUtilities.buildPackageVariableName(dependency))}</version>
        </dependency>
            """)
        }

        projectInformation.controllers.controllerDependenciesList.forEach { controllerDependency ->
            workspace.append("""
        <dependency>
            <groupId>${controllerDependency.dependency.group}</groupId>
            <artifactId>${controllerDependency.dependency.name}</artifactId>
            <version>${buildMavenPropertyReference(JavaUtilities.buildPackageVariableName(controllerDependency.dependency))}</version>
        </dependency>
        <dependency>
            <groupId>${controllerDependency.dependency.group}</groupId>
            <artifactId>c${controllerDependency.dependency.name}</artifactId>
            <version>${buildMavenPropertyReference(JavaUtilities.buildPackageVariableName(controllerDependency.dependency))}</version>
        </dependency>
""")
        }

        return workspace.toString()
    }

    private fun buildRepositories():String {
        val workspace = StringBuilder()

        val uniqueRepositories = HashMap<String, String>()
        uniqueRepositories[projectInformation.mainDependency.mavenRepository.url] = this.buildRepository(projectInformation.mainDependency.mavenRepository)

        projectInformation.controllers.controllerDependenciesList.forEach {
            uniqueRepositories[it.dependency.mavenRepository.url] = this.buildRepository(it.dependency.mavenRepository)
        }

        uniqueRepositories.values.forEach {
            workspace.appendln(it)
        }

        return workspace.toString()
    }

    private fun buildRepository(repository: YaclibModel.Repository): String {
        return """<repository>
  <snapshots>
    <enabled>false</enabled>
  </snapshots>
  <id>${repository.name}</id>
  <name>${repository.name}</name>
  <url>${JavaUtilities.buildRepositoryUrl(repository)}</url>
</repository>
"""
    }

    private fun buildProperties(): String {
        val workspace = StringBuilder("<properties>")

        workspace.appendln(buildProperty(JavaUtilities.GroupName, projectInformation.mainDependency.group))
        workspace.appendln(buildProperty(JavaUtilities.NameName, projectInformation.mainDependency.name))
        workspace.appendln(buildProperty(JavaUtilities.MinorName, projectInformation.mainDependency.minorVersion.toString()))
        workspace.appendln(buildProperty(JavaUtilities.MajorName, projectInformation.mainDependency.majorVersion.toString()))

        val uniqueDependencies = HashMap<String, String>()
        projectInformation.controllers.controllerDependenciesList.forEach {
            uniqueDependencies[JavaUtilities.buildPackageVariableName(it.dependency)] = "${it.dependency.majorVersion}.${it.dependency.minorVersion}"
        }

        projectInformation.thirdPartyDependenciesList
                .filter { it.type == YaclibModel.DependencyType.JAVA }
                .forEach {
                    if (it.thirdPartyDependencyVersion.isEmpty()) {
                        uniqueDependencies[JavaUtilities.buildPackageVariableName(it)] = "${it.majorVersion}.${it.minorVersion}"
                    }
                    else {
                        uniqueDependencies[JavaUtilities.buildPackageVariableName(it)] = it.thirdPartyDependencyVersion
                    }
        }

        uniqueDependencies.keys.forEach {
            workspace.appendln(buildProperty(it, uniqueDependencies[it]!!))
        }

        workspace.appendln("\t</properties>")
        return workspace.toString()
    }

    private fun buildProperty(key:String, value: String): String {
        return "\t\t<$key>$value</$key>"
    }

    private fun buildMavenPropertyReference(item: String): String {
        return "$" + "{" + item + "}"
    }

    companion object {
        const val DefaultSonaTypeOSSRepo = """<repository>
                    <id>sonatype.oss.snapshots</id>
                    <name>Sonatype OSS Snapshot Repository</name>
                    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>"""

        const val DefaultSonaTypeOSSPlugin = """<pluginRepositories>
                <pluginRepository>
                    <id>sonatype.oss.snapshots</id>
                    <name>Sonatype OSS Snapshot Repository</name>
                    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
"""
    }
}