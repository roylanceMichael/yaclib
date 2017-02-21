package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftShareDataBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {
    private val frameworkName = SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)

    override fun build(): YaclibModel.File {
        val file = YaclibModel.File.newBuilder()
                .setFileExtension(YaclibModel.FileExtension.XCSCHEME_EXT)
                .setFileToWrite(InitialTemplate)
                .setFileName("${frameworkName}_iOS")
                .setFullDirectoryLocation("${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.xcodeproj/xcshareddata/xcshemes")
                .build()
        return file
    }

    private val InitialTemplate = """<?xml version="1.0" encoding="UTF-8"?>
<Scheme
   LastUpgradeVersion = "0800"
   version = "1.3">
   <BuildAction
      parallelizeBuildables = "YES"
      buildImplicitDependencies = "YES">
      <BuildActionEntries>
         <BuildActionEntry
            buildForTesting = "YES"
            buildForRunning = "YES"
            buildForProfiling = "YES"
            buildForArchiving = "YES"
            buildForAnalyzing = "YES">
            <BuildableReference
               BuildableIdentifier = "primary"
               BlueprintIdentifier = "BD12FD351D767BA0001815C7"
               BuildableName = "$frameworkName.framework"
               BlueprintName = "${frameworkName}_iOS"
               ReferencedContainer = "container:$frameworkName.xcodeproj">
            </BuildableReference>
         </BuildActionEntry>
      </BuildActionEntries>
   </BuildAction>
   <LaunchAction
      buildConfiguration = "Debug"
      selectedDebuggerIdentifier = "Xcode.DebuggerFoundation.Debugger.LLDB"
      selectedLauncherIdentifier = "Xcode.DebuggerFoundation.Launcher.LLDB"
      launchStyle = "0"
      useCustomWorkingDirectory = "NO"
      ignoresPersistentStateOnLaunch = "NO"
      debugDocumentVersioning = "YES"
      debugServiceExtension = "internal"
      allowLocationSimulation = "YES">
      <MacroExpansion>
         <BuildableReference
            BuildableIdentifier = "primary"
            BlueprintIdentifier = "BD12FD351D767BA0001815C7"
            BuildableName = "$frameworkName.framework"
            BlueprintName = "${frameworkName}_iOS"
            ReferencedContainer = "container:$frameworkName.xcodeproj">
         </BuildableReference>
      </MacroExpansion>
      <AdditionalOptions>
      </AdditionalOptions>
   </LaunchAction>
   <ProfileAction
      buildConfiguration = "Release"
      shouldUseLaunchSchemeArgsEnv = "YES"
      savedToolIdentifier = ""
      useCustomWorkingDirectory = "NO"
      debugDocumentVersioning = "YES">
      <MacroExpansion>
         <BuildableReference
            BuildableIdentifier = "primary"
            BlueprintIdentifier = "BD12FD351D767BA0001815C7"
            BuildableName = "$frameworkName.framework"
            BlueprintName = "${frameworkName}_iOS"
            ReferencedContainer = "container:$frameworkName.xcodeproj">
         </BuildableReference>
      </MacroExpansion>
   </ProfileAction>
   <AnalyzeAction
      buildConfiguration = "Debug">
   </AnalyzeAction>
   <ArchiveAction
      buildConfiguration = "Release"
      revealArchiveInOrganizer = "YES">
   </ArchiveAction>
</Scheme>
"""
}