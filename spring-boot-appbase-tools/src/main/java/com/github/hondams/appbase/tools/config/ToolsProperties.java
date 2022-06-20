package com.github.hondams.appbase.tools.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "tools")
public class ToolsProperties {

    private List<String> inputDirs;

    private String templateDir;

    private String outputDir;

    private String databaseType;
}
