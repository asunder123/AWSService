package com.awssdjava.awsdemo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.Response;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
@Slf4j
public class AWSDynamoDBConfiguration {

	static AmazonDynamoDB client =  AmazonDynamoDBClientBuilder.standard().build();
	static DynamoDB dynamoDB = new DynamoDB(client);
	
	public PutItemOutcome createDataAmazinDynDb(String metric,Number value) {
		Map<String,String> uriVariables = new HashMap<>();
		ResponseEntity<SpringCloudMetricsData> response = new RestTemplate().getForEntity("http://localhost:8888/admin/metrics",SpringCloudMetricsData.class,uriVariables);
		Item item = new Item();
		item.withString("MetricName",metric);
		item.withNumber("MetricValue",value);
		item.withString("MetricName","Heap");
		item.withNumber("MetricValue", response.getBody().getHeap());
		item.withString("MetricName","Processors");
		item.withNumber("MetricValue", response.getBody().getProcessors());
		item.withString("MetricName","Uptime");
		item.withNumber("MetricValue", response.getBody().getUptime());
		return dynamoDB.getTable("MetricCollection").putItem(item);
   
		
	}
	
	public Table createschema( String tableName, String attrone,String attrtwo,String scalartypeone, String scalartypetwo) {
		Table table  = new Table(client, tableName);
	    try {
	    	ScalarAttributeType Type;
            System.out.println("Attempting to create table; please wait...");
            table = dynamoDB.createTable(tableName,
                Arrays.asList(new KeySchemaElement(attrone, KeyType.HASH),
                   new KeySchemaElement(attrtwo, KeyType.RANGE)), 
               Arrays.asList(new AttributeDefinition(attrone, ScalarAttributeType.fromValue(scalartypeone)),
                    new AttributeDefinition(attrtwo, ScalarAttributeType.fromValue(scalartypetwo))),
                new ProvisionedThroughput(10L, 10L));
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

        }
        catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
		return table;
	}
	
	public String updateschema(String tableName,String fieldone,String fieldtwo,String valueone,String valuetwo) {
		

		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
		PutItemRequest request = new PutItemRequest();
		log.info("Setting table name " );
		request.setTableName(tableName);
		log.info("Table to be updated is "+ request);
		  request.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
		  log.info("Request Id " +dynamoDB.listTables().getSdkResponseMetadata());

		  
		  request.setReturnValues(ReturnValue.ALL_OLD) ;
		 
		  log.info("Table update request is now " + request );
		  Map<String, AttributeValue> map = new HashMap<>();
		    map.put(fieldone, new AttributeValue(valueone));     
	        map.put(fieldtwo, (new AttributeValue()).withN(valuetwo));
	        request.setItem(map);
	        try {
	            /* Send Put Item Request */
	            PutItemResult result = dynamoDB.putItem(request);
	             
	            System.out.println("Status : " + result.getSdkHttpMetadata().getHttpStatusCode());
	             
	            System.out.println("Consumed Capacity : " + result.getConsumedCapacity().getCapacityUnits());
	             
	            if(result.getAttributes() != null) {
	                result.getAttributes().entrySet().stream()
	                    .forEach( e -> System.out.println(e.getKey() + " " + e.getValue()));  
	            }
	             
	        } catch (AmazonServiceException e) {
	             
	            System.out.println(e.getErrorMessage());
	             
	        }
		return tableName;
		
		
	}
	
	
	public String deletetable(String tableName) {
      Table table = dynamoDB.getTable(tableName);
      table.delete();
	return "Deleted" + tableName;
	}

}
