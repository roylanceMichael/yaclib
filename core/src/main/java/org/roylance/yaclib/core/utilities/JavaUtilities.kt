package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel

object JavaUtilities {
    private val AlphaRegex = Regex("[^A-Za-z]+")

    const val GroupName = "group"
    const val NameName = "name"
    const val MajorName = "major"
    const val MinorName = "minor"
    const val AuthorName = "author"
    const val GithubUrlName = "githubUrl"
    const val LicenseName = "license"
    const val DescriptionName = "description"

    const val ArtifactoryUserName = "ARTIFACTORY_USER"
    const val ArtifactoryPasswordName = "ARTIFACTORY_PASSWORD"

    const val BintrayUserName = "BINTRAY_USER"
    const val BintrayKeyName = "BINTRAY_KEY"

    const val StandardMavenUserName = "STANDARD_MAVEN_USER"
    const val StandardMavenPassword = "STANDARD_MAVEN_PASSWORD"

    const val PropertiesFileNameWithoutExtension = "gradle"
    const val PropertiesFileName = "$PropertiesFileNameWithoutExtension.properties"

    const val DefaultRepository = "http://dl.bintray.com/roylancemichael/maven"

    const val ArtifactoryVersion = "4.4.0"
    const val BintrayVersion = "1.7"
    const val RoylanceCommonVersion = "0.12"
    const val TomcatVersion = "8.0.28"
    const val KotlinVersion = "1.0.6"

    const val CommonsIOVersion = "2.5"
    const val ProtobufVersion = "3.0.0"
    const val HttpComponentsVersion = "4.5.1"
    const val IntellijAnnotationsVersion = "12.0"
    const val JerseyJsonVersion = "1.19.1"
    const val JerseyMediaVersion = "2.22.2"
    const val JUnitVersion = "4.12"
    const val QuartzVersion = "2.2.1"
    const val GsonVersion = "2.7"
    const val RetrofitVersion = "2.1.0"
    const val JettyServerVersion = "9.3.14.v20161028"

    const val JettyServerName = "yaclib_jetty"
    const val JerseyMediaName = "yaclib_jersey"
    const val HttpComponentsName = "yaclib_http_components"
    const val KotlinName = "yaclib_kotlin"
    const val ServerPortName = "yaclib_server_port"
    const val YaclibVersionName = "yaclib_version"
    const val RoylanceCommonName = "yaclib_roylance_common"
    const val BintrayName = "yaclib_bintray"
    const val ArtifactoryName = "yaclib_artifacatory"
    const val RetrofitName = "yaclib_retrofit"

    const val MavenCompilerPluginVersion = "2.3.2"
    const val MavenWarPluginVersion = "2.1.1"
    const val CodeHausAppPluginVersion = "1.1.1"
    const val MavenSurefirePluginVersion = "2.18.1"
    const val HerokuPluginVersion = "1.0.3"
    const val JavaServerJdkVersion = "1.8"
    const val PropertiesMavenPlugin = "1.0.0"

    fun convertGroupNameToFolders(groupName: String): String {
        return groupName.replace(".", "/")
    }

    fun buildFullPackageName(dependency: YaclibModel.Dependency): String {
        return "${dependency.group}.${dependency.name}"
    }

    fun buildPackageVariableName(dependency: YaclibModel.Dependency): String {
        val underscoreReplace = dependency.group.replace(AlphaRegex, "_")
        val underscoreReplaceName = dependency.name.replace(AlphaRegex, "_")
        return "${underscoreReplace}_$underscoreReplaceName"
    }

    fun buildRepositoryUrl(repository: YaclibModel.Repository): String {
        if (repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY ||
            repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_NPM ||
            repository.repositoryType == YaclibModel.RepositoryType.ARTIFACTORY_PYTHON) {
            if (repository.url.isNotEmpty() && repository.url[repository.url.length - 1] == '/') {
                return "${repository.url}${repository.name}"
            }
            return "${repository.url}/${repository.name}"
        }
        return repository.url
    }
}