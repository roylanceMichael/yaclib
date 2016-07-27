package org.roylance.yaclib.core.services

import org.roylance.yaclib.Models

interface IFilePersistService {
    fun persistFiles(initialLocation: String, files: Models.AllFiles)
}