package com.github.hondams.appbase.test.dynamodb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.github.hondams.appbase.exception.SystemException;

public class DynamoDBLocalEmbeddedExtension
        implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    private static ThreadLocal<Class<?>> testClassHolder = new ThreadLocal<>();

    private static Map<Class<?>, AmazonDynamoDB> amazonDynamoDBMap = new ConcurrentHashMap<>();

    public static AmazonDynamoDB getAmazonDynamoDB() {

        Class<?> testClass = testClassHolder.get();
        if (testClass == null) {
            throw new SystemException("not found testClass.");
        }

        AmazonDynamoDB amazonDynamoDB = amazonDynamoDBMap.get(testClass);
        if (amazonDynamoDB == null) {
            throw new SystemException("not found amazonDynamoDB. testClass=" + testClass.getName());
        }
        return amazonDynamoDB;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        DynamoDBLocalUtils.tryInitializeLibraryPath();

        Class<?> testClass = context.getRequiredTestClass();
        testClassHolder.set(testClass);

        AmazonDynamoDB amazonDynamoDB = amazonDynamoDBMap.get(testClass);
        if (amazonDynamoDB == null) {
            amazonDynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
            amazonDynamoDBMap.put(testClass, amazonDynamoDB);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        testClassHolder.remove();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        Class<?> testClass = context.getRequiredTestClass();

        AmazonDynamoDB amazonDynamoDB = amazonDynamoDBMap.remove(testClass);
        if (amazonDynamoDB != null) {
            amazonDynamoDB.shutdown();
        }
    }
}
