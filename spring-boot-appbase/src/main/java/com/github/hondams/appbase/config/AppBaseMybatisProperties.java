package com.github.hondams.appbase.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "appbase.mybatis")
@Data
public class AppBaseMybatisProperties {

    private List<String> basePackages = new ArrayList<>();
}
