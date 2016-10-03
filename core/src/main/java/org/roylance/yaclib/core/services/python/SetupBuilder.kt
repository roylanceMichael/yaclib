package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities

class SetupBuilder(private val mainDependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val initialTemplate = """from setuptools import setup, find_packages

setup(
    name="${PythonUtilities.buildPackageName(mainDependency)}",
    version="${mainDependency.majorVersion}.${mainDependency.minorVersion}",
    author="${mainDependency.authorName}",
    license='${mainDependency.license}',
    include_package_data=True,
    install_requires=['${PythonUtilities.ProtobufName}==${JavaUtilities.ProtobufVersion}'],
    description="models to interface with the ${mainDependency.group}.${mainDependency.name} system",
    author_email="${mainDependency.authorName}",
    url="${mainDependency.githubRepo}",
    packages=find_packages(exclude=['tests']))
"""
        val returnFile = YaclibModel.File.newBuilder()
                .setFileToWrite(initialTemplate.trim())
                .setFileExtension(YaclibModel.FileExtension.PY_EXT)
                .setFileName("setup")
                .setFullDirectoryLocation("")
                .build()

        return returnFile
    }

}