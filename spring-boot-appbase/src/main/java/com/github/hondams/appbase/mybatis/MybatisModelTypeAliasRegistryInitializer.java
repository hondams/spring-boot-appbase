package com.github.hondams.appbase.mybatis;

import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import com.github.hondams.appbase.config.AppBaseMybatisProperties;
import com.github.hondams.appbase.model.MybatisModel;
import com.github.hondams.appbase.util.ClassResourceUtils;

public class MybatisModelTypeAliasRegistryInitializer {

    // SqlSessionFactoryでは、@MappedTypes、TypeReference<T>を利用しない、TypeHandlerの登録は困難。
    // SqlSessionFactoryBeanCustomizerによる、TypeHandlerの登録を断念。

    public MybatisModelTypeAliasRegistryInitializer(AppBaseMybatisProperties properties,
            ObjectProvider<List<SqlSessionFactory>> sqlSessionFactoriesProvider) {

        List<SqlSessionFactory> sqlSessionFactories = sqlSessionFactoriesProvider.getIfAvailable();
        if (sqlSessionFactories != null) {
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
                TypeAliasRegistry typeAliasRegistry =
                        sqlSessionFactory.getConfiguration().getTypeAliasRegistry();
                registerTypeAliases(properties, typeAliasRegistry);
            }
        }
    }

    private void registerTypeAliases(AppBaseMybatisProperties properties,
            TypeAliasRegistry typeAliasRegistry) {

        if (properties.getBasePackages().isEmpty()) {
            Resource[] resources = ClassResourceUtils.getAllClassResources();
            registerTypeAliases(resources, typeAliasRegistry);
        } else {
            for (String basePackage : properties.getBasePackages()) {
                Resource[] resources = ClassResourceUtils.getClassResources(basePackage);
                registerTypeAliases(resources, typeAliasRegistry);
            }
        }
    }

    private void registerTypeAliases(Resource[] resources, TypeAliasRegistry typeAliasRegistry) {

        for (Resource resource : resources) {
            AnnotationMetadata annotationMetadata =
                    ClassResourceUtils.getAnnotationMetadata(resource);
            if (ClassResourceUtils.hasAnnotation(annotationMetadata, MybatisModel.class)) {
                Class<?> modelClass = ClassResourceUtils.getClass(annotationMetadata);
                typeAliasRegistry.registerAlias(modelClass.getSimpleName(), modelClass);
            }
        }
    }
}
