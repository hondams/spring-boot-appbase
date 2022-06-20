package com.github.hondams.appbase.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.boot.type.classreading.ConcurrentReferenceCachingMetadataReaderFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import com.github.hondams.appbase.exception.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassResourceUtils {

    private static final PathMatchingResourcePatternResolver resourcePatternResolver =
            new PathMatchingResourcePatternResolver();

    private static final ConcurrentReferenceCachingMetadataReaderFactory metadataReaderFactory =
            new ConcurrentReferenceCachingMetadataReaderFactory();

    public static Resource[] getAllClassResources() {
        try {
            return resourcePatternResolver.getResources(//
                    ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX//
                            + "/**/*" + ClassUtils.CLASS_FILE_SUFFIX);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static Resource[] getClassResources(String basePackage) {
        try {
            return resourcePatternResolver.getResources(//
                    ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX//
                            + "/" + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*"
                            + ClassUtils.CLASS_FILE_SUFFIX);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static Resource getClassResource(Class<?> searchingClass) throws IOException {
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(//
                    ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX//
                            + "/"
                            + ClassUtils.convertClassNameToResourcePath(searchingClass.getName())
                            + ClassUtils.CLASS_FILE_SUFFIX);
        } catch (IOException e) {
            throw new SystemException(e);
        }
        switch (resources.length) {
            case 0:
                return null;
            case 1:
                return resources[0];
            default:
                throw new SystemException(
                        "duplicated class resource. resources=" + Arrays.asList(resources));
        }
    }

    public static AnnotationMetadata getAnnotationMetadata(Resource resource) {
        try {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
            return metadataReader.getAnnotationMetadata();
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static boolean isEnum(ClassMetadata classMetadata) {
        return Enum.class.getName().equals(classMetadata.getSuperClassName());
    }

    public static boolean hasInterface(ClassMetadata classMetadata, Class<?> searchingInterface) {
        String searchingInterfaceName = searchingInterface.getName();
        for (String interfaceName : classMetadata.getInterfaceNames()) {
            if (Objects.equals(interfaceName, searchingInterfaceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnnotation(AnnotationMetadata classMetadata,
            Class<?> searchingAnnotation) {

        return classMetadata.hasAnnotation(searchingAnnotation.getName());
    }

    public static Class<?> getClass(AnnotationMetadata annotationMetadata) {
        try {
            return Class.forName(annotationMetadata.getClassName());
        } catch (ClassNotFoundException e) {
            throw new SystemException(e);
        }
    }
}
