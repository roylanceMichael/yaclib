package org.roylance.yaclib.core.services.java.client

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder
import org.roylance.yaclib.core.utilities.StringUtilities

class JavaRetrofitBuilder(private val controller: Models.Controller, private val overallPackage: String): IBuilder<Models.File> {

    override fun build(): Models.File {
        val workspace = StringBuilder()
        val lowercaseName = controller.name.toLowerCase()
        val interfaceName = "I${controller.name}${CommonTokens.UpperCaseRestName}"
        val initialTemplate = """package $overallPackage.${CommonTokens.ServicesName};

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface $interfaceName {
"""
        workspace.append(initialTemplate)
        val initialUrl = StringUtilities.buildUrl("/rest/$lowercaseName/")
        controller.actionsList.forEach { action ->
            if (action.inputsCount == 1) {
                val input = action.inputsList.first()

                val lowercaseActionName = StringUtilities.buildUrl(action.name.toLowerCase())
                val initialActionTemplate = """
    @POST("$initialUrl$lowercaseActionName")
    Call<String> ${action.name.toLowerCase()}(@Body String ${input.argumentName});
"""
                workspace.append(initialActionTemplate)
            }
            else if (action.inputsCount > 1) {
                   val inputArguments = action.inputsList.map { input ->
                       """@Part("${input.argumentName}") String ${input.argumentName}
"""
                   }.joinToString()

                val lowercaseActionName = StringUtilities.buildUrl(action.name.toLowerCase())
                val initialActionTemplate = """
    @Multipart
    @POST("$initialUrl$lowercaseActionName")
    Call<String> ${action.name.toLowerCase()}($inputArguments);
"""
                workspace.append(initialActionTemplate)
            }
        }

        workspace.append("}")

        val returnFile = Models.File.newBuilder()
                .setFileToWrite(workspace.toString())
                .setFileName(interfaceName)
                .setFileExtension(Models.FileExtension.JAVA_EXT)
                .setFullDirectoryLocation(StringUtilities.convertPackageToJavaFolderStructureServices(overallPackage, CommonTokens.ServicesName))

        return  returnFile.build()
    }
}