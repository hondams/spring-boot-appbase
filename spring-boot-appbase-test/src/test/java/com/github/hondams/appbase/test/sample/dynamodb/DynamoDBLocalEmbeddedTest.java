package com.github.hondams.appbase.test.sample.dynamodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.github.hondams.appbase.test.dynamodb.DynamoDBLocalEmbeddedExtension;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(DynamoDBLocalEmbeddedExtension.class)
@Slf4j
class DynamoDBLocalEmbeddedTest {

    @Test
    void test() {
        AmazonDynamoDB client = DynamoDBLocalEmbeddedExtension.getAmazonDynamoDB();
        DynamoDB dynamoDB = new DynamoDB(client);
        assertEquals(0, client.listTables().getTableNames().size());
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
