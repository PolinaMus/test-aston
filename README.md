# test-aston
This pet project is a simple implementation of a RESTful API, back service and data model for creating bank accounts and transferring money between them. Interaction with the API is done using HTTP requests.

## instructions
To generate jooq-beans, run the command:
`mvn clean compile` or `mvn clean package`

To create a database, execute the command: 
```
mvn liquibase:update
```

To connect to the database, start Docker: 
```
docker-compose up
```

Next you need to check the connection to the database, the connection data is contained in docker-compose.yml.

## Server
The Application starts a server on localhost port 8080.

## API
| HTTP Method  | Path | Usage |
| ------------- | ------------- | -------------|
| POST  | /api/accounts  | Create Account | 
| GET | /api/accounts/balances  | Get all balances by accounts  |
| POST  | /api/accounts/deposit  | Replenish deposit | 
| POST  | /api/accounts/withdraw  | Withdraw money | 
| POST  | /api/accounts/transfer  | Transfer money between accounts | 
