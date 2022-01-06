### Project Index

1. WebFlux:
    1. RestControllers


2. MongoDB Strategy:
    1. Reactive SpringDataMongoDB
       1. Crud
       2. Repo
       3. Template
    2. Services:
        1. Embed Objects
        2. Referencing
        3. _"Assemble"_ full objects
    3. Example Sources - :
       1. [Spring Project](https://github.com/spring-projects/spring-data-examples)
       2. Spring DataMongo


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


6. Architectural Strategy:
    * SOLID
    * Screaming Architecture
    * CDC: Contract driven development
    * Testability:
      * TDD/CDC: Controllers
      * Confirmation: Service
      * Reactive Queries
      
 
7. Spring Data  (findPostsByAuthor_Id)
    1. Queries
       1. Derived:
           1. Simple
           2. Relationships
       2. Parameter
       3. Native
       4. Criteria
    2. Examples:
       1. [SpringaData Project](https://github.com/spring-projects/spring-data-examples)
       2. [MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb)
       3. [Reactive MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb/reactive)
       

8. Project Organization:
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


9. Bean Validation:
   1. Annotations - javax.validation.constraints:
      1. @NotEmpty
      2. @Positive


10. Exceptions:
    1. Global
    2. Custom
       1. Importation/validation of properties:
           1. @PropertySource
           2. @ConfigurationProperties:
               1. Automatic importation from PropertiesFile to Class-Instances-variables
                   1. Do not need "@Value" annotation