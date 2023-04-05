package com.redhat;

import org.apache.camel.Configuration;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FTPRoute extends RouteBuilder {

    @ConfigProperty(name = "ftp.ipaddress")
    String ftpIPAddress;

    @ConfigProperty(name = "ftp.username")
    String ftpUserName;

    @ConfigProperty(name = "ftp.password")
    String ftpPassword;

    String ftpString;

    @Override
    public void configure() {
        from(ftpString)
                .log(LoggingLevel.INFO, "processing file ${file:name}")
                .split(body().convertToString().tokenize("\n"))
                .setHeader(Exchange.FILE_NAME, body())
                .to("kafka:greetings");
    }

    @PostConstruct
    void buildString() {
        ftpString = new StringBuilder("ftp://")
                .append(ftpUserName)
                .append("@")
                .append(ftpIPAddress)
                .append("/?password=")
                .append(ftpPassword).toString();
    }
}
