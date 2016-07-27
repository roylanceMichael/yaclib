package org.roylance.yaclib.core.services

import com.google.protobuf.Descriptors
import org.roylance.yaclib.Models

interface IProcessFileDescriptorService {
    fun processFile(fileDescriptor: Descriptors.FileDescriptor): Models.AllControllers
}