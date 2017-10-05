package org.roylance.yaclib.core.plugins.debian

import org.roylance.common.service.IBuilder
import java.io.File

class InitDBuilder(projectName: String, port: Int,
    private val fileLocation: String) : IBuilder<Boolean> {
  private val InitialTemplate = """#!/usr/bin/env bash
# /etc/init.d/$projectName

touch /var/lock/$projectName

case "$1" in
  start)
    pushd /opt/$projectName
    bash proc.sh > web.out 2>&1&
    ;;
  stop)
    kill -9 $(lsof -i:$port -t)
    ;;
  *)
    echo "Usage: /etc/init.d/$projectName {start|stop}"
    exit 1
    ;;
esac

exit 0
"""

  override fun build(): Boolean {
    File(fileLocation).delete()
    File(fileLocation).writeText(InitialTemplate)
    return true
  }
}