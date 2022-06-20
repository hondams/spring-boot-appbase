package com.github.hondams.appbase.util;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import com.github.hondams.appbase.exception.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AppBaseClassPathUtils {

    public static List<Path> getClassPaths() {
        List<Path> classPaths = new ArrayList<>();
        String classPathsText = System.getProperty("java.class.path");
        if (classPathsText != null) {
            String[] classPathTexts = classPathsText.split(File.pathSeparator);
            for (String classPathText : classPathTexts) {
                classPaths.add(Path.of(classPathText).toAbsolutePath());
            }
        }
        return classPaths;
    }

    public static Path getRequiredClassPathJar(String jarNamePattern) {
        Path classPathJar = getClassPathJar(jarNamePattern);
        if (classPathJar == null) {
            throw new SystemException("not found classpath jar. jarNamePattern=" + jarNamePattern
                    + ", classpath=" + System.getProperty("java.class.path"));
        }
        return classPathJar;
    }

    public static Path getClassPathJar(String jarNamePattern) {

        Pattern pattern = Pattern.compile("^" + jarNamePattern + "$");
        for (Path classPath : getClassPaths()) {
            if (!Files.isDirectory(classPath)) {
                String jarName = classPath.getFileName().toString();
                if (pattern.matcher(jarName).find()) {
                    return classPath;
                }
            }
        }
        return null;
    }

    public static void addLibraryPath(String libraryPath) {

        try {
            Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
            usrPathsField.setAccessible(true);

            String[] paths = (String[]) usrPathsField.get(null);

            for (String path : paths) {
                if (path.equals(libraryPath)) {
                    return;
                }
            }

            String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
            newPaths[newPaths.length - 1] = libraryPath;
            usrPathsField.set(null, newPaths);
            log.warn("addLibraryPath forcibly. libraryPath={}", libraryPath);
        } catch (NoSuchFieldException e) {
            throw new SystemException(e);
        } catch (IllegalArgumentException e) {
            throw new SystemException(e);
        } catch (IllegalAccessException e) {
            throw new SystemException(e);
        } catch (SecurityException e) {
            throw new SystemException(e);
        }

        // System.setProperty("java.library.path", currentLibraryPath)では、変更できない。
        // String currentLibraryPath = System.getProperty("java.library.path");
        // if (currentLibraryPath == null) {
        // currentLibraryPath = libraryPath;
        // } else {
        // currentLibraryPath = currentLibraryPath + File.pathSeparator + libraryPath;
        // }
        // System.setProperty("java.library.path", currentLibraryPath);
    }

}
