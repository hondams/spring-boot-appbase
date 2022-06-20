package com.github.hondams.appbase.tools.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsUtils {

    public static Path changeExtension(Path path, String extension) {
        String fileName =
                FilenameUtils.getExtension(path.getFileName().toString()) + "." + extension;
        return path.getParent().resolve(fileName);
    }

    public static List<Path> getFilePaths(Path basePath, String extension) throws IOException {

        try (Stream<Path> paths = Files.walk(basePath)) {
            return paths.filter(p -> !Files.isDirectory(p))//
                    .filter(p -> StringUtils.equalsIgnoreCase(
                            FilenameUtils.getExtension(p.getFileName().toString()), extension))//
                    .map(Path::toAbsolutePath)//
                    .filter(p -> !p.toString().contains("\\bak\\"))//
                    .collect(Collectors.toList());
        }
    }

    public static Charset getDetectedCharset(Path path) throws IOException {

        try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)) {
            UniversalDetector detector = new UniversalDetector(null);

            byte[] bytes = new byte[1024];

            int length = inputStream.read(bytes);
            while (0 < length) {
                detector.handleData(bytes, 0, length);
                if (detector.isDone()) {
                    break;
                }
                length = inputStream.read(bytes);
            }

            detector.dataEnd();
            String detectedCharset = detector.getDetectedCharset();
            if (detectedCharset == null) {
                return StandardCharsets.US_ASCII;
            }
            switch (detectedCharset) {
                case "UTF-8":
                    return StandardCharsets.UTF_8;
                case "UTF-16":
                    return StandardCharsets.UTF_16;
                default:
                    throw new ToolsException(
                            "not supported charset. charset=" + detectedCharset);
            }
        }
    }

    public static int getLineCount(String text) {
        int lineCount = 0;
        boolean cr = false;
        for (char ch : text.toCharArray()) {
            switch (ch) {
                case '\r':
                    cr = true;
                    lineCount++;
                    break;
                case '\n':
                    if (!cr) {
                        lineCount++;
                    }
                    cr = false;
                    break;
                default:
                    cr = false;
                    break;
            }
        }
        return lineCount;
    }
}
