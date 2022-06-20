package com.github.hondams.appbase.test.dynamodb;

import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;
import com.github.hondams.appbase.exception.SystemException;
import com.github.hondams.appbase.util.AppBaseClassPathUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamoDBLocalUtils {

    private static final ReentrantLock lock = new ReentrantLock();

    private static volatile boolean initializedLibraryPath = false;

    public static void tryInitializeLibraryPath() {

        if (!initializedLibraryPath) {
            lock.lock();
            try {
                if (!initializedLibraryPath) {
                    initializeLibraryPath();
                    initializedLibraryPath = true;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private static void initializeLibraryPath() {

        Path sqlite4javaJar = AppBaseClassPathUtils
                .getRequiredClassPathJar("sqlite4java-\\d+\\.\\d+\\.\\d+\\.jar");
        Path sqlite4javaLibraryPath = toSqlite4javaLibraryPath(sqlite4javaJar);
        AppBaseClassPathUtils.addLibraryPath(sqlite4javaLibraryPath.toString());
    }

    private static Path toSqlite4javaLibraryPath(Path sqlite4javaJar) {
        // MavenのJar構成を想定。
        String version = sqlite4javaJar.getParent().getFileName().toString();
        String orgArtifactId = sqlite4javaJar.getParent().getParent().getFileName().toString();
        Path groupPath = sqlite4javaJar.getParent().getParent().getParent();

        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        String artifactId;
        if (osName.startsWith("Windows")) {
            switch (osArch) {
                case "amd64":
                    artifactId = orgArtifactId + "-win32-x64";
                    break;
                default:
                    throw new SystemException(
                            "not supported. osName=" + osName + ", osArch=" + osArch);
            }
        } else {
            throw new SystemException("not supported. osName=" + osName + ", osArch=" + osArch);
        }

        return groupPath.resolve(artifactId).resolve(version);
    }

}
