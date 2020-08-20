package com.hjdrsa.dynamodb.poc.event;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hjd
 */
@Slf4j
@Repository
public class EventRepository {
  
  @Autowired
  private DynamoDBMapper dynamoDBMapper;
  
  public Optional<Event> findByUserIdAndId(String userId, String id) {
    Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(id));
    Event event = new Event();
    event.setUserId(userId);
    event.setId(id);
    
    DynamoDBQueryExpression<Event> dynamoDBScanExpression = new DynamoDBQueryExpression().withHashKeyValues(event).withRangeKeyCondition("id", condition).withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
    
    PaginatedQueryList<Event> events = dynamoDBMapper.query(Event.class, dynamoDBScanExpression);
    
    return Optional.ofNullable(events.isEmpty() ? null : events.get(0));
  }
  
  public List<Event> findByUserId(String userId) {
    Event event = new Event();
    event.setUserId(userId);
    DynamoDBQueryExpression<Event> dynamoDBScanExpression = new DynamoDBQueryExpression().withHashKeyValues(event);
    return dynamoDBMapper.query(Event.class, dynamoDBScanExpression );
  }
  
  public Event save(Event event) {
    dynamoDBMapper.save(event);
    return event;
  }
  
  public List<Event> findAll() {
    DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
    return dynamoDBMapper.scan(Event.class,dynamoDBScanExpression);
  }
}
