# my-bank

A Spring Boot REST API that implements a simple banking system with Accounts and Transactions entities. 
This project provides a basic API that allows users to create, read and update accounts, as well as perform transactions between them.

The project is built on top of the Spring Boot framework and uses a relational database to store the account and transaction data.
In this project, the following endpoints are implemented:

| HTTP Method | Path                            | Summary                                                                       |
|-------------|---------------------------------|-------------------------------------------------------------------------------|
| GET         | /api/accounts/list              | Retrieves a list of all account                                               |
| POST        | /api/accounts/create            | Creates a new account                                                         |
| PUT         | /api/accounts/update/{id}       | Updates an existing account                                                   |
| GET         | /api/transactions/list          | Retrieves a list of all transactions                                          |
| GET         | /api/transactions/listByAccount | Retrieves a transaction by source or target account id provided as parameters |
| POST        | /api/transactions/create        | Creates a new transaction between two accounts                                |



If you're using Postman, you can import the collection located under:
`src/main/resources/my-bank-collection.postman_collection.json`

## Build the application

`mvn clean package -DskipTests`

## Run the application

You can run the application using your IDE or by executing:

`mvn spring-boot:run`

On application start up, the H2 database will be created as file under the user's directory. 
You can change the location of the file as well as the database credentials configured in `src/main/resources/application.properties`.  

Liquibase has been used for initial DB schema creation as well as for some initial data entry.

`spring.datasource.url=jdbc:h2:~/my-bank-db`  
`spring.datasource.username=sa`  
`spring.datasource.password=sa`

You can view the database while the application is running,  by accessing: 
http://localhost:8080/h2-console



## Open Api
Detailed information about the rest endpoints are documented with swagger. You can access the documentation while the application is running:
http://localhost:8080/openapi/swagger-ui/index.html


## Tests
The project also includes several tests for the API endpoints and database integration. JUnit and Mockito frameworks have been used for unit testing.
Tests run on their own H2 database file configured in: `src/test/resources/application.properties`

## Using docker 

Build the app image   
`docker build -t my-bank-app . `

Run the container  
`docker run -p 8080:8080 -d my-bank-app`

