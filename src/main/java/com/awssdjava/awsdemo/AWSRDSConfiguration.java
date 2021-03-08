package com.awssdjava.awsdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.aws.jdbc.config.annotation.AmazonRdsInstanceConfiguration;
import org.springframework.cloud.aws.jdbc.config.annotation.AmazonRdsInstanceConfiguration.RdsInstanceConfigurerBeanPostProcessor;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;


@Configuration
@Component
@EnableRdsInstance(dbInstanceIdentifier = "rdsdatabaseschema",password = "password456")
public class AWSRDSConfiguration implements BeanPostProcessor  {

	
	
	  @Bean
	    public RdsInstanceConfigurer instanceConfigurer() {
	        return new RdsInstanceConfigurer() {
	        	 
	        	AWSCredentials credentials = new BasicAWSCredentials("AKIATG5DD6H6U45LOK6Q", "NWAH6yXwG+0GqoO6xjpb1n9LIkiypB8KBpor1tKu");
	        	AmazonRDSClient rds ;
	        	  
	        	
	            @Override
	            public TomcatJdbcDataSourceFactory getDataSourceFactory() {
					TomcatJdbcDataSourceFactory dataSourceFactory =  new TomcatJdbcDataSourceFactory();
	                dataSourceFactory.setInitialSize(10);
	                System.out.println(dataSourceFactory.getUsername());
	                System.out.println(dataSourceFactory.getPassword());
	                dataSourceFactory.setValidationQuery("SELECT 1 FROM DUAL");
	                dataSourceFactory.setInitSQL("SELECT 2");
	                return dataSourceFactory;
	            }
	        };

	    }
}
;