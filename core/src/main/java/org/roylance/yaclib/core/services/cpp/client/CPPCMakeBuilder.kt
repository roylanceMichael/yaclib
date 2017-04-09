package org.roylance.yaclib.core.services.cpp.client

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.CPPUtilities

class CPPCMakeBuilder(private val projectInformation: YaclibModel.ProjectInformation,
                      private val projectName: String): IBuilder<YaclibModel.File> {

    private val cmakeTemplate = """# ${CommonTokens.DoNotAlterMessage}
cmake_minimum_required(VERSION 3.5)
project(${CommonTokens.CppApi})

INCLUDE(FindProtobuf)
FIND_PACKAGE(Protobuf REQUIRED)
INCLUDE_DIRECTORIES(${CPPUtilities.ProtobufIncludeDir})

set(CMAKE_CXX_FLAGS "${CPPUtilities.CMakeCXXFlags} -std=c++11")

"""

//        set(SOURCE_FILES
//        src/cpp/models/yaorm_models.pb.h
//        src/cpp/models/yaorm_models.pb.cc
//        src/cpp/services/i_sql_generator_service.h
//        src/cpp/services/sqlite/sqlite_generator_service.cpp
//        src/cpp/services/sqlite/sqlite_generator_service.h
//        src/cpp/models/sqlite_models.pb.h
//        src/cpp/models/sqlite_models.pb.cc
//        src/cpp/utilities/common_sql_utilities.cpp
//        src/cpp/utilities/common_sql_utilities.h
//        src/cpp/services/i_granular_database_service.h
//        src/cpp/services/i_map_streamer.h
//        src/cpp/services/sqlite/sqlite_granular_database_service.cpp
//        src/cpp/services/sqlite/sqlite_granular_database_service.h)
//    add_library(yaorm_sqlite_cpp SHARED ${SOURCE_FILES})
//    target_link_libraries(yaorm_sqlite_cpp ${PROTOBUF_LIBRARY})

    override fun build(): YaclibModel.File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}