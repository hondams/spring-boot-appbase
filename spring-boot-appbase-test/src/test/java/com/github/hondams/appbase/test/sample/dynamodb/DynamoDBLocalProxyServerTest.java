package com.github.hondams.appbase.test.sample.dynamodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.hondams.appbase.test.dynamodb.DynamoDBLocalProxyServerExtension;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;

@ExtendWith(DynamoDBLocalProxyServerExtension.class)
@Slf4j
class DynamoDBLocalProxyServerTest {

    @Test
    void test() {
        log.info("test");

        DynamoDBLocalProxyServerExtension.getAmazonDynamoDB("us-west-2");
        DynamoDBLocalProxyServerExtension.getDynamoDbClient(Region.US_WEST_2);
        // dynamoDB2.cre
        //
        //
        //
        // List<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        // attributeDefinitions.add(new
        // AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
        //
        // List<KeySchemaElement> keySchema = new ArrayList<>();
        // keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
        //
        // CreateTableRequest request = new CreateTableRequest()
        // .withTableName(tableName)
        // .withKeySchema(keySchema)
        // .withAttributeDefinitions(attributeDefinitions)
        // .withProvisionedThroughput(new ProvisionedThroughput()
        // .withReadCapacityUnits(5L)
        // .withWriteCapacityUnits(6L));
        //
        // Table table = dynamoDB.createTable(request);
        //
        // table.waitForActive();
        // CreateTableRequest request = CreateTableRequest.builder().;
        // dynamoDb.createTable(null);
        //
        // tables = dynamoDb.listTables();
        // assertEquals(0, tables.getTableNames().size());

    }
}
