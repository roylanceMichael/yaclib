package org.roylance.yaclib.core.services

import org.roylance.yaclib.Models

interface IProcessLanguageService {
    fun buildInterface(controllers: Models.AllControllers, overallPackage: String, groupName: String, dependency: Models.Dependency): Models.AllFiles
}