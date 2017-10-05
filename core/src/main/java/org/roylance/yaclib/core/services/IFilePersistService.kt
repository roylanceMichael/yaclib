package org.roylance.yaclib.core.services

import org.roylance.yaclib.YaclibModel

interface IFilePersistService {
  fun persistFiles(initialLocation: String, files: YaclibModel.AllFiles)
}