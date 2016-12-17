package org.roylance.yaclib.core.models

import java.util.*

class DotNetProject {
    var name: String? = null
    var version: String? = null
    var description: String? = null
    var copyright: String? = null
    var title: String? = null
    var entryPoint: String? = null
    var testRunner: String? = null
    var authors: MutableList<String>? = null
    var language: String? = null
    var embedInteropTypes: Boolean? = null
    var preprocess: String? = null
    var shared: String? = null

    class Dependency {
        var dependencies = HashMap<String, String>()
    }

    var dependencies = HashMap<String, String>()

    class BuildOptions {
        var debugType: String? = null
    }

    var frameworks: HashMap<String, Dependency>? = null
}