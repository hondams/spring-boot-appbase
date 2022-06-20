package com.github.hondams.appbase.mybatis;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;

public class CodeEnumTypeHandlerRegistryInitializer implements SqlSessionFactoryBeanCustomizer {

    @Override
    public void customize(SqlSessionFactoryBean factoryBean) {
        factoryBean.setDefaultEnumTypeHandler(CodeEnumTypeHandler.class);
    }
}
