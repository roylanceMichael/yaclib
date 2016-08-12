package org.roylance.yaclib.core.models;

import com.google.protobuf.Descriptors;
import org.roylance.yaclib.YaclibModel;

public class DependencyDescriptor {
    public final YaclibModel.Dependency dependency;
    public final Descriptors.FileDescriptor descriptor;
    public DependencyDescriptor(final YaclibModel.Dependency dependency,
                                final Descriptors.FileDescriptor descriptor) {
        this.dependency = dependency;
        this.descriptor = descriptor;
    }
}
