package org.roylance.yaclib.core.services.cpp.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CSharpUtilities
import org.roylance.yaclib.core.utilities.StringUtilities

class CPPHeaderBuilder(private val controller: YaclibModel.Controller,
                       private val dependency: YaclibModel.Dependency): IBuilder<YaclibModel.File> {
    override fun build(): YaclibModel.File {
        val workspace = StringBuilder()
        val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

        val initialTemplate = """${CommonTokens.DoNotAlterMessage}
#include "../models/yaorm_models.pb.h"

namespace ${CSharpUtilities.buildFullName(dependency)}
{
    public interface $interfaceName
    {
"""
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}