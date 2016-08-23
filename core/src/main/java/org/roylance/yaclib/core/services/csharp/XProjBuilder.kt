package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.CSharpUtilities

class XProjBuilder(projectGuid: String, private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val InitialTemplate = """<?xml version="1.0" encoding="utf-8"?>
<Project DefaultTargets="Build" ToolsVersion="14.0" xmlns="http://schemas.microsoft.com/developer/msbuild/2003">
  <PropertyGroup>
    <VisualStudioVersion Condition="'$(VisualStudioVersion)' == ''">14.0</VisualStudioVersion>
    <VSToolsPath Condition="'$(VSToolsPath)' == ''">$(MSBuildExtensionsPath32)\Microsoft\VisualStudio\v$(VisualStudioVersion)</VSToolsPath>
  </PropertyGroup>
  <Import Project="$(VSToolsPath)\DotNet\Microsoft.DotNet.Props" Condition="'$(VSToolsPath)' != ''" />
  <PropertyGroup Label="Globals">
    <ProjectGuid>$projectGuid</ProjectGuid>
    <RootNamespace>${CSharpUtilities.buildFullName(mainDependency)}</RootNamespace>
    <BaseIntermediateOutputPath Condition="'$(BaseIntermediateOutputPath)' == ''">.\obj</BaseIntermediateOutputPath>
    <OutputPath Condition="'$(OutputPath)' == ''">.\bin\</OutputPath>
    <TargetFrameworkVersion>v4.5</TargetFrameworkVersion>
  </PropertyGroup>
  <PropertyGroup>
    <SchemaVersion>2.0</SchemaVersion>
  </PropertyGroup>
  <Import Project="$(VSToolsPath)\DotNet\Microsoft.DotNet.targets" Condition="'$(VSToolsPath)' != ''" />
</Project>
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileExtension(YaclibModel.FileExtension.XPROJ_EXT)
                .setFileName(CSharpUtilities.buildFullName(mainDependency))
                .setFullDirectoryLocation(CSharpUtilities.buildFullName(mainDependency))
                .build()
        return returnFile
    }
}