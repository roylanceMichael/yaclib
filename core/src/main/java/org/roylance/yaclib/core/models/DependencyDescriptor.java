package org.roylance.yaclib.core.models;

import com.google.protobuf.Descriptors;
import org.roylance.yaclib.YaclibModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DependencyDescriptor {
    private static String getDescriptorName = "getDescriptor";

    public final YaclibModel.Dependency dependency;
    public final Descriptors.FileDescriptor descriptor;


    public DependencyDescriptor(final YaclibModel.Dependency dependency,
                                final Descriptors.FileDescriptor descriptor) {
        this.dependency = dependency;
        this.descriptor = descriptor;
    }


    public DependencyDescriptor(final YaclibModel.Dependency dependency,
                                final String descriptor) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.dependency = dependency;
        this.descriptor = buildFileDescriptor(descriptor);
    }

    public static Descriptors.FileDescriptor buildFileDescriptor(String fullName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Class<?> fileDescriptorClass = Class.forName(fullName);
        final Method getDescriptor = fileDescriptorClass.getMethod(getDescriptorName);
        return  (Descriptors.FileDescriptor) getDescriptor.invoke(fileDescriptorClass);
    }
}
