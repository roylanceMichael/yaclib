package org.roylance.yaclib.core.services.csharp

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities

class IHttpClientBuilder(
        private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {

    private val InitialTemplate = """using System.Threading.Tasks;
namespace ${CSharpUtilities.buildFullName(dependency)}
{
	public interface ${CommonTokens.HttpExecute}
	{
		Task<string> PostAsync(string url, string base64Message);
	}
}
"""

    override fun build(): YaclibModel.File {
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(InitialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.CS_EXT)
                .setFileName(CommonTokens.HttpExecute)
                .setFullDirectoryLocation(CSharpUtilities.buildFullName(dependency))
                .build()
        return returnFile    }
}