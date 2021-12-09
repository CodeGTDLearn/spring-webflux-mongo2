### Project Index

1. WebFlux:
    1. Functional End-Points:
        1. **Router:**
           1. Receive the messages from the client
           2. Using the defined routes
           3. Send the messages for Handler
        2. **Handler:**
           1. Manage HTTP(Resquests/responses)
           2. Receiving the message ROUTED from the Router
           3. and, send this message for SERVICE


2. MongoDB Strategy:
    1. Reactive SpringDataMongoDB
    2. Services:
        1. Embed Objects
        2. Referencing
        3. _"Assemble"_ full objects


3. Application.Yml:
    1. Importation of properties:
        1. "@Value" annotation
        2. PropertySource
    2. Yml filesystem-Format
    3. Custom Logging.pattern.console


4. Application PROFILES:
    1. **Sufix:** Defining Application-sufix.Yml files
    2. **Annotations:** Selecting beans with specific Db properties
    3. **Groups:**
        1. Active group using _active-profile property_
        2. Grouping profiles [check link](https://www.baeldung.com/spring-profiles#4-profile-groups)


5. Docker:
    1. Compose
        1. _Specific file:_ **dev-compose.yml**
    2. Dockerfile
        1. _Specific file:_ **Dockerfile-dev**
    3. Bat Scripts:
        1. Parametric-scripts (env_variables)
            1. Parametric-scripts IDE execution
        2. Reusing bat-scripts:
            1. ex.: compose-up.bat using clean.bat


4. Testcontainers:
    * Containers
    * Compose
   

5. CRUD Strategy:
   1. OPTIMISTIC-LOCKING-UPDATE:
      1. Uses the 'VERSION-ANNOTATION' in the Java-Entity
      2. to prevent problems caused by 'CONCURRENT-UPDATES'
      3. EXPLANATION:
         1. The ENTITY-VERSION in the UPDATING-OBJECT
         2. must be the same ENTITY-VERSION as the DB-OBJECT