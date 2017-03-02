package org.roylance.yaclib.core.plugins.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.InitUtilities
import org.roylance.yaclib.core.utilities.SwiftUtilities
import java.io.File
import java.nio.file.Paths
import java.util.*

class SwiftBuilder(private val location: String,
                   private val projectInformation: YaclibModel.ProjectInformation,
                   private val processSwift: Boolean): IBuilder<Boolean> {
    override fun build(): Boolean {
        // generate template
        println(InitUtilities.buildPhaseMessage("building protobufs for swift"))
        val generateProtoProcessReport = SwiftUtilities.buildProtobufs(location)
        println(generateProtoProcessReport.normalOutput)
        println(generateProtoProcessReport.errorOutput)

        // add custom swift files to project
        val newFiles = ArrayList<String>()
        val sourceFolders = Paths.get(location, CommonTokens.SwiftName, "Source").toFile()

        if (sourceFolders.exists() && sourceFolders.isDirectory) {
            sourceFolders.list().forEach {
                newFiles.add(File(sourceFolders, it).absolutePath)
            }
        }

        println(InitUtilities.buildPhaseMessage("adding files to project"))
        val addReport = SwiftUtilities.addFilesToProject(projectInformation.mainDependency, location, newFiles)
        println(addReport.normalOutput)
        println(addReport.errorOutput)

        if (processSwift) {
            // call carthage update
            println(InitUtilities.buildPhaseMessage("restoring carthage dependencies"))
            val restoreReport = SwiftUtilities.restoreDependencies(location)
            println(restoreReport.normalOutput)
            println(restoreReport.errorOutput)

            // add alamofire framework
            // add swiftprotobuf framework
            println(InitUtilities.buildPhaseMessage("adding frameworks to project"))
            val addFrameworksReport = SwiftUtilities.addFrameworksToProject(projectInformation.mainDependency, location)
            println(addFrameworksReport.normalOutput)
            println(addFrameworksReport.errorOutput)

            // build
            println(InitUtilities.buildPhaseMessage("building swift project"))
            val buildReport = SwiftUtilities.build(location)
            println(buildReport.normalOutput)
            println(buildReport.errorOutput)
        }

        return true
    }
}