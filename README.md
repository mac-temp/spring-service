# Offer Service documentation
This project contains the source of an offer service a simple CRUD using Spring Boot.
## How to run it
1. Run `mvn verify` to run the test and prepare a jar
2. Run `docker-compose up -d` to have the application running locally on 8080

## Application details
I've worked through this project using outside-in method. I have first drawn up a rough API contract using
[Swagger Editor](https://swagger.io/docs/open-source-tools/swagger-editor/)
which is available under `local/api-spec.yml`. I would then proceed to write controller test matching the contract
followed by service and persistence tests.
### Persistence
I left out any persistent data repository. Repository is just a HashMap.
### Test coverage
I have covered unit tests for the controller, service and repository.
I have also written an integration test using real application context.
### Documentation
Apart from this document, I have added
[documentation via Swagger](http://localhost:8080/swagger-ui/).
