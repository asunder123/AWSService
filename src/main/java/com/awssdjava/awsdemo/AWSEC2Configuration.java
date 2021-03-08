package com.awssdjava.awsdemo;


import java.util.Map;

import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.Request;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;


@Configuration
@Slf4j
public class AWSEC2Configuration {

    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
    
     private DescribeInstancesRequest desrequest = new DescribeInstancesRequest().withInstanceIds("i-08a70a890d79303da");

     
     public RequestClientOptions createinstmonitoring(String instanceIds) {
     	 return   new DescribeInstancesRequest().withInstanceIds(instanceIds).withInstanceIds(instanceIds).getRequestClientOptions();
   
     }
	
     public DescribeInstancesResult getEc2InstanceStatusResult() {
		log.info("Monitor Request" +desrequest.getCustomRequestHeaders() ) ;
		DescribeInstancesResult  describeInstancesResult = ec2.describeInstances(desrequest);
		log.info("Token" +describeInstancesResult.getNextToken());
		log.info("Reservations1" +describeInstancesResult.getReservations().get(0));
		log.info("Reservations2" +describeInstancesResult.getReservations().get(0));
		log.info("Http Status code " +describeInstancesResult.getSdkHttpMetadata().getHttpStatusCode());
		log.info("Http Headers " +describeInstancesResult.getSdkHttpMetadata().getHttpHeaders());
		return describeInstancesResult;
	} 
	
}
