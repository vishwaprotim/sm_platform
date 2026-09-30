# User Service

### Features
* Request DTO validation using Jakarta, avoids unnecessary if-else validation logic
* Tables are created if they don't exist. However, DB named "postgres" must exist.
* Spring Security - disabled to allow all incoming requests
* Passwords are hashed and then stored into DB
* Separation of concerns - Swagger Documentation Annotations are applied on the API interfaces instead of controllers

### TODOs
* Caching for taken usernames
* Unit Testing for all field level validations