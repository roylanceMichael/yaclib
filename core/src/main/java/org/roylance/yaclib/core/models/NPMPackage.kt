package org.roylance.yaclib.core.models

import java.util.*

class NPMPackage {
    var name: String? = null
    var version: String? = null
    var description: String? = null
    var homePage: String? = null
    var keyWords: MutableList<String>? = null

    class Bugs {
        var url: String? = null
        var email: String? = null
    }
    var bugs: Bugs? = null

    class Script {
        var test: String? = null
    }

    var scripts: Script? = null

    var author: String? = null
    var license: String? = null

    var dependencies = HashMap<String, String>()
}