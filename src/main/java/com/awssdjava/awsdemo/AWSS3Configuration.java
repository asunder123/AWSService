package com.awssdjava.awsdemo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.io.s3.AmazonS3ClientFactory;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.RequestClientOptions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.S3Configuration.Builder;
import software.amazon.awssdk.services.s3.internal.handlers.CreateBucketInterceptor;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@Configuration
@Slf4j
public class AWSS3Configuration {

  
 
    private AmazonS3 awss3  ;


    public  com.amazonaws.services.s3.model.Bucket createbuckets(String bucketname) {
	   awss3  = AmazonS3ClientBuilder.standard().build();
	   awss3.createBucket(bucketname);
	   return awss3.listBuckets().get(0);
   }
    
    public void  deletebuckets(String bucketname) {
    	awss3  = AmazonS3ClientBuilder.standard().build();
    	awss3.deleteBucket(bucketname);
    }
	
}
