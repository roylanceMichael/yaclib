package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.services.IProjectBuilderServices

object CPPUtilities: IProjectBuilderServices {
    const val ProtobufIncludeDir = "$" + "{PROTOBUF_INCLUDE_DIR}"
    const val CMakeCXXFlags = "$" + "{CMAKE_CXX_FLAGS}"

    override fun getVersion(location: String): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDependencyVersion(location: String, otherDependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun incrementVersion(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clean(location: String): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun build(location: String): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildPackage(location: String, dependency: YaclibModel.Dependency): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publish(location: String, dependency: YaclibModel.Dependency, apiKey: String): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun restoreDependencies(location: String, doAnonymously: Boolean): YaclibModel.ProcessReport {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}