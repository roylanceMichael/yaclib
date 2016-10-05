package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.ArtifactoryUtilities
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.io.File
import java.nio.file.Paths

class TypeScriptBuilder(private val location: String,
                        private val mainDependency: YaclibModel.Dependency): IBuilder<Boolean> {
    override fun build(): Boolean {
        // run node stuff
        val javaScriptDirectory = Paths.get(location, CommonTokens.JavaScriptName).toFile()

        println("building protobufs for npm")
        val restoreDependenciesReport = TypeScriptUtilities.restoreDependencies(javaScriptDirectory.toString())
        println(restoreDependenciesReport.normalOutput)
        println(restoreDependenciesReport.errorOutput)

        // this is custom
        val createJsonModelProcess = FileProcessUtilities.executeProcess(javaScriptDirectory.toString(),
                Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(),
                "../api/src/main/resources/*.proto > $ModelJson",
                false)
        println(createJsonModelProcess.normalOutput)
        println(createJsonModelProcess.errorOutput)

        val createModelJsReport = FileProcessUtilities.executeProcess(javaScriptDirectory.toString(),
                Paths.get(javaScriptDirectory.toString(), NodeModules, "protobufjs", Bin, "pbjs").toString(),
                "../api/src/main/resources/*.proto -t js > $ModelJS",
                false)
        println(createModelJsReport.normalOutput)
        println(createModelJsReport.errorOutput)

        val proto2TypeScriptReport = FileProcessUtilities.executeProcess(javaScriptDirectory.toString(),
                Paths.get(javaScriptDirectory.toString(), NodeModules, "proto2typescript", Bin, "proto2typescript-bin.js").toString(),
                "--file $ModelJson > ${mainDependency.typescriptModelFile}.d.ts",
                false)
        println(proto2TypeScriptReport.normalOutput)
        println(proto2TypeScriptReport.errorOutput)

        // protobuf ts helper
        val typeScriptDefinitionPath = "./${mainDependency.typescriptModelFile}.d.ts"
        val inputFileString = FileProcessUtilities.readFile(Paths.get(javaScriptDirectory.toString(), ModelJS).toString())
        val outputTypeScript = TypeScriptUtilities.buildTypeScriptOutput(
                typeScriptDefinitionPath,
                mainDependency.typescriptModelFile,
                inputFileString)
        FileProcessUtilities.writeFile(outputTypeScript, Paths.get(javaScriptDirectory.toString(), "${mainDependency.typescriptModelFile}Factory.ts").toString())

        Paths.get(javaScriptDirectory.toString(), ModelJson).toFile().delete()
        Paths.get(javaScriptDirectory.toString(), ModelJS).toFile().delete()

        println("compiling npm")
        val compileReport = TypeScriptUtilities.build(javaScriptDirectory.toString())
        println(compileReport.normalOutput)
        println(compileReport.errorOutput)

        println("publishing npm")
        val publishReport = TypeScriptUtilities.buildPublish(javaScriptDirectory.toString(), mainDependency)
        println(publishReport.normalOutput)
        println(publishReport.errorOutput)

        return true
    }

    companion object {
        private const val Bin = "bin"
        private const val NodeModules = "node_modules"
        private const val ModelJson = "model.json"
        private const val ModelJS = "model.js"
    }
}