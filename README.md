# Kigali Clinic API

A Spring Boot REST API for managing clinic patients, doctors, offices, specializations, and appointments. The project uses Spring Data JPA with PostgreSQL and includes CRUD endpoints plus derived-query and JPQL examples for searching, reporting, and bulk appointment operations.

## Stack

- Java 17
- Spring Boot 4 and Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven Wrapper
- Lombok

## Domain model

- A doctor may have an office and may hold multiple specializations.
- An appointment belongs to one doctor and one patient, has a `LocalDate`, and has one of four statuses: `SCHEDULED`, `CONFIRMED`, `COMPLETED`, or `CANCELLED`.
- Patients and doctors store first and last names.

## API

The API base URL is `http://localhost:8080`. Standard CRUD routes are available for each resource:

| Resource | Collection path |
|---|---|
| Patients | `/api/patients` |
| Doctors | `/api/doctors` |
| Offices | `/api/offices` |
| Specializations | `/api/specializations` |
| Appointments | `/api/appointments` |

For each collection, `GET` lists records, `POST` creates one, `GET /{id}` reads one, `PUT /{id}` updates one, and `DELETE /{id}` deletes one. Appointment creation also accepts `POST /api/appointments/save`.

### Query and reporting endpoints

| Requirement | Method and path | Purpose |
|---|---|---|
| A1 | `GET /api/patients/by-last-name?lastName=Uwase` | Case-insensitive last-name lookup, ordered by first name |
| A2 | `GET /api/appointments/by-status?status=SCHEDULED` | Appointments by enum status, ordered by date |
| A3 | `GET /api/appointments/between?start=2026-10-01&end=2026-10-31` | Appointments between inclusive dates |
| A4 | `PATCH /api/doctors/{doctorId}/office?officeNumber=2` | Move a doctor to an office if it is not occupied |
| B1 | `POST /api/appointments/save` | Rejects past appointment dates and double-booking; new appointments start as `SCHEDULED` |
| B2 | `PATCH /api/appointments/{id}/status?status=CONFIRMED` | Enforces the allowed appointment status transitions |
| B3 | `PATCH /api/appointments/{id}/reschedule?newDate=2026-10-30` | Reschedules non-final appointments and checks date conflicts |
| C1 | `POST /api/prescriptions/save?appointmentId=1` | Creates a prescription only for a completed appointment |
| C2 | `GET /api/prescriptions/appointment/{appointmentId}` | Lists prescriptions for an appointment |
| C3 | `GET /api/prescriptions/patient/{ssNumber}` | Lists patient prescription history, newest first |
| C4 | `PUT /api/prescriptions/update/{id}`, `DELETE /api/prescriptions/delete/{id}`, `DELETE /api/appointments/{id}` | Updates/deletes prescriptions and prevents deleting appointments that still have prescriptions |
| D1 | `GET /api/doctors/{doctorId}/agenda?date=2026-10-20` | Lists a doctor's appointments for a day, excluding cancelled ones |
| D2 | `GET /api/patients/{ssNumber}/upcoming` | Lists today's and future scheduled/confirmed patient appointments |
| D3 | `GET /api/doctors/search?name=gis` | Case-insensitive first/last-name search, sorted by last name |
| D4 | `GET /api/dashboard/summary` | Returns clinic patient, doctor, office, appointment, and prescription counts |
| Bonus | `GET /api/prescriptions/stats/top-medicines` | Prescription counts by medicine, most prescribed first |
| Bonus | `GET /api/doctors/workload?start=2026-10-01&end=2026-10-31` | Completed appointment counts by doctor, busiest first |

Date parameters use ISO format (`YYYY-MM-DD`). Appointment statuses must match the enum values. The bulk cancellation and delete endpoints modify stored data.

## Database configuration

PostgreSQL must be running, and the `hospital_db` database must exist. Configure these environment variables before starting the application:

- `SPRING_DATASOURCE_URL` (defaults to `jdbc:postgresql://localhost:5432/hospital_db`)
- `SPRING_DATASOURCE_USERNAME` (defaults to `postgres`)
- `SPRING_DATASOURCE_PASSWORD` (required; never commit this value)

In PowerShell, set the URL and username, then enter the password at a masked prompt in the same terminal used to run the app:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/hospital_db"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$secure = Read-Host "PostgreSQL password" -AsSecureString
$env:SPRING_DATASOURCE_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
Remove-Variable secure
```

Hibernate uses `ddl-auto=update` for local schema updates, and SQL logging is enabled with `spring.jpa.show-sql=true`.

## Build, test, and run

Build without running tests:

```powershell
.\mvnw.cmd -DskipTests package
```

Run tests (the Spring context test requires the configured PostgreSQL database):

```powershell
.\mvnw.cmd test
```

Start the API from the project root, in the same terminal where the datasource variables are set:

```powershell
.\mvnw.cmd spring-boot:run
```

## Postman

Import [`clinic-quiz2.postman_collection.json`](clinic-quiz2.postman_collection.json) into Postman. Update its collection variables, especially IDs, patient SS number, and dates, to match your local database. Requests are named by requirement ID; C4 groups its update, delete, and appointment-protection requests.

For a live demo, choose a valid ID/date for a successful request, then use a nonexistent ID or an invalid status/date to show an error response. The service methods enforce the business rules and response messages; Spring Data repository methods and JPQL queries perform the lookups and reports.
