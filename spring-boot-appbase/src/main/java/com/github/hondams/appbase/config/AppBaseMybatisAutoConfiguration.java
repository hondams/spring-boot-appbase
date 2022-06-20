package com.github.hondams.appbase.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.hondams.appbase.mybatis.CodeEnumTypeHandlerRegistryInitializer;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@EnableConfigurationProperties(AppBaseMybatisProperties.class)
public class AppBaseMybatisAutoConfiguration {

    @Bean
    public CodeEnumTypeHandlerRegistryInitializer codeEnumTypeHandlerRegistryInitializer() {
        return new CodeEnumTypeHandlerRegistryInitializer();
    }
}
