package org.roylance.yaclib.core.plugins.debian

import org.roylance.common.service.IBuilder
import java.io.File

class StopBuilder(private val port: Int, private val fileLocation: String) : IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
kill -9 $(lsof -i:$port -t)
"""

        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}