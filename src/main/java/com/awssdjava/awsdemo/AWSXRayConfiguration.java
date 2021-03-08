package com.awssdjava.awsdemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.Filter;
import javax.swing.text.html.parser.Entity;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.emitters.Emitter;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.entities.TraceID;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.servicemetadata.XrayServiceMetadata;

@Configuration
@Slf4j
public class AWSXRayConfiguration {
    
	@Autowired
	private EC2Plugin ec2Plugin ;
	
	public Map<String, Object> getXrayconfig() throws InstantiationException, IllegalAccessException {
	AWSXRayRecorderBuilder awsxRayRecorderBuilder = AWSXRayRecorderBuilder.standard().withPlugin(ec2Plugin);
	URL ruleFile = AWSXRayConfiguration.class.getResource("https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-java-monitoring.html");
	awsxRayRecorderBuilder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
	AWSXRay.setGlobalRecorder(awsxRayRecorderBuilder.standard().defaultRecorder());
     log.info("Recorder set " + awsxRayRecorderBuilder.contextMissingStrategyFromEnvironmentVariable());
	return  AWSXRay.getGlobalRecorder().beginSegment("Document").getHttp();
	}
	
	 public String randomName() throws IOException {
		    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		    HttpGet httpGet = new HttpGet("www.google.com");
		    CloseableHttpResponse response = httpclient.execute(httpGet);
		    try {
		      HttpEntity entity = response.getEntity();
		      InputStream inputStream = entity.getContent();
		      ObjectMapper mapper = new ObjectMapper();
		      Map<String, String> jsonMap = mapper.readValue(inputStream, Map.class);
		      String name = jsonMap.get("name");
		      EntityUtils.consume(entity);
		      return name;
		    } finally {
		      response.close();
		    }
		  }
	 
	 @Bean
	  public Filter TracingFilter() {
	    return new AWSXRayServletFilter("Scorekeep");
	  }
}
