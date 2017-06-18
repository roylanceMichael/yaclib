package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.nio.file.Paths

class TypeScriptBuilder(private val location: String,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        // run node stuff
        val javaScriptDirectory = Paths.get(location, CommonTokens.JavaScriptName).toFile()
        println(InitUtilities.buildPhaseMessage("typescript client begin"))

        println(InitUtilities.buildPhaseMessage("building protobufs for npm"))
        val protobufJsInstallReport = FileProcessUtilities.executeProcess(
                javaScriptDirectory.toString(),
                InitUtilities.NPM,
                "install -g protobufjs@${TypeScriptUtilities.ProtobufJsVersion}")
        println(protobufJsInstallReport.normalOutput)
        println(protobufJsInstallReport.errorOutput)

        val restoreDependenciesReport = TypeScriptUtilities.restoreDependencies(javaScriptDirectory.toString())
        println(restoreDependenciesReport.normalOutput)
        println(restoreDependenciesReport.errorOutput)

        // this is custom
        val createJsonModelProcess = FileProcessUtilities.executeProcess(javaScriptDirectory.toString(),
                "pbjs",
                "-t static-module -w commonjs -o ${mainDependency.typescriptModelFile}.js ../api/src/main/resources/*.proto")
        println(createJsonModelProcess.normalOutput)
        println(createJsonModelProcess.errorOutput)

        val proto2TypeScriptReport = FileProcessUtilities.executeProcess(javaScriptDirectory.toString(),
                "pbts",
                "-o ${mainDependency.typescriptModelFile}.d.ts ${mainDependency.typescriptModelFile}.js")
        println(proto2TypeScriptReport.normalOutput)
        println(proto2TypeScriptReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("compiling npm"))
        val compileReport = TypeScriptUtilities.build(javaScriptDirectory.toString())
        println(compileReport.normalOutput)
        println(compileReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("publishing npm"))
        val publishReport = TypeScriptUtilities.publish(javaScriptDirectory.toString(), mainDependency)
        println(publishReport.normalOutput)
        println(publishReport.errorOutput)

        println(InitUtilities.buildPhaseMessage("typescript client end"))

        return true
    }
}