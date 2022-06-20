package com.github.hondams.appbase.tools.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ToolsProperties.class)
public class ToolsConfig {

}
