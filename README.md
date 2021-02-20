# Speech Manager

This application provides speech management features:

1. View all their speeches saved in the system.
2. Add a speech to the system (for this challenge we will consider a speech to have the actual text, the author information, keywords about the speech and a speech date).
3. Edit a speech or its metadata
4. Delete a speech
5. Share a speech with someone else via email
6. The ability to search the speeches – (search by author, date range, subject area, or snippets of text from the speech body.

## Tech

It provides REST API running on top of Spring Boot 2 framework. It integrates Mapstruct library for entity-dto conversion and Lombok for automatic getter/setter generation.

## Deployment

To run the application, simply clone this project and execute the command below in the terminal inside the root directory.

```shell
mvn spring-boot:run
```

The application is running on port 8080 by default.

## Docker Container

Before building docker, make sure to build the project first by running mvn install.

A docker file is also available for building an image:

```shell
docker build -t czetsuyatech/speech-manager -f docker/Dockerfile .
docker run -d -p 8080:8080 czetsuyatech/speech-manager
```

The application should now be running on port 8080.

## Testing

The integration tests using Mockito can be run via maven (development-h2 profile must be use):

```shell
mvn clean test -P development-h2
```

A Postman collection is also available in the test/postman folder. To run it, development profile must be use and a PostgreSQL database must be configured.

- DatabaseName=legalsight
- Username=dev
- Password=dev

The test "Share speech will fail" as it requires a valid email account in the application.properties.