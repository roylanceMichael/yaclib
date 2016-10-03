package org.roylance.yaclib.core.plugins

import com.google.protobuf.Descriptors
import org.apache.commons.io.FileUtils
import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.models.DependencyDescriptor
import org.roylance.yaclib.core.plugins.client.CSharpBuilder
import org.roylance.yaclib.core.plugins.client.JavaClientBuilder
import org.roylance.yaclib.core.plugins.client.PythonBuilder
import org.roylance.yaclib.core.plugins.client.TypeScriptBuilder
import org.roylance.yaclib.core.plugins.server.JavaServerBuilder
import java.nio.file.Paths

class PluginLogic(
        val location: String,
        val mainDependency: YaclibModel.Dependency,
        val mainController: Descriptors.FileDescriptor,
        val dependencyDescriptors: List<DependencyDescriptor>,
        val thirdPartyServerDependencies: List<YaclibModel.Dependency>,
        val nugetKey: String?): IBuilder<Boolean> {

    override fun build(): Boolean {
        println("deleting ${Paths.get(location, CommonTokens.JavaScriptName).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.JavaScriptName).toFile())
        println("deleting ${Paths.get(location, CommonTokens.ClientApi).toFile()}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.ClientApi).toFile())
        println("deleting ${Paths.get(location, CommonTokens.CSharpName)}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.CSharpName).toFile())
        println("deleting ${Paths.get(location, CommonTokens.PythonName)}")
        FileUtils.deleteDirectory(Paths.get(location, CommonTokens.PythonName).toFile())

        println("running main logic now")
        MainLogic(
                location,
                mainDependency,
                mainController,
                dependencyDescriptors,
                thirdPartyServerDependencies).build()

        println("now doing final cleanup for csharp")
        CSharpBuilder(location, mainDependency, nugetKey).build()
        println("now doing final cleanup for python")
        PythonBuilder(location, mainDependency).build()

        println("now doing final cleanup for java")
        JavaClientBuilder(location, mainDependency).build()
        println("now doing final cleanup for typescript")
        TypeScriptBuilder(location, mainDependency).build()

        // java server has dependencies on both java client and typescript client
        println("now doing final cleanup for server")
        JavaServerBuilder(location).build()

        return true
    }
}