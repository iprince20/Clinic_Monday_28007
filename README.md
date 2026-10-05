# Clinic API

Spring Boot REST API starter for the clinic project.

## Included

- Java 17 and Maven
- Spring Web MVC
- Spring Data JPA
- PostgreSQL JDBC driver
- Lombok

No Doctor, Office, or Patient CRUD functionality has been added yet.

## Build

```powershell
.\mvnw.cmd -DskipTests package
```

The starter context test and application startup require a PostgreSQL datasource. Configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` before running them.

## Run

```powershell
.\mvnw.cmd spring-boot:run
```
