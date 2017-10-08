package org.roylance.yaclib.core.services.swift

import org.roylance.common.service.IBuilder
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.utilities.StringUtilities
import org.roylance.yaclib.core.utilities.SwiftUtilities

class SwiftServiceImplementationBuilder(
    private val mainDependency: YaclibModel.Dependency,
    private val controller: YaclibModel.Controller) : IBuilder<YaclibModel.File> {
  override fun build(): YaclibModel.File {
    val workspace = StringBuilder()
    val interfaceName = StringUtilities.convertServiceNameToInterfaceName(controller)

    val initialTemplate = """${CommonTokens.DoNotAlterMessage}
import Foundation
import Alamofire
import SwiftProtobuf

public class ${controller.name}${CommonTokens.ServiceName}: $interfaceName {
    let baseUrl: String
    public init(baseUrl: String) {
        self.baseUrl = baseUrl
    }
"""
    workspace.append(initialTemplate)

    controller.actionsList.forEach { action ->
      // for now, only processing one input and one output
      if (action.inputsCount == 1) {
        val colonSeparatedInputs = action.inputsList.map { input ->
          "${input.argumentName}: ${SwiftUtilities.buildSwiftFullName(input)}"
        }.joinToString()

        val actionTemplate = "\tpublic func ${action.name}($colonSeparatedInputs, onSuccess: @escaping (_ response: ${SwiftUtilities.buildSwiftFullName(
            action.output).trim()}) -> Void, onError: @escaping (_ response: String) -> Void) {\n"
        val fullUrl = StringUtilities.buildUrl("/rest/${controller.name}/${action.name}")
        val functionTemplate = """
            do {
                let serializedRequest = try ${action.inputsList.first().argumentName}.serializedData()
                var urlRequest = URLRequest(url: URL(string: self.baseUrl + "$fullUrl")!)
                urlRequest.httpMethod = HTTPMethod.post.rawValue
                urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
                urlRequest.httpBody = serializedRequest.base64EncodedData()

                Alamofire.request(urlRequest)
                    .response { alamoResponse in
                        let base64String = String(data: alamoResponse.data!, encoding: String.Encoding.utf8)
                        let decodedData = Data(base64Encoded: base64String!)!
                        do {
                            let actualResponse = try ${SwiftUtilities.buildSwiftFullName(
            action.output)}(serializedData: decodedData)
                            onSuccess(actualResponse)
                        }
                        catch {
                            onError("\(error)")
                        }
                    }
                }
            catch {
                onError("\(error)")
            }
"""
        workspace.append(actionTemplate)
        workspace.append(functionTemplate)
        workspace.appendln("\t}")
      }
    }

    workspace.append("}")

    val returnFile = YaclibModel.File.newBuilder()
        .setFileToWrite(workspace.toString())
        .setFileExtension(YaclibModel.FileExtension.SWIFT_EXT)
        .setFileName("${controller.name}${CommonTokens.ServiceName}")
        .setFullDirectoryLocation("${mainDependency.name}${CommonTokens.SwiftSuffix}/Source")
        .build()

    return returnFile
  }
}