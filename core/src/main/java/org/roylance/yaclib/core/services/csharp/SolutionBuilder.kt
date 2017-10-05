package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.CSharpUtilities

class SolutionBuilder(generatedSolutionGuid: String,
    generatedProjectGuid: String,
    private val mainDependency: YaclibModel.Dependency) : IBuilder<YaclibModel.File> {

  private val InitialTemplate = """
Microsoft Visual Studio Solution File, Format Version 12.00
# Visual Studio 2012
Project("{$generatedSolutionGuid}") = "${CSharpUtilities.buildFullName(
      mainDependency)}", "${CSharpUtilities.buildFullName(
      mainDependency)}.xproj", "{$generatedProjectGuid}"
EndProject
Global
	GlobalSection(SolutionConfigurationPlatforms) = preSolution
		Debug|Any CPU = Debug|Any CPU
		Release|Any CPU = Release|Any CPU
	EndGlobalSection
	GlobalSection(ProjectConfigurationPlatforms) = postSolution
		{$generatedProjectGuid}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{$generatedProjectGuid}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{$generatedProjectGuid}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{$generatedProjectGuid}.Release|Any CPU.Build.0 = Release|Any CPU
	EndGlobalSection
EndGlobal
"""

  override fun build(): YaclibModel.File {
    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(InitialTemplate)
        .setFileExtension(YaclibModel.FileExtension.SLN_EXT)
        .setFileName(CSharpUtilities.buildFullName(mainDependency))
        .setFullDirectoryLocation(CSharpUtilities.buildFullName(mainDependency))
        .build()
    return returnFile
  }
}