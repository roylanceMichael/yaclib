package org.roylance.yaclib.core.plugins.debian

import org.roylance.common.service.IBuilder
import java.io.File

class RunBuilder(private val projectName: String, private val fileLocation: String) : IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
pushd /opt/$projectName
bash /opt/$projectName/custom.sh
nohup /opt/$projectName/bin/$projectName "$@" > latest.out 2>&1&
"""

        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}