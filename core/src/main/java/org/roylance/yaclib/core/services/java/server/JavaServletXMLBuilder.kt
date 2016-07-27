package org.roylance.yaclib.core.services.java.server

import org.roylance.yaclib.Models
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.IBuilder

class JavaServletXMLBuilder(): IBuilder<Models.File> {
    private val ActualXML = """<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    <display-name>${CommonTokens.ServerApi}</display-name>

    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>

    <servlet>
        <servlet-name>javax.ws.rs.core.Application</servlet-name>
        <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
        <async-supported>true</async-supported>
    </servlet>

    <servlet-mapping>
        <servlet-name>javax.ws.rs.core.Application</servlet-name>
        <url-pattern>/rest/*</url-pattern>
    </servlet-mapping>
</web-app>
"""
    override fun build(): Models.File {
        return Models.File.newBuilder()
            .setFileToWrite(ActualXML)
            .setFileName("web")
            .setFileUpdateType(Models.FileUpdateType.WRITE_IF_NOT_EXISTS)
            .setFileExtension(Models.FileExtension.XML_EXT)
            .setFullDirectoryLocation("src/main/webapp/WEB-INF")
            .build()
    }
}