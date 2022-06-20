package com.github.hondams.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import com.github.hondams.appbase.model.CodeEnum;
import com.github.hondams.appbase.model.MybatisModel;
import com.github.hondams.appbase.util.ClassResourceUtils;
import com.github.hondams.util.testmodel.TestEnum;
import com.github.hondams.util.testmodel.TestMyBatisModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ClassResourceUtilsTest {

    @Test
    void test_getClassResources() throws IOException {
        Resource[] resources =
                ClassResourceUtils.getClassResources("com.github.hondams.util.testmodel");
        Assertions.assertEquals(2, resources.length);
        Set<String> resourceFileNames =
                Stream.of(resources).map(Resource::getFilename).collect(Collectors.toSet());
        Assertions.assertTrue(resourceFileNames.contains("TestEnum.class"));
        Assertions.assertTrue(resourceFileNames.contains("TestMyBatisModel.class"));

        for (Resource resource : resources) {
            AnnotationMetadata annotationMetadata =
                    ClassResourceUtils.getAnnotationMetadata(resource);
            log.info("{}", annotationMetadata.getClassName());
            log.info("    EnclosingClassName={}", annotationMetadata.getEnclosingClassName());
            // TODO:extract
            int i = 0;
            for (MergedAnnotation<?> annotation : annotationMetadata.getAnnotations()) {
                log.info("    Annotations[{}].AggregateIndex={}", i,
                        annotation.getAggregateIndex());
                log.info("    Annotations[{}].Distance={}", i, annotation.getDistance());
                log.info("    Annotations[{}].MetaSource={}", i, annotation.getMetaSource());
                log.info("    Annotations[{}].MetaTypes={}", i, annotation.getMetaTypes());
                log.info("    Annotations[{}].Root={}", i, annotation.getRoot());
                log.info("    Annotations[{}].Source={}", i, annotation.getSource());
                log.info("    Annotations[{}].Type={}", i, annotation.getType());
                i++;
            }
            log.info("    AnnotationTypes={}", annotationMetadata.getAnnotationTypes());
            log.info("    InterfaceNames={}",
                    Arrays.asList(annotationMetadata.getInterfaceNames()));
            log.info("    SuperClassName={}", annotationMetadata.getSuperClassName());
            log.info("    MemberClassNames={}",
                    Arrays.asList(annotationMetadata.getMemberClassNames()));
        }
    }

    @Test
    void test_isEnum_not() throws IOException {
        Resource resource = ClassResourceUtils.getClassResource(TestMyBatisModel.class);
        AnnotationMetadata annotationMetadata = ClassResourceUtils.getAnnotationMetadata(resource);
        Assertions.assertFalse(ClassResourceUtils.isEnum(annotationMetadata));
    }

    @Test
    void test_hasInterface() throws IOException {
        Resource resource = ClassResourceUtils.getClassResource(TestEnum.class);
        AnnotationMetadata annotationMetadata = ClassResourceUtils.getAnnotationMetadata(resource);
        Assertions.assertTrue(ClassResourceUtils.hasInterface(annotationMetadata, CodeEnum.class));
    }

    @Test
    void test_hasInterface_not() throws IOException {
        Resource resource = ClassResourceUtils.getClassResource(TestMyBatisModel.class);
        AnnotationMetadata annotationMetadata = ClassResourceUtils.getAnnotationMetadata(resource);
        Assertions.assertFalse(ClassResourceUtils.hasInterface(annotationMetadata, CodeEnum.class));
    }

    @Test
    void test_hasAnnotation() throws IOException {
        Resource resource = ClassResourceUtils.getClassResource(TestMyBatisModel.class);
        AnnotationMetadata annotationMetadata = ClassResourceUtils.getAnnotationMetadata(resource);
        Assertions.assertTrue(
                ClassResourceUtils.hasAnnotation(annotationMetadata, MybatisModel.class));
    }

    @Test
    void test_hasAnnotation_not() throws IOException {
        Resource resource = ClassResourceUtils.getClassResource(TestEnum.class);
        AnnotationMetadata annotationMetadata = ClassResourceUtils.getAnnotationMetadata(resource);
        Assertions.assertFalse(
                ClassResourceUtils.hasAnnotation(annotationMetadata, MybatisModel.class));
    }
}
