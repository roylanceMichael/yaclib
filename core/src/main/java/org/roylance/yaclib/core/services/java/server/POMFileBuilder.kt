package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder

class POMFileBuilder(groupName: String, overallPackageName: String, dependency: Models.Dependency): IBuilder<Models.File> {
    private val initialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>$groupName</groupId>
    <artifactId>${CommonTokens.ServerApi}</artifactId>
    <version>0.1-SNAPSHOT</version>

    <repositories>
        <repository>
            <id>mike-MacBook</id>
            <name>mike-MacBook-snapshots</name>
            <url>http://chaperapp.dyndns-server.com:8081/artifactory/libs-snapshot-local</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>sonatype.oss.snapshots</id>
            <name>Sonatype OSS Snapshot Repository</name>
            <url>http://oss.sonatype.org/content/repositories/snapshots</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <pluginRepositories>
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

    <dependencies>
        <dependency>
            <groupId>${dependency.group}</groupId>
            <artifactId>${dependency.name}</artifactId>
            <version>${dependency.version}</version>
        </dependency>

        <dependency>
            <groupId>org.roylance</groupId>
            <artifactId>common</artifactId>
            <version>LATEST</version>
        </dependency>

        <dependency>
            <groupId>com.squareup.retrofit2</groupId>
            <artifactId>retrofit</artifactId>
            <version>2.1.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>$tomcatVersion</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-logging-juli</artifactId>
            <version>$tomcatVersion</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>$tomcatVersion</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper</artifactId>
            <version>$tomcatVersion</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jasper-el</artifactId>
            <version>$tomcatVersion</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jsp-api</artifactId>
            <version>$tomcatVersion</version>
        </dependency>

        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.6.2</version>
        </dependency>

        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
            <version>$kotlinVersion</version>
        </dependency>

        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz</artifactId>
            <version>2.2.1</version>
        </dependency>

        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz-jobs</artifactId>
            <version>2.2.1</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet</artifactId>
            <version>2.22.2</version>
        </dependency>

        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-json</artifactId>
            <version>1.19.1</version>
        </dependency>

        <dependency>
            <groupId>com.intellij</groupId>
            <artifactId>annotations</artifactId>
            <version>12.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.1</version>
        </dependency>

        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>3.0.0-beta-3</version>
        </dependency>

        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
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
                <version>2.3.2</version>
                <configuration>
                    <verbose>true</verbose>
                    <fork>true</fork>
                    <compilerVersion>1.8</compilerVersion>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.1.1</version>
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
                <version>1.1.1</version>
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
                <version>2.18.1</version>
            </plugin>

            <plugin>
                <artifactId>kotlin-maven-plugin</artifactId>
                <groupId>org.jetbrains.kotlin</groupId>
                <version>$kotlinVersion</version>

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
                <version>1.0.3</version>
                <configuration>
                    <jdkVersion>1.8</jdkVersion>
                    <appName>$overallPackageName</appName>
                    <processTypes>
                        <web>sh heroku_proc.sh</web>
                    </processTypes>
                    <includes>
                        <include>src/main/webapp</include>
                        <include>heroku_proc.sh</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>

    </build>

</project>
"""

    override fun build(): Models.File {
        val returnFile = Models.File.newBuilder()
            .setFileToWrite(initialTemplate.trim())
            .setFileExtension(Models.FileExtension.POM_EXT)
            .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
            .setFileName("pom")
            .setFullDirectoryLocation("")
            .build()

        return returnFile
    }

    companion object {
        private const val tomcatVersion = "8.0.28"
        private const val kotlinVersion = "1.0.3"
    }
}