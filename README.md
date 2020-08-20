
# DynamoDB poc
A poc using the dynamodb java sdk

## Prerequisites
 - Git client
 - Java 11
 - Maven version 3.5.x or later
 - Java IDE
 - Docker for Desktop (Optional)

## Getting Started

You need a local instance of dynamodb running:

 1. Navigate to [Amazon docker hub](https://hub.docker.com/r/amazon/dynamodb-local/) and flow the steps

To get the project up and running on your local machine, do the following:
 
 2. Check out the project code using Git.
 3. Go to the root directory of the checked out project.
 4. Run `mvn package`
 5. Run `java -jar target/dynamodb.poc-0.0.1.jar`
 6. Navigate to [http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)
