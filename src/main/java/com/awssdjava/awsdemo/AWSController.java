package com.awssdjava.awsdemo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.GroupIdentifier;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceNetworkInterface;
import com.amazonaws.services.ec2.model.InstanceNetworkInterfaceAssociation;
import com.amazonaws.services.ec2.model.Monitoring;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.S3Configuration.Builder;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@RestController
public class AWSController {

	@Autowired
	private AWSEC2Configuration awsEC2Configuration ; 
	
	@Autowired
	private AWSDynamoDBConfiguration aWSDynamoDBConfiguration;
	
	@Autowired
	private AWSS3Configuration aWSS3Configuration;
	
	@GetMapping("/ec2sdkhttpmetadata")
	public SdkHttpMetadata getresult() {
		return awsEC2Configuration.getEc2InstanceStatusResult().getSdkHttpMetadata();
		
	}
	
	@GetMapping("/ec2respmetadata")
	public ResponseMetadata getresultmetadata() {
		return awsEC2Configuration.getEc2InstanceStatusResult().getSdkResponseMetadata();
		
	}
	
	@GetMapping("/ec2monitoring")
	public Monitoring getmonitoringstate() {
		return awsEC2Configuration.getEc2InstanceStatusResult().getReservations().get(0).getInstances().get(0).getMonitoring();
		
	}
	
	@GetMapping("/ec2ipaddress")
	public String getipaddress() {
		return awsEC2Configuration.getEc2InstanceStatusResult().getReservations().get(0).getInstances().get(0).getPublicIpAddress();
		
	}
	
	@GetMapping("/ec2networkinterfacesassociation")
	public InstanceNetworkInterfaceAssociation getninterfaces() {
		return awsEC2Configuration.getEc2InstanceStatusResult().getReservations().get(0).getInstances().get(0).getNetworkInterfaces().get(0).getAssociation();
		
	}
	

	
	@PostMapping("/s3createbucket")
	public com.amazonaws.services.s3.model.Bucket getbuckets(@RequestHeader String bucketname)  {
		 return aWSS3Configuration.createbuckets(bucketname);
	}
	
	@DeleteMapping("/s3deletebucket")
	public  ResponseEntity<String> deletebuckets(@RequestHeader String bucketname)
	{  
		aWSS3Configuration.deletebuckets(bucketname);
		System.out.println("Deleted bucket is " + bucketname);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PostMapping("/ec2crmonitoredinst")
	public ResponseEntity<Object> createInstanceMonitoring(@RequestHeader String instanceid) {
		 awsEC2Configuration.createinstmonitoring(instanceid);
		return ResponseEntity.status(201).build();
		
	}
	
	@PostMapping("/crdyndatametrics")
	public ResponseEntity<String> crdynamodbdata(@RequestHeader String metric, @RequestHeader Number value) {
		
		aWSDynamoDBConfiguration.createDataAmazinDynDb(metric,value);
		return ResponseEntity.status(HttpStatus.CREATED).body("{\"Created Metric\":"+metric+",\"Created Value\":"+value+"}");
	}
	
	@PostMapping("/crdyntable")
	public ResponseEntity<String> createtable(@RequestHeader String tablename,@RequestHeader String attrone,@RequestHeader String attrtwo,@RequestHeader String scalartypeone, @RequestHeader String scalartypetwo) {
		 aWSDynamoDBConfiguration.createschema(tablename, attrone,attrtwo,scalartypeone,scalartypetwo);
		 return ResponseEntity.status(HttpStatus.CREATED).body("{\"Created TableName\":"+tablename+"}");
	}
	
	@PutMapping("/updtable")
	public String updateschema(@RequestHeader String tableName,@RequestHeader String fieldone,@RequestHeader String fieldtwo,@RequestHeader String valueone,@RequestHeader String valuetwo) {
		aWSDynamoDBConfiguration.updateschema(tableName, fieldone, fieldtwo, valueone, valuetwo);
		return tableName;
		
	}
	
	@DeleteMapping("/deltable")
	public ResponseEntity<String> deleteschema(@RequestHeader String tableName) {
		aWSDynamoDBConfiguration.deletetable(tableName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"Deleted table\":"+tableName);
		
	}

	
}
