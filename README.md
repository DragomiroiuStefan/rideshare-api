## Table of Contents

1.  [About The Project](#about-the-project)
    - [Built With](#built-with)
2.  [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Build](#build)
    - [Build a Container](#build-a-container)
    - [Run](#run)
    - [Run the Container](#run-the-container)
    - [Database configuration](#database-configuration)
3.  [Requirements](#requirements)
4.  [Design](#design)
5.  [Implementation](#implementation)
6.  [Security](#security)
7.  [Testing](#testing)
8.  [Deployment](#deployment)
9.  [Maintain](#maintain)
10. [Roadmap](#roadmap)
11. [License](#license)
12. [Contact](#contact)
13. [Acknowledgments](#acknowledgments)

# About The Project

RideShare is a web application for sharing mobility <span style="color: #0d1214;">where individuals can travel together to the same destinations, reducing travel costs, traffic, and parking demand.</span>

<span style="color: #0d1214;">This repository is the backend of the application - a [Spring Boot](https://spring.io/guides/gs/spring-boot) based REST API. The frontend for the project is available at: https://github.com/DragomiroiuStefan/ride-share-client</span>

### Built With

This section lists major frameworks/libraries used to bootstrap the project.

- Java  
  https://www.java.com/en/
- Maven  
  https://maven.apache.org/
- Spring Framework  
  https://spring.io/projects/spring-framework/
- Spring Boot  
  https://spring.io/projects/spring-boot/
- Spring Security  
  https://spring.io/projects/spring-security
- Spring Data JDBC  
  https://spring.io/projects/spring-data-jdbc
- PostgreSQL  
  https://www.postgresql.org/
- Docker  
  https://www.docker.com/

# Getting Started

To set up the project locally follow these steps:

### Prerequisites

The following items should be installed on your system:

- Java
- Git
- Docker
- A PostgreSQL instance set up (see Database Configuration)
- The following environment variables defined on the system:
    - PGHOST=&lt;database_host&gt;
    - PGPORT=&lt;database_port&gt;
    - PGDATABASE=&lt;database_name&gt;
    - PGUSER=&lt;database_user&gt;
    - PGPASSWORD=&lt;database_password&gt;
    - JWTSECRETKEY=&lt;\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*\*&gt;

### Build

Build an executable jar file:

1.  Clone the repo

    ```
    git clone https://github.com/DragomiroiuStefan/rideshare-api
    ```

2.  Install dependencies and create jar. The command uses the maven wrapper packaged with the project, no Maven installation is required

    ```
    ./mvnw package
    ```


### Build a Container

There is no `Dockerfile` in this project. You can build a container image (if you have a docker daemon) using the Spring Boot build plugin:

```
./mvnw spring-boot:build-image
```

### Run

To run the jar file execute the following command:

```
java -jar target/*.jar
```

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this, it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

### Run the container:

#TODO

### Database configuration

#TODO

# Requirements

#TODO

# Implementation

The easiest way to start a Spring Boot maven application is to use `spring-boot-­starter-parent` as the parent for your application. `spring-boot-­starter-parent` provides dependency and plugin management.

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.1</version>
</parent>
```

Next, we add Spring dependencies.

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
```

This will pull in all the core dependencies needed to start a very basic Spring Boot application; such as the Spring Framework, Logback for logging, and Spring Boot itself. Notice that there is no version or other information needed; because `spring-boot-starter-parent` defined the versions for spring dependencies.

Finally, to be able to create a JAR file that is executable, you will need to add the spring-boot-maven-plugin.

```
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

Create the entry point for the application

```
@SpringBootApplication
public class RideShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideShareApplication.class, args);
    }
}
```

## Logging

By default Spring Boot uses [Logback](https://logback.qos.ch/) for logging, and it's <span style="color: #191e1e;">pre-configured to use console output. <span style="color: #191e1e;">By default,</span> `ERROR`<span style="color: #191e1e;">\-level,</span> `WARN`<span style="color: #191e1e;">\-level, and</span> `INFO`<span style="color: #191e1e;">\-level messages are logged.</span></span>

<span style="color: #191e1e;"><span style="color: #191e1e;">We will configure logging to a file with daily and size (10MB) file rotation by adding the following configuration to `aplication.properties`.</span></span>

```properties
logging.file.name=rideshare.log
logging.file.path=/var/log/rideshare
logging.logback.rollingpolicy.file-name-pattern=logs/rideshare.%d{yyyy-MM-dd}.%i.log
```

#TODO in actual app.

## Spring DevTools

Devtools allows you to detect classpath changes, and when a change is detected, it will restart the application. This restart will be much faster than a cold start. It will also set certain properties to values that make sense during development (such as disabling caching for Thymeleaf templates, including full error details in error responses).

To use DevTools we need to add the dependency to `pom.xml`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

## REST API

To build a REST API we will add Spring MVC to the project.

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This adds the needed dependencies for Spring MVC, `spring-web` and `spring-­webmvc`. It will also add all the JAR files needed to be able to start an embedded Tomcat server when the application starts.

For each resource in our API we will create a controller annotated with `@RestController`. The following is a sample controller for locations in the application.

```java
@RestController
@RequestMapping("/locations")
class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    public static final String LOCATION_NOT_FOUND_ERROR_MESSAGE = "Location %s not found";

    private final LocationRepository locationRepository;

    LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @GetMapping("/{locationId}")
    public Location findById(@PathVariable Long locationId) {
        return locationRepository.findOptionalById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(LOCATION_NOT_FOUND_ERROR_MESSAGE, locationId)
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Location create(@RequestBody Location location) {
        logger.info("Create request for location: {}", location);
        locationRepository.save(location);
        return location;
    }

    @PutMapping("/{locationID}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long locationID, @RequestBody Location location) {
        logger.info("Update request for location: {}", location);
        if (locationRepository.findById(locationID).isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format(LOCATION_NOT_FOUND_ERROR_MESSAGE, locationID
                    ));
        }
        return locationRepository.save(location);
    }

    @DeleteMapping("/{locationID}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long locationID) {
        logger.info("Delete request location with ID: {}", locationID);
        locationRepository.deleteById(locationID);
    }
}
```

### Exception Handling

Throwing the following `ResourceNotFoundException` from any of the layers of the web tier will ensure Spring maps the corresponding status code on the HTTP response.

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
```

We also should have a centralized place to handle common errors, a global error handler, so we don't repeat error handling code across the application.

```java
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestError handleIllegalArgumentException(RuntimeException e, HttpServletRequest request) {
        return new RestError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );
    }

    public record RestError(
            LocalDateTime timestamp,
            Integer status,
            String error,
            String message,
            String path
    ) {
    }
}
```

#TODO add others handle methods to the class when the application is finished.

### Validation

Spring Boot has built in support for Hibernate Validator, the Bean Validation framework’s reference implementation.

To validate our domain objects we just need to add the Bean Validation’s constraint annotations.

```java
public record Location(
        @Id
        @NotNull(groups = {Update.class})
        Long locationId,
        @NotBlank
        @Size(max = 255)
        String city,
        @NotBlank
        @Size(max = 255)
        String county
) {}
```

By adding `@Valid` in our controllers, Spring Boot performs the validation for us.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Location create(@Valid @RequestBody Location location) {
    logger.info("Create request for location: {}", location);
    return locationRepository.save(location);
}
```

When Spring Boot finds an argument annotated with `@Valid`, it automatically bootstraps the default JSR 380 implementation — Hibernate Validator — and validates the argument.

<span style="color: #242424;">When we are using the same model for multiple operations like Create or Update, we may want to validate same model differently. For an Update <span style="color: #242424;">the id field is mandatory. We will use <span style="color: #242424;">a validation group to enforce validation only for the Update operation.</span></span></span>

```java
@PutMapping("/{locationID}")
@ResponseStatus(HttpStatus.OK)
public Location update(@PathVariable Long locationID, @Validated(Update.class) @RequestBody Location location) {
    // ...
}
```

### Mapping

### SSL

### CORS

## Database Access

### Data Source Configuration

To connect to a database we need:

1.  A database driver. In this case a PostgreSQL driver

    ```xml
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
    </dependency>
    ```

2.  A data source. Configuring a data source is a matter of including the properties in the `application.properties`.

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    ```


### Spring Data JDBC

To <span style="color: #191e1e;">reduce the amount of boilerplate code required to implement data access we will add Spring Data JDBC.</span>

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

<span style="color: #191e1e;">The central interface in the Spring Data repository abstraction is</span> `Repository`<span style="color: #191e1e;">. It takes the domain class to manage as well as the identifier type of the domain class as type arguments.</span>

```java
@Repository
public interface LocationRepository extends ListCrudRepository<Location, Long> {
    List<Location> findByCityContainingIgnoreCase(String city);
}
```

`ListCrudRepository` <span style="color: #191e1e;">offers CRUD functionality. Custom queries can be implemented manually or derived (implemented by Spring Data JDBC based on method signature).</span>

The domain class is a simple POJO. The only requirement is that the entity has a property annotated with `Id` (that is, `@org.springframework.data.annotation.Id`.

```
public record Location(
        @Id
        @NotNull(groups = {Update.class})
        Long locationId,
        @NotBlank
        @Size(max = 255)
        String city,
        @NotBlank
        @Size(max = 255)
        String county
) {}
```

### Transactions

#TODO Transaction Configuration

# Security

REST -> Stateless -> JWT for user sessions.

An advantage of using JWTs is scalability as the backend does not need to do a database/cache lookup for every API call.

<span style="color: #333333;">The drawback is that revoking a single token on demand (before it expires) can be difficult.</span>

<span style="color: #333333;">There are multiple ways of managing a user session. We will use <span style="color: #333333;">a short lived JWT (access token) and a long lived opaque token (refresh token). When the user signs in both of these are sent to the frontend via httpOnly and secure cookies. The JWT is sent by the forntend for each API call and is used to verify the session. Once the JWT expires, the frontend uses the opaque token to get a new JWT and a new opaque token. This is known as rotating refresh tokens. The new JWT is used to make subsequent API calls and the session continues normally. This flow is illustrated in the diagram below:</span></span>

![9f128fa55dca0ad14e32d1e812b77041.png](:/b91ed6a63e72420ba05db56dcb2049af)

### Sign Up

- check if email is already used
- save user to database
    - create access token  
      \- create refresh token  
      \- save refresh token to database
- return saved user, access token, refresh token

Request:

POST /auth/signUp
```json
{
  "email": "seconduser@gmail.com",
  "password": "pass",
  "firstName": "second",
  "lastName": "user",
  "phoneNumber": "07223",
  "birthDate": "2000-02-05"
}
```

Response:

```json
{
  "refreshToken": "a6hidhaeb8s... ",
  "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9... ",
  "user": { ... }
}
```

### Sign In

- check if email exists and if password matches
- delete refresh token by user ID
    - create access token
    - create refresh token
    - save refresh token to database
- return saved user, access token, refresh token

Request:

POST /auth/signIn
```json
{
  "email": "scott",
  "password": "tiger"
}
```

Response:

```json
{
  "refreshToken": "...",
  "accessToken": "...",
  "user": { ... }
}
```

### Sign Out

- delete refresh token from database by user ID

### Refresh

- if no refresh token in database return error
- create access token; use the same refresh token
- return saved user, access token, refresh token

Request:

POST /auth/refresh
```json
{
  "refreshToken": "a6hidhaeb8s..."
}
```

Response:

```json
{
  "refreshToken": "a6hidhaeb8s... ",
  "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9... ",
  "user": { ... }
}
```

# Testing

# Deployment

# Maintain

<span style="color: #333333;">In the maintenance phase, among other tasks, the team fixes bugs, resolves customer issues, and manages software changes. In addition, the team monitors overall system performance, security, and user experience to identify new ways to improve the existing software. <span style="color: #091e42;">Automatically notify your team of changes, high-risk actions, or failures, so you can keep services on.</span></span>

# Roadmap

- [x] Add Changelog (#TODO see repo)
- [x] Add back to top links
- [ ] If a user didn't show up change booking status from CONFIRMED to ?? so other users can still book even if the ride started.
- [ ] Integrate a payment processor and include the option to pay in advance. Refund if driver doesn't show up.

See the [open issues](https://github.com/othneildrew/Best-README-Template/issues) for a full list of proposed features (and known issues).

# License

Distributed under the MIT License. See `LICENSE.txt` for more information. #TODO

# Contact

Dragomiroiu Stefan - dragomiroiustefan@gmail.com

Project Link: <ins>https://github.com/dragomiroiustefan/rideshare-api</ins>

# Acknowledgments

#TODO

- [Choose an Open Source License](https://choosealicense.com)
- [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
- [Malven's Flexbox Cheatsheet](https://flexbox.malven.co/)
- [Malven's Grid Cheatsheet](https://grid.malven.co/)
- [Img Shields](https://shields.io)
- [GitHub Pages](https://pages.github.com)
- [Font Awesome](https://fontawesome.com)
- [React Icons](https://react-icons.github.io/react-icons/search)