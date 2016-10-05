package org.roylance.yaclib.core.services.python

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.JavaUtilities
import org.roylance.yaclib.core.utilities.PythonUtilities

class SetupBuilder: IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val initialTemplate = """from setuptools import setup, find_packages
import ${PythonUtilities.PropertiesNameWithoutExtension}


setup(
    name=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.FullPackageName},
    version=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.FullVersionName},
    author=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.AuthorName},
    license=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.LicenseName},
    include_package_data=True,
    install_requires=['${PythonUtilities.ProtobufName}==${JavaUtilities.ProtobufVersion}'],
    description=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.DescriptionName},
    author_email=${PythonUtilities.PropertiesNameWithoutExtension}.${JavaUtilities.AuthorName},
    url=properties.${JavaUtilities.GithubUrlName},
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