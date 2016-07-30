package org.roylance.yaclib.core.utilities

import org.roylance.yaclib.YaclibModel
import java.util.*

object TypeScriptUtilities {
    val protobufHelperDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("0.0.8").setGroup("protobuftshelper").setNodeAliasName("@mroylance").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val protobufJsDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^5.0.1").setGroup("protobufjs").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val proto2TypeScriptDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^2.2.0").setGroup("proto2typescript").setType(YaclibModel.DependencyType.TYPESCRIPT)

    val baseTypeScriptKit = object: ArrayList<YaclibModel.Dependency>(){
        init {
            add(protobufHelperDependencyBuilder.build())
            add(protobufJsDependencyBuilder.build())
            add(proto2TypeScriptDependencyBuilder.build())
        }
    }

    val angularDependencyBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^1.5.7").setGroup("angular").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val angularRouteBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^1.5.7").setGroup("angular-route").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val bytebufferBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^5.0.1").setGroup("bytebuffer").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val longBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.2.0").setGroup("long").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val bootstrapBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.3.6").setGroup("bootstrap").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val d3Builder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.5.17").setGroup("d3").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val dagreD3Builder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^0.4.17").setGroup("dagre-d3").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val fontAwesomeBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^4.6.3").setGroup("font-awesome").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val jqueryBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^2.2.4").setGroup("jquery").setType(YaclibModel.DependencyType.TYPESCRIPT)
    val webpackStreamBuilder = YaclibModel.Dependency.newBuilder().setThirdPartyDependencyVersion("^3.2.0").setGroup("webpack-stream").setType(YaclibModel.DependencyType.TYPESCRIPT)

    val baseServerTypeScriptKit = object: ArrayList<YaclibModel.Dependency>() {
        init {
            add(angularDependencyBuilder.build())
            add(angularRouteBuilder.build())
            add(bytebufferBuilder.build())
            add(longBuilder.build())
            add(bootstrapBuilder.build())
            add(d3Builder.build())
            add(dagreD3Builder.build())
            add(fontAwesomeBuilder.build())
            add(jqueryBuilder.build())
            add(protobufJsDependencyBuilder.build())
            add(webpackStreamBuilder.build())
        }
    }

    fun buildDependency(dependency: YaclibModel.Dependency): String {
        if (dependency.type.equals(YaclibModel.DependencyType.INTERNAL)) {
            if (dependency.hasNodeAliasName()) {
                return """"${dependency.nodeAliasName}/${dependency.group}.${dependency.name}": "${buildVersion(dependency)}"
"""
            }
            else {
                return """"${dependency.group}.${dependency.name}": "${buildVersion(dependency)}"
"""
            }
        }

        if (dependency.hasNodeAliasName()) {
            return """"${dependency.nodeAliasName}/${dependency.group}": "${buildVersion(dependency)}"
"""
        }
        else {
            return """"${dependency.group}": "${buildVersion(dependency)}"
"""
        }
    }

    private fun buildVersion(dependency: YaclibModel.Dependency): String {
        if (dependency.type.equals(YaclibModel.DependencyType.INTERNAL)) {
            return "0.0.${dependency.version}"
        }
        else {
            return dependency.thirdPartyDependencyVersion
        }
    }
}