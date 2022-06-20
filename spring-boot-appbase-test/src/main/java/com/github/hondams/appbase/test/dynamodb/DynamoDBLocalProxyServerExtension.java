package com.github.hondams.appbase.test.dynamodb;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.github.hondams.appbase.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
public class DynamoDBLocalProxyServerExtension
        implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    private static ThreadLocal<Class<?>> testClassHolder = new ThreadLocal<>();

    private static Map<Class<?>, DynamoDBProxyServer> proxyServerMap = new ConcurrentHashMap<>();

    private static Map<Class<?>, Map<String, AmazonDynamoDB>> amazonDynamoDBMapMap =
            new ConcurrentHashMap<>();

    private static Map<Class<?>, Map<Region, DynamoDbClient>> dynamoDbClientMapMap =
            new ConcurrentHashMap<>();

    private static Map<Class<?>, String[]> proxyServerArgsMap = new ConcurrentHashMap<>();

    public static void setProxyServerArgs(String[] proxyServerArgs) {

        Class<?> testClass = testClassHolder.get();
        if (testClass == null) {
            throw new SystemException("not found testClass.");
        }

        DynamoDBProxyServer proxyServer = proxyServerMap.get(testClass);
        if (proxyServer != null) {
            throw new SystemException("ProxyServer is already started. setting on BeforeAll.");
        }
        proxyServerArgsMap.put(testClass, proxyServerArgs);
    }

    public static String[] getProxyServerArgs() {

        Class<?> testClass = testClassHolder.get();
        if (testClass == null) {
            throw new SystemException("not found testClass.");
        }

        String[] proxyServerArgs = proxyServerArgsMap.get(testClass);
        if (proxyServerArgs == null) {
            String[] defaultProxyServerArgs = {"-inMemory"};
            proxyServerArgs = defaultProxyServerArgs;
        }
        return proxyServerArgs;
    }

    public static String getProxyServerPort() {

        boolean foundPortArg = false;
        for (String proxyServerArg : getProxyServerArgs()) {
            if (foundPortArg) {
                return proxyServerArg;
            } else {
                if ("-port".equals(proxyServerArg)) {
                    foundPortArg = true;
                }
            }
        }
        return "8000";
    }

    public static AmazonDynamoDB getAmazonDynamoDB(String region) {

        Class<?> testClass = testClassHolder.get();
        if (testClass == null) {
            throw new SystemException("not found testClass.");
        }

        Map<String, AmazonDynamoDB> amazonDynamoDBMap =
                amazonDynamoDBMapMap.computeIfAbsent(testClass, k -> new ConcurrentHashMap<>());
        AmazonDynamoDB amazonDynamoDB = amazonDynamoDBMap.get(region);
        if (amazonDynamoDB == null) {
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()//
                    .withEndpointConfiguration(//
                            new AwsClientBuilder.EndpointConfiguration(//
                                    "http://localhost:" + getProxyServerPort(), //
                                    region))
                    .build();
            amazonDynamoDBMap.put(region, amazonDynamoDB);
        }
        return amazonDynamoDB;
    }

    public static DynamoDbClient getDynamoDbClient(Region region) {

        Class<?> testClass = testClassHolder.get();
        if (testClass == null) {
            throw new SystemException("not found testClass.");
        }

        Map<Region, DynamoDbClient> dynamoDbClientMap =
                dynamoDbClientMapMap.computeIfAbsent(testClass, k -> new ConcurrentHashMap<>());
        DynamoDbClient dynamoDbClient = dynamoDbClientMap.get(region);
        if (dynamoDbClient == null) {
            AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider
                    .create(AwsBasicCredentials.create("dummy-key", "dummy-secret"));
            dynamoDbClient = DynamoDbClient.builder()//
                    .endpointOverride(URI.create("http://localhost:" + getProxyServerPort()))//
                    // The region is meaningless for local DynamoDb but required for client builder
                    // validation
                    .region(region)//
                    .credentialsProvider(credentialsProvider)//
                    .build();
            dynamoDbClientMap.put(region, dynamoDbClient);
        }
        return dynamoDbClient;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        DynamoDBLocalUtils.tryInitializeLibraryPath();

        Class<?> testClass = context.getRequiredTestClass();
        testClassHolder.set(testClass);

        DynamoDBProxyServer proxyServer = proxyServerMap.get(testClass);
        if (proxyServer == null) {
            String[] proxyServerArgs = getProxyServerArgs();
            proxyServer = ServerRunner.createServerFromCommandLineArgs(proxyServerArgs);
            log.info("starting DynamoDBLocal ProxyServer. proxyServerArgs={}",
                    Arrays.asList(proxyServerArgs));
            proxyServer.start();
            log.info("started DynamoDBLocal ProxyServer");
            proxyServerMap.put(testClass, proxyServer);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        testClassHolder.remove();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        Class<?> testClass = context.getRequiredTestClass();

        Map<String, AmazonDynamoDB> amazonDynamoDBMap = amazonDynamoDBMapMap.remove(testClass);
        if (amazonDynamoDBMap != null) {
            for (AmazonDynamoDB amazonDynamoDB : amazonDynamoDBMap.values()) {
                amazonDynamoDB.shutdown();
            }
        }
        Map<Region, DynamoDbClient> dynamoDbClientMap = dynamoDbClientMapMap.remove(testClass);
        if (dynamoDbClientMap != null) {
            for (DynamoDbClient dynamoDbClient : dynamoDbClientMap.values()) {
                dynamoDbClient.close();
            }
        }

        DynamoDBProxyServer proxyServer = proxyServerMap.remove(testClass);
        if (proxyServer != null) {
            log.info("stoping DynamoDBLocal ProxyServer");
            proxyServer.stop();
            log.info("stoped DynamoDBLocal ProxyServer");
        }
    }
}
