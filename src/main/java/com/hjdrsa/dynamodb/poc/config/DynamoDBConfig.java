package com.hjdrsa.dynamodb.poc.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.hjdrsa.dynamodb.poc.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author hjd
 */
@Configuration
@Slf4j
public class DynamoDBConfig {
  
  @Value("${amazon.aws.accesskey}")
  private String accessKey;
  
  @Value("${amazon.aws.secretkey}")
  private String secretKey;
  
  @Value("${amazon.dynamodb.endpoint}")
  private String endpoint;
  
  @Value("${amazon.aws.region}")
  private String region;
  
  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(accessKey, secretKey);
  }
  
  public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
  }

  @Bean
  public DynamoDBMapperConfig dynamoDBMapperConfig(){
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Bean
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, config);
    init(dynamoDBMapper, amazonDynamoDB);
    return dynamoDBMapper;
  }
  
  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBAsyncClientBuilder
            .standard()
            .withCredentials(amazonAWSCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .build();
    
  } 
  
  public void init(DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB) {
    CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Event.class);
    tableRequest.setBillingMode("PAY_PER_REQUEST");
    
    if(TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest)) {
      log.info("Table {} created", tableRequest.getTableName());
    }
  }
  
  @Bean
  public DynamoDB dynamoDB() {
    return new DynamoDB(amazonDynamoDB());
  }
}
