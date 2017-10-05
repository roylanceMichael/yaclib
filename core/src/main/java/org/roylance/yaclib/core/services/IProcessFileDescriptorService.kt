package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.roylance.yaclib.YaclibModel

interface IProcessFileDescriptorService {
  fun processFile(fileDescriptor: Descriptors.FileDescriptor): YaclibModel.AllControllers
}