package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.CSharpUtilities
import java.nio.file.Paths

class AssemblyInfoBuilder(
        generatedProjectGuid: String,
        private val mainDependency: YaclibModel.Dependency
): IBuilder<YaclibModel.File> {
    private val InitialTemplate = """using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;

// General Information about an assembly is controlled through the following
// set of attributes. Change these attribute values to modify the information
// associated with an assembly.

[assembly: AssemblyTitle("${CSharpUtilities.buildFullName(mainDependency)}")]
[assembly: AssemblyDescription("Initial library for ${CSharpUtilities.buildFullName(mainDependency)}")]
[assembly: AssemblyConfiguration("")]
[assembly: AssemblyCompany("")]
[assembly: AssemblyProduct("${CSharpUtilities.buildFullName(mainDependency)}")]
[assembly: AssemblyCopyright("Copyright © Mike Roylance")]
[assembly: AssemblyTrademark("")]
[assembly: AssemblyCulture("")]

// Setting ComVisible to false makes the types in this assembly not visible
// to COM components.  If you need to access a type in this assembly from
// COM, set the ComVisible attribute to true on that type.

[assembly: ComVisible(false)]

// The following GUID is for the ID of the typelib if this project is exposed to COM
// todo: this is auto generated right now...
[assembly: Guid("$generatedProjectGuid")]
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate)
                .setFileExtension(YaclibModel.FileExtension.CS_EXT)
                .setFileName("AssemblyInfo")
                .setFullDirectoryLocation(Paths.get(CSharpUtilities.buildFullName(mainDependency), "Properties").toString())
                .build()
        return returnFile
    }
}