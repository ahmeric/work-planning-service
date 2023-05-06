# Work Planning Service

This is a Spring Boot REST application that serves as a work planning service. The application
allows workers to have shifts and ensures that a worker never has two shifts on the same day. A
shift is 8 hours long, and the timetable is 24 hours long with three shifts: 0-8, 8-16, and 16-24.

## Build and Run

To build the application, run the following command in the project directory:

```sh
./gradlew build
```
This will generate a JAR file in the `build/libs` directory.

To run the application, execute the following command from the root directory:

```
./gradlew bootRun
```

The application will start on port 8080, and you can access the Swagger UI by navigating
to `http://localhost:8080/swagger-ui.html`.

### Running the Application with Docker

#### Build the Docker Image

Ensure you have built the JAR file by running `./gradlew build` in your project directory.

To build the Docker image, run the following command in the same directory as your Dockerfile:

```sh
docker build -t work-planning-service .
```

#### Run the Docker Container

To run the Docker container, use the following command:

```sh
docker run -p 8080:8080 work-planning-service
```

The application should now be accessible at `http://localhost:8080`.

## Testing the Application

The application comes with a few unit tests that can be executed using the following command:

```
./gradlew test
```


## Technologies Used

The application was built using the following technologies:

- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Validation
- H2 Database
- Lombok
- ModelMapper
- Mockito
- JUnit 5
- Gradle


## Configuration

The `application.properties` file contains the configuration for the application, including server settings, JPA settings, H2 settings, Actuator settings, and Swagger settings.

## API Documentation

The API documentation is available through the Swagger UI, which can be accessed by navigating
to `http://localhost:8080/swagger-ui.html`. The Swagger UI provides a list of available endpoints,
request/response examples, and the ability to test the API directly from the UI.
