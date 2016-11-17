package org.roylance.yaclib.controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import org.roylance.yaclib.Main
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.plugins.InitLogic
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.enums.UITokens
import java.net.URL
import java.util.*

class MainController: Initializable {
    @FXML
    private var groupNameTextField: TextField? = null
    @FXML
    private var baseLocationTextField: TextField? = null
    @FXML
    private var repositoryComboBox: ComboBox<String>? = null
    @FXML
    private var repositoryURLTextField: TextField? = null
    @FXML
    private var messageLabel: Label? = null
    @FXML
    private var gradleLocationLabel: Label? = null
    @FXML
    private var mavenLocationLabel: Label? = null
    @FXML
    private var npmLocationLabel: Label? = null
    @FXML
    private var tscLocationLabel: Label? = null
    @FXML
    private var pythonLocationLabel: Label? = null
    @FXML
    private var protocLocationLabel: Label? = null
    @FXML
    private var nugetLocationLabel: Label? = null
    @FXML
    private var dotNetLocationLabel: Label? = null
    @FXML
    private var windowsLabel: Label? = null
    @FXML
    private var settingsMessageLabel: Label? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        repositoryComboBox?.items?.add(ArtifactoryName)
        repositoryComboBox?.items?.add(BintrayName)

        gradleLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.Gradle)
        mavenLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.Maven)
        npmLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.NPM)
        tscLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.TypeScriptCompiler)
        pythonLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.Python)
        protocLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.Protoc)
        nugetLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.Nuget)
        dotNetLocationLabel?.text = FileProcessUtilities.getActualLocation(InitUtilities.DotNet)
        windowsLabel?.text = if (InitUtilities.isNotWindows()) "This is either Mac or Linux, which works" else "This is Windows, which will not work"
        settingsMessageLabel?.text = if (InitUtilities.hasMinimumRequired()) "This system is configured to run yaclib" else "This system is missing programs in the path"
    }

    fun createProject() {
        val groupName = groupNameTextField?.text
        val baseLocation = baseLocationTextField?.text
        val repo = repositoryComboBox?.selectionModel?.selectedItem
        val repoUrl = repositoryURLTextField?.text

        if (groupName == null || groupName.isEmpty()) {
            messageLabel?.text = "need to have a group name!"
            return
        }

        if (baseLocation == null || baseLocation.isEmpty()) {
            messageLabel?.text = "need to have a base location!"
            return
        }

        if (repo == null || repo.isEmpty()) {
            messageLabel?.text = "need to have a repository selected!"
            return
        }

        if (repoUrl == null || repoUrl.isEmpty()) {
            messageLabel?.text = "need to have a repository url!"
            return
        }

        messageLabel?.text = "processing..."

        val yaclibDependency = YaclibModel.Dependency.newBuilder()
            .setName(UITokens.YaclibName)
            .setGroup(UITokens.YaclibGroup)
            .setMajorVersion(UITokens.YaclibMajor)
            .setMinorVersion(UITokens.YaclibMinor)
            .build()

        val name = groupName.split(".").last()
        val repoName = repoUrl.split("/").last()
        val mainDependency = YaclibModel.Dependency.newBuilder()
            .setName(name)
            .setGroup(groupName)
            .setMajorVersion(0)
            .setMinorVersion(1)
            .setGithubRepo(FillOutMessage)
            .setAuthorName(FillOutMessage)
            .setLicense(FillOutMessage)
            .setMavenRepository(YaclibModel.Repository.newBuilder()
                .setRepositoryType(if (BintrayName == repo) YaclibModel.RepositoryType.BINTRAY else YaclibModel.RepositoryType.ARTIFACTORY)
                .setName(if (BintrayName == repo) repoName else "")
                .setUrl(repoUrl))
            .build()

        val result = InitLogic(
                baseLocation,
                mainDependency,
                yaclibDependency).build()

        messageLabel?.text = "processing... result was $result"
    }

    fun chooseFolder() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Choose Directory Location"
        val directory = directoryChooser.showDialog(Main.stage)

        if (directory != null) {
            baseLocationTextField?.text = directory.absolutePath
        }
    }

    companion object {
        private const val BintrayName = "bintray"
        private const val ArtifactoryName = "artifactory"
        private const val FillOutMessage = "<FILL_OUT>"
    }
}