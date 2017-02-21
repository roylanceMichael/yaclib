package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftPBXProjBuilder(private val projectInformation: YaclibModel.ProjectInformation): IBuilder<YaclibModel.File> {

    override fun build(): YaclibModel.File {
        val file = YaclibModel.File.newBuilder()
                .setFileExtension(YaclibModel.FileExtension.PBXPROJ_EXT)
                .setFileToWrite(InitialTemplate)
                .setFileName("project")
                .setFullDirectoryLocation("${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.xcodeproj")
                .build()

        return file
    }

    private val InitialTemplate = """// !$*UTF8*$!
{
	archiveVersion = 1;
	classes = {
	};
	objectVersion = 46;
	objects = {

/* Begin PBXBuildFile section */
		FD8755A61E5A30C400087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework in Frameworks */ = {isa = PBXBuildFile; fileRef = FD87559C1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework */; };
/* End PBXBuildFile section */

/* Begin PBXContainerItemProxy section */
		FD8755A71E5A30C400087F70 /* PBXContainerItemProxy */ = {
			isa = PBXContainerItemProxy;
			containerPortal = FD8755931E5A30C300087F70 /* Project object */;
			proxyType = 1;
			remoteGlobalIDString = FD87559B1E5A30C300087F70;
			remoteInfo = ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)};
		};
/* End PBXContainerItemProxy section */

/* Begin PBXFileReference section */
		FD87559C1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework */ = {isa = PBXFileReference; explicitFileType = wrapper.framework; includeInIndex = 0; path = ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework; sourceTree = BUILT_PRODUCTS_DIR; };
		FD8755A01E5A30C300087F70 /* Info.plist */ = {isa = PBXFileReference; lastKnownFileType = text.plist.xml; path = Info.plist; sourceTree = "<group>"; };
/* End PBXFileReference section */

/* Begin PBXFrameworksBuildPhase section */
		FD8755981E5A30C300087F70 /* Frameworks */ = {
			isa = PBXFrameworksBuildPhase;
			buildActionMask = 2147483647;
			files = (
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
		FD8755A21E5A30C400087F70 /* Frameworks */ = {
			isa = PBXFrameworksBuildPhase;
			buildActionMask = 2147483647;
			files = (
				FD8755A61E5A30C400087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework in Frameworks */,
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
/* End PBXFrameworksBuildPhase section */

/* Begin PBXGroup section */
		FD8755921E5A30C300087F70 = {
			isa = PBXGroup;
			children = (
				FD87559E1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)} */,
				FD87559D1E5A30C300087F70 /* Products */,
			);
			sourceTree = "<group>";
		};
		FD87559D1E5A30C300087F70 /* Products */ = {
			isa = PBXGroup;
			children = (
				FD87559C1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework */,
			);
			name = Products;
			sourceTree = "<group>";
		};
/* End PBXGroup section */

/* Begin PBXHeadersBuildPhase section */
		FD8755991E5A30C300087F70 /* Headers */ = {
			isa = PBXHeadersBuildPhase;
			buildActionMask = 2147483647;
			files = (
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
/* End PBXHeadersBuildPhase section */

/* Begin PBXNativeTarget section */
		FD87559B1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)} */ = {
			isa = PBXNativeTarget;
			buildConfigurationList = FD8755B01E5A30C400087F70 /* Build configuration list for PBXNativeTarget "${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}" */;
			buildPhases = (
				FD8755971E5A30C300087F70 /* Sources */,
				FD8755981E5A30C300087F70 /* Frameworks */,
				FD8755991E5A30C300087F70 /* Headers */,
				FD87559A1E5A30C300087F70 /* Resources */,
			);
			buildRules = (
			);
			dependencies = (
			);
			name = ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)};
			productName = ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)};
			productReference = FD87559C1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}.framework */;
			productType = "com.apple.product-type.framework";
		};
/* End PBXNativeTarget section */

/* Begin PBXProject section */
		FD8755931E5A30C300087F70 /* Project object */ = {
			isa = PBXProject;
			attributes = {
				LastSwiftUpdateCheck = 0820;
				LastUpgradeCheck = 0820;
				ORGANIZATIONNAME = "${projectInformation.mainDependency.authorName}";
				TargetAttributes = {
					FD87559B1E5A30C300087F70 = {
						CreatedOnToolsVersion = 8.2.1;
						DevelopmentTeam = WF4M5UWETA;
						ProvisioningStyle = Automatic;
					};
					FD8755A41E5A30C400087F70 = {
						CreatedOnToolsVersion = 8.2.1;
						DevelopmentTeam = WF4M5UWETA;
						ProvisioningStyle = Automatic;
					};
				};
			};
			buildConfigurationList = FD8755961E5A30C300087F70 /* Build configuration list for PBXProject "${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}" */;
			compatibilityVersion = "Xcode 3.2";
			developmentRegion = English;
			hasScannedForEncodings = 0;
			knownRegions = (
				en,
			);
			mainGroup = FD8755921E5A30C300087F70;
			productRefGroup = FD87559D1E5A30C300087F70 /* Products */;
			projectDirPath = "";
			projectRoot = "";
			targets = (
				FD87559B1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)} */,
			);
		};
/* End PBXProject section */

/* Begin PBXResourcesBuildPhase section */
		FD87559A1E5A30C300087F70 /* Resources */ = {
			isa = PBXResourcesBuildPhase;
			buildActionMask = 2147483647;
			files = (
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
		FD8755A31E5A30C400087F70 /* Resources */ = {
			isa = PBXResourcesBuildPhase;
			buildActionMask = 2147483647;
			files = (
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
/* End PBXResourcesBuildPhase section */

/* Begin PBXSourcesBuildPhase section */
		FD8755971E5A30C300087F70 /* Sources */ = {
			isa = PBXSourcesBuildPhase;
			buildActionMask = 2147483647;
			files = (
			);
			runOnlyForDeploymentPostprocessing = 0;
		};
/* End PBXSourcesBuildPhase section */

/* Begin PBXTargetDependency section */
		FD8755A81E5A30C400087F70 /* PBXTargetDependency */ = {
			isa = PBXTargetDependency;
			target = FD87559B1E5A30C300087F70 /* ${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)} */;
			targetProxy = FD8755A71E5A30C400087F70 /* PBXContainerItemProxy */;
		};
/* End PBXTargetDependency section */

/* Begin XCBuildConfiguration section */
		FD8755AE1E5A30C400087F70 /* Debug */ = {
			isa = XCBuildConfiguration;
			buildSettings = {
				ALWAYS_SEARCH_USER_PATHS = NO;
				CLANG_ANALYZER_NONNULL = YES;
				CLANG_CXX_LANGUAGE_STANDARD = "gnu++0x";
				CLANG_CXX_LIBRARY = "libc++";
				CLANG_ENABLE_MODULES = YES;
				CLANG_ENABLE_OBJC_ARC = YES;
				CLANG_WARN_BOOL_CONVERSION = YES;
				CLANG_WARN_CONSTANT_CONVERSION = YES;
				CLANG_WARN_DIRECT_OBJC_ISA_USAGE = YES_ERROR;
				CLANG_WARN_DOCUMENTATION_COMMENTS = YES;
				CLANG_WARN_EMPTY_BODY = YES;
				CLANG_WARN_ENUM_CONVERSION = YES;
				CLANG_WARN_INFINITE_RECURSION = YES;
				CLANG_WARN_INT_CONVERSION = YES;
				CLANG_WARN_OBJC_ROOT_CLASS = YES_ERROR;
				CLANG_WARN_SUSPICIOUS_MOVE = YES;
				CLANG_WARN_UNREACHABLE_CODE = YES;
				CLANG_WARN__DUPLICATE_METHOD_MATCH = YES;
				"CODE_SIGN_IDENTITY[sdk=iphoneos*]" = "iPhone Developer";
				COPY_PHASE_STRIP = NO;
				CURRENT_PROJECT_VERSION = ${projectInformation.mainDependency.majorVersion}.${projectInformation.mainDependency.minorVersion};
				DEBUG_INFORMATION_FORMAT = dwarf;
				ENABLE_STRICT_OBJC_MSGSEND = YES;
				ENABLE_TESTABILITY = YES;
				GCC_C_LANGUAGE_STANDARD = gnu99;
				GCC_DYNAMIC_NO_PIC = NO;
				GCC_NO_COMMON_BLOCKS = YES;
				GCC_OPTIMIZATION_LEVEL = 0;
				GCC_PREPROCESSOR_DEFINITIONS = (
					"DEBUG=1",
					"$(inherited)",
				);
				GCC_WARN_64_TO_32_BIT_CONVERSION = YES;
				GCC_WARN_ABOUT_RETURN_TYPE = YES_ERROR;
				GCC_WARN_UNDECLARED_SELECTOR = YES;
				GCC_WARN_UNINITIALIZED_AUTOS = YES_AGGRESSIVE;
				GCC_WARN_UNUSED_FUNCTION = YES;
				GCC_WARN_UNUSED_VARIABLE = YES;
				IPHONEOS_DEPLOYMENT_TARGET = 10.2;
				MTL_ENABLE_DEBUG_INFO = YES;
				ONLY_ACTIVE_ARCH = YES;
				SDKROOT = iphoneos;
				SWIFT_ACTIVE_COMPILATION_CONDITIONS = DEBUG;
				SWIFT_OPTIMIZATION_LEVEL = "-Onone";
				TARGETED_DEVICE_FAMILY = "1,2";
				VERSIONING_SYSTEM = "apple-generic";
				VERSION_INFO_PREFIX = "";
			};
			name = Debug;
		};
		FD8755AF1E5A30C400087F70 /* Release */ = {
			isa = XCBuildConfiguration;
			buildSettings = {
				ALWAYS_SEARCH_USER_PATHS = NO;
				CLANG_ANALYZER_NONNULL = YES;
				CLANG_CXX_LANGUAGE_STANDARD = "gnu++0x";
				CLANG_CXX_LIBRARY = "libc++";
				CLANG_ENABLE_MODULES = YES;
				CLANG_ENABLE_OBJC_ARC = YES;
				CLANG_WARN_BOOL_CONVERSION = YES;
				CLANG_WARN_CONSTANT_CONVERSION = YES;
				CLANG_WARN_DIRECT_OBJC_ISA_USAGE = YES_ERROR;
				CLANG_WARN_DOCUMENTATION_COMMENTS = YES;
				CLANG_WARN_EMPTY_BODY = YES;
				CLANG_WARN_ENUM_CONVERSION = YES;
				CLANG_WARN_INFINITE_RECURSION = YES;
				CLANG_WARN_INT_CONVERSION = YES;
				CLANG_WARN_OBJC_ROOT_CLASS = YES_ERROR;
				CLANG_WARN_SUSPICIOUS_MOVE = YES;
				CLANG_WARN_UNREACHABLE_CODE = YES;
				CLANG_WARN__DUPLICATE_METHOD_MATCH = YES;
				"CODE_SIGN_IDENTITY[sdk=iphoneos*]" = "iPhone Developer";
				COPY_PHASE_STRIP = NO;
				CURRENT_PROJECT_VERSION = ${projectInformation.mainDependency.majorVersion}.${projectInformation.mainDependency.minorVersion};
				DEBUG_INFORMATION_FORMAT = "dwarf-with-dsym";
				ENABLE_NS_ASSERTIONS = NO;
				ENABLE_STRICT_OBJC_MSGSEND = YES;
				GCC_C_LANGUAGE_STANDARD = gnu99;
				GCC_NO_COMMON_BLOCKS = YES;
				GCC_WARN_64_TO_32_BIT_CONVERSION = YES;
				GCC_WARN_ABOUT_RETURN_TYPE = YES_ERROR;
				GCC_WARN_UNDECLARED_SELECTOR = YES;
				GCC_WARN_UNINITIALIZED_AUTOS = YES_AGGRESSIVE;
				GCC_WARN_UNUSED_FUNCTION = YES;
				GCC_WARN_UNUSED_VARIABLE = YES;
				IPHONEOS_DEPLOYMENT_TARGET = 10.2;
				MTL_ENABLE_DEBUG_INFO = NO;
				SDKROOT = iphoneos;
				SWIFT_OPTIMIZATION_LEVEL = "-Owholemodule";
				TARGETED_DEVICE_FAMILY = "1,2";
				VALIDATE_PRODUCT = YES;
				VERSIONING_SYSTEM = "apple-generic";
				VERSION_INFO_PREFIX = "";
			};
			name = Release;
		};
		FD8755B11E5A30C400087F70 /* Debug */ = {
			isa = XCBuildConfiguration;
			buildSettings = {
				CODE_SIGN_IDENTITY = "";
				DEFINES_MODULE = YES;
				DEVELOPMENT_TEAM = WF4M5UWETA;
				DYLIB_COMPATIBILITY_VERSION = 1;
				DYLIB_CURRENT_VERSION = 1;
				DYLIB_INSTALL_NAME_BASE = "@rpath";
				INFOPLIST_FILE = swift/Source/Info.plist;
				INSTALL_PATH = "$(LOCAL_LIBRARY_DIR)/Frameworks";
				LD_RUNPATH_SEARCH_PATHS = "$(inherited) @executable_path/Frameworks @loader_path/Frameworks";
				PRODUCT_BUNDLE_IDENTIFIER = ${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name};
				PRODUCT_NAME = "$(TARGET_NAME)";
				SKIP_INSTALL = YES;
				SWIFT_VERSION = 3.0;
			};
			name = Debug;
		};
		FD8755B21E5A30C400087F70 /* Release */ = {
			isa = XCBuildConfiguration;
			buildSettings = {
				CODE_SIGN_IDENTITY = "";
				DEFINES_MODULE = YES;
				DEVELOPMENT_TEAM = WF4M5UWETA;
				DYLIB_COMPATIBILITY_VERSION = 1;
				DYLIB_CURRENT_VERSION = 1;
				DYLIB_INSTALL_NAME_BASE = "@rpath";
				INFOPLIST_FILE = swift/Source/Info.plist;
				INSTALL_PATH = "$(LOCAL_LIBRARY_DIR)/Frameworks";
				LD_RUNPATH_SEARCH_PATHS = "$(inherited) @executable_path/Frameworks @loader_path/Frameworks";
				PRODUCT_BUNDLE_IDENTIFIER = ${projectInformation.mainDependency.group}.${projectInformation.mainDependency.name};
				PRODUCT_NAME = "$(TARGET_NAME)";
				SKIP_INSTALL = YES;
				SWIFT_VERSION = 3.0;
			};
			name = Release;
		};
/* End XCBuildConfiguration section */

/* Begin XCConfigurationList section */
		FD8755961E5A30C300087F70 /* Build configuration list for PBXProject "${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}" */ = {
			isa = XCConfigurationList;
			buildConfigurations = (
				FD8755AE1E5A30C400087F70 /* Debug */,
				FD8755AF1E5A30C400087F70 /* Release */,
			);
			defaultConfigurationIsVisible = 0;
			defaultConfigurationName = Release;
		};
		FD8755B01E5A30C400087F70 /* Build configuration list for PBXNativeTarget "${SwiftUtilities.buildSwiftFullName(projectInformation.mainDependency)}" */ = {
			isa = XCConfigurationList;
			buildConfigurations = (
				FD8755B11E5A30C400087F70 /* Debug */,
				FD8755B21E5A30C400087F70 /* Release */,
			);
			defaultConfigurationIsVisible = 0;
		};
/* End XCConfigurationList section */
	};
	rootObject = FD8755931E5A30C300087F70 /* Project object */;
}

"""
}