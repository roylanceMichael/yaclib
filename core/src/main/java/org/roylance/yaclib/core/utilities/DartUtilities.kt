package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel.Dependency
import org.roylance.yaclib.YaclibModel.ProcessReport
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IProjectBuilderServices
import java.nio.file.Paths

object DartUtilities : IProjectBuilderServices {
  private const val ProtocGenDart = "protoc-gen-dart"

  override fun buildProtobufs(
    location: String,
    mainDependency: Dependency
  ): ProcessReport {
    val protocGenDartLocation = FileProcessUtilities.getActualLocation(
        ProtocGenDart
    )

    val sourceDirectory =
      Paths.get(location, "${mainDependency.name}${CommonTokens.DartSuffix}", CommonTokens.LibName)
          .toFile()
    val protobufLocation = Paths.get(
        location, mainDependency.name, "src", "main",
        "proto"
    )
        .toString()
    val arguments =
      "--plugin=$protocGenDartLocation -I=$protobufLocation --proto_path=$protobufLocation --dart_out=$sourceDirectory $protobufLocation/*.proto"
    return FileProcessUtilities.executeProcess(
        sourceDirectory.toString(), InitUtilities.Protoc,
        arguments
    )
  }

  override fun getVersion(location: String): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun setVersion(
    location: String,
    dependency: Dependency
  ): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun updateDependencyVersion(
    location: String,
    otherDependency: Dependency
  ): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun incrementVersion(
    location: String,
    dependency: Dependency
  ): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun clean(location: String): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun build(location: String): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun buildPackage(
    location: String,
    dependency: Dependency
  ): ProcessReport {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun publish(
    location: String,
    dependency: Dependency,
    apiKey: String
  ): ProcessReport {
    return ProcessReport.getDefaultInstance()
  }

  override fun restoreDependencies(
    location: String,
    doAnonymously: Boolean
  ): ProcessReport {
    return FileProcessUtilities.executeProcess(location, InitUtilities.DartPub, "install")
  }
}