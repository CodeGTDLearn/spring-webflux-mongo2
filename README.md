## Spring-WebFlux-Mongo2

### Table of Contents
* [WebFlux](#webflux)
* [MongoDB Strategy](#MongoDB-Strategy)
* [Docker Mongo Replicaset](#docker-mongo-replicaset)
* [Application.Yml](#application_yml)
* [Application Profiles](#application-profiles)
* [Docker](#docker)
* [Testcontainers](#Testcontainers)
* [CRUD Strategy](#CRUD Strategy)
* [Architectural Strategy](#architectural-strategy)
* [SpringData](#springdata)
* [Project Organization](#Project Organization)
* [Bean Validation](#bean validation)
* [Exceptions](#Exceptions)
* [Tests Junit 5](#Tests Junit 5)

### WebFlux:
1. RestControllers


### MongoDB Strategy
1. Reactive SpringDataMongoDB
    1. Crud
    2. Repo
    3. Template
2. Services:
    1. Embed Objects
    2. Referencing
    3. _"Assemble"_ full objects
3. Example Sources:
    1. [Spring Project](https://github.com/spring-projects/spring-data-examples)
    2. Spring DataMongo


### Docker-Mongo-Replicaset
1. Singlenode
    1. Idea:
        1. Three nodes is the ideal, either for redundance, or transactions;
            1. However
                1. It will increasy A LOT the HOST-SERVER
            2. Singlenode IS CHEAPER and allow transactions, as well:
            * It is cheaper because requires only ONE VM in the cloud
                + IT CAN _**DECREASE**_ THE COST "CONSIDERABLY"
    3. Types:
        1. NoAuthentication
        2. Authenticated
            1. dynamic mongodb-keyfile (generate as a service in compose)
            2. [3Nodes - Base for single node authentication](https://www.youtube.com/watch?v=-XzMfd4XQak)
                1. [GitHub](https://github.com/willitscale/learning-docker)
2. Three nodes: (Aborted - Archived)
    1. **NOTE**:
        1. This replica set is for *Local Development* purposes ONLY.
            1. Run multiple nodes within a single machine is an anti-pattern, and MUST BE AVOIDED in Production.
        2. Multiple nodes requires:
            1. Multiple Vm's in the cloud
                1. IT CAN _**INCREASE**_ THE COST "CONSIDERABLY"
        3. Only for test-reason because in production:
            1. Each node must be in a different host
    3. Types:
        1. NoAuthentication
        2. Three nodes - Authenticated:
            1. [ProfileProduction](https://sntnupl.com/mongodb-replicaset-for-development-using-docker)
            2. [yowko](https://github.com/yowko/docker-compose-mongodb-replica-set-with-auth/blob/master/docker-compose.yaml)
            3. [prashix](https://prashix.medium.com/setup-mongodb-replicaset-with-authentication-enabled-using-docker-compose-5edd2ad46a90)
            4. [keyfile](https://www.educba.com/mongodb-keyfile/)
            5. [mongo-authentication](https://mkyong.com/mongodb/mongodb-authentication-example/)
            6. [MongoCli](https://www.mongodb.com/docs/manual/reference/configuration-file-settings-command-line
               -options-mapping/#std-label-conf-file-command-line-mapping/)
3. StandAlone - ProfileDevelopment
    1. Only archive because:
        1. it does not allow transactions, therefore:
            1. it does not run queues(CREATE+DELETE+UPDATE)
4. Database Configurations:
    1. Select programattically the URI Database 
    2. based on the Application-Profiles:
       1. replicaset
       2. replicaset auth 
       3. standalone


### Application_Yml
1. Importation of properties:
    1. "@Value" annotation
    2. PropertySource
2. Yml filesystem-Format
3. Custom Logging.pattern.console


### Application PROFILES
1. **Sufix:** Defining Application-sufix.Yml files
2. **Annotations:** Selecting beans with specific Db properties
3. **Groups:**
    1. Active group using _active-profile property_
    2. [Grouping profiles](https://www.baeldung.com/spring-profiles#4-profile-groups)


### Docker
1. Compose
    1. _Specific file:_ **dev-compose.yml**
2. Dockerfile
    1. _Specific file:_ **Dockerfile-dev**
3. Batch Scripts:
    1. Parametric-scripts (env_variables)
        1. Parametric-scripts IDE execution
    2. Reusing bat-scripts:
        1. ex.: compose-up.bat using clean.bat


### Testcontainers
* Containers:
    - Annotation (TcContainerConfig)
        - EX.: ResourceTransactionExcTest
    - ContextConfiguration - initializers
        - EX.: ResourceTransactionTest
* Compose - Annotation


### CRUD Strategy
1. OPTIMISTIC-LOCKING-UPDATE:
    1. Uses the 'VERSION-ANNOTATION' in the Java-Entity
    2. to prevent problems caused by 'CONCURRENT-UPDATES'
    3. EXPLANATION:
        1. The ENTITY-VERSION in the UPDATING-OBJECT
        2. must be the same ENTITY-VERSION as the DB-OBJECT


### Architectural Strategy
* SOLID
* Screaming Architecture
* CDC: Contract driven development
* Testability:
    * TDD/CDC: Controllers
    * Confirmation: Service
    * Reactive Queries


### SpringData
1. findPostsByAuthor_Id
2. @Transactions
3. Queries
    1. Derived:
        1. Simple
        2. Relationships
    2. Parameter
    3. Native
    4. Criteria
4. Examples:
    1. [SpringaData Project](https://github.com/spring-projects/spring-data-examples)
    2. [MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb)
    3. [Reactive MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb/reactive)


### Project Organization
1. Crud (ReactiveCrudRepository)
2. Repo (ReactiveMongoRepository)
3. Template (ReactiveMongoTemplate):
    1. Templ
    2. Aggregations
    3. TemplChildArrays
        1. Element Arrays
        2. Child Objects Lists
    4. TemplCollections
        1. Operations in multiple collections


### Bean Validation
1. Annotations - javax.validation.constraints:
    1. @NotEmpty
    2. @Positive


### Exceptions
1. Exceptions must be in Controller/Resource:
    1. Reason:
        1. Como stream pode ser manipulado por diferentes grupos de thread, caso um erro aconteça em uma thread que
           não é a que operou a controller, o ControllerAdvice não vai ser notificado "
        2. As stream can be handled by different thread groups, if an error happens on a thread other than the one
           that operated the controller, ControllerAdvice will not be notified "
    2. Source:
        1. [medium](https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3)
        2. [Github](https://github.com/netshoes/blog-spring-reactive)
2. Global
3. Custom
    1. Importation/validation of properties:
        1. @PropertySource
        2. @ConfigurationProperties:
            1. Automatic importation from PropertiesFile to Class-Instances-variables
                1. Do not need "@Value" annotation


### Tests Junit 5
1. MultiThread/Parallel Test
    1. Aborted: Because server-costs in CI/CD
2. RestAssured:
    1. RestAssuredWebTestClient:
        1. Reactive RestAssured
    2. JsonSchemaValidator
        1. Validate Responses
3. Ordered tests (Junit 5.8.2)
4. Suites
    1. [junit5-test-suites-examples](https://howtodoinjava.com/junit5/junit5-test-suites-examples/)
5. Tags
6. System.setProperty:
    1. Get environment.getActiveProfiles() for detect ReplicasetProfile
7. EnabledIfSystemProperty
    1.
    Source:  [junit5-enabledifsystempropert](https://self-learning-java-tutorial.blogspot.com/2021/07/junit5-enabledifsystemproperty.html)
8. Spring Expression Language (SpEL) expressions:
    1. EnabledIf + SpEL
        1. Sources:
            1. [spring-5-enabledIf](https://www.baeldung.com/spring-5-enabledIf)
            2. [junit-5-conditional-test-execution](https://www.baeldung.com/junit-5-conditional-test-execution)