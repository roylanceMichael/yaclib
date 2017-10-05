package org.roylance.yaclib.core.services

import org.roylance.yaclib.YaclibModel
import java.util.*

interface IProcessLanguageService {
  fun buildInterface(projectInformation: YaclibModel.ProjectInformation): YaclibModel.AllFiles
}