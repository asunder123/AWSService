package com.awssdjava.awsdemo;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.FilterConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectId;
import com.amazonaws.services.s3.model.S3ObjectIdBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.BucketCannedACL;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Request;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest.Builder;


@SpringBootApplication
@EnableAutoConfiguration
@EnableContextCredentials
@Slf4j
public class AwsServiceApplication {


	private static  AWSEC2Configuration aWSEC2Configuration = new AWSEC2Configuration();
    
	private  static AWSRDSConfiguration awsrdsConfiguration = new AWSRDSConfiguration();
	
	private static AWSDynamoDBConfiguration awsDynamoDBConfiguration = new AWSDynamoDBConfiguration();
	
	private static AWSXRayConfiguration awsXRayConfiguration = new AWSXRayConfiguration();
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException{
		 SerializationFeature.FAIL_ON_EMPTY_BEANS.enabledIn(0);
		 log.info("XRayRecorderConfig " + awsXRayConfiguration.TracingFilter());
		 SpringApplication.run(AwsServiceApplication.class, args);

	 }

	@Bean
	public EC2Plugin ec2plugin() {
		return new EC2Plugin();
	}
   
	/*@Bean
    public S3Client client() {
		return S3Client.builder().region(Region.US_EAST_2).build();
		
	}
	
   @Bean
   public S3Configuration s3Configuration() {
	return S3Configuration.builder().build();
	   
   }
   
   @Bean
   public CreateBucketRequest req() {
	return CreateBucketRequest.builder().build();
	   
   }
   
   @Bean
   public GetObjectRequest getreq() {
	   return new GetObjectRequest("splunk-micro-service","AWSService-0.0.1-SNAPSHOT.jar");
   } */

   }


