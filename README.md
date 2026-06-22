# SolMed Backend

REST API for **SolMed** — medication scheduling, dose logging, caretaker linking, and adherence reporting. Built with Spring Boot and secured with JWT.

The web UI lives in the companion **Project-SolMed-frontend** repo (static HTML/JS served on port **5000** in development).

---

## Features

- **User accounts** — patients (`OWNER`), caretakers (`CARETAKER`), and admins (`position: "Admin"`)
- **Medicine catalog** — global list plus user-submitted medicines
- **Schedules & logs** — per-user medicine times, dose taken/refill, stock tracking
- **Dose automation** — scheduled jobs mark missed doses and extend future dose windows
- **Caretaker linking** — request/accept/reject/revoke patient–caretaker relationships; adherence reports for linked patients
- **JWT authentication** — stateless API with role-based access control

---

## Tech stack

| Layer | Technology |
|-------|------------|
| Runtime | Java 17 |
| Framework | Spring Boot 4.0.x |
| Security | Spring Security + JWT (jjwt 0.12.x) |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| Build | Maven |

---

## Prerequisites

- JDK 17+
- Maven 3.9+ (or use included `./mvnw`)
- MySQL 8+ running locally or remotely

---

## Database setup

Create an empty database:

```sql
CREATE DATABASE sol_meddb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Hibernate `ddl-auto=update` creates/updates tables on startup. For production, prefer explicit migrations (Flyway/Liquibase) instead of `update`.

---

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8081

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/sol_meddb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT (use a strong secret in production; min 256 bits for HS256)
jwt.secret=YOUR_BASE64_OR_HEX_SECRET
jwt.expiration=86400000

# Optional schedulers (defaults shown)
# solmed.missed-dose-interval-ms=900000
# solmed.dose-extend-cron=0 15 0 * * ?
# solmed.app-zone=Asia/Kolkata
```

**Security:** Do not commit real database passwords or JWT secrets. Use environment-specific config or Spring profiles for production.

The frontend expects the API at **`http://localhost:8081`** (`API_BASE` in `Common.js`).

---

## Run locally

```bash
cd solmedbackend
./mvnw spring-boot:run
```

Windows:

```powershell
cd solmedbackend
.\mvnw.cmd spring-boot:run
```

API base: **http://localhost:8081**

Run tests:

```bash
./mvnw test
```

---

## Authentication

### Register

`POST /api/auth/signup`

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "secret",
  "phone": "9876543210",
  "age": 30,
  "gender": "Female",
  "position": "Patient",
  "role": "OWNER"
}
```

For caretakers, set `"role": "CARETAKER"` and optionally `"patientEmail": "patient@example.com"` to start a link request.

### Login

`POST /api/auth/login`

```json
{
  "email": "jane@example.com",
  "password": "secret"
}
```

Response includes a JWT. Send it on protected routes:

```
Authorization: Bearer <token>
```

Admin users are determined by `position: "Admin"` (grants `ROLE_ADMIN`).

---

## API overview

### Public

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/signup` | Register |
| POST | `/api/auth/login` | Login, returns JWT |

### Users — `/user`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/user/signup`, `/user/login` | Public | Legacy auth endpoints (prefer `/api/auth/*`) |
| GET | `/user/{id}` | Authenticated | Get user |
| GET | `/user/list` | ADMIN | List all users |
| POST | `/user/add` | Authenticated | Create user |
| PUT | `/user/{id}` | Authenticated | Update user |
| DELETE | `/user/{id}` | Authenticated | Delete user |

### Medicines — `/medicine`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/medicine/list` | Authenticated | List medicines |
| GET | `/medicine/{id}` | Authenticated | Get medicine |
| POST | `/medicine/add` | OWNER/ADMIN | Add to catalog |
| POST | `/medicine/addByUser` | OWNER/ADMIN | User-submitted medicine |
| DELETE | `/medicine/{id}` | OWNER/ADMIN | Delete medicine |

### User medicines — `/userMedicine`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/userMedicine/user/{Id}` | OWNER/ADMIN | Medicines for a user |
| GET | `/userMedicine/{id}` | OWNER/ADMIN | Single schedule |
| POST | `/userMedicine/add` | OWNER/ADMIN | Assign medicine + time |
| PATCH | `/userMedicine/medicine/{id}` | OWNER/ADMIN | Update schedule |
| DELETE | `/userMedicine/{id}` | OWNER/ADMIN | Remove schedule |

### Medicine logs — `/medicineLog`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| GET | `/medicineLog/userMedicine/{Id}` | OWNER/ADMIN | Logs for a user medicine |
| GET | `/medicineLog/userMedicine/{id}/taken-today` | OWNER/ADMIN | Today's taken status |
| POST | `/medicineLog/add` | OWNER/ADMIN | Create log |
| PATCH | `/medicineLog/medTaken/{id}` | OWNER/ADMIN | Mark dose taken |
| PATCH | `/medicineLog/refill/{id}` | OWNER/ADMIN | Refill stock |
| DELETE | `/medicineLog/{id}` | OWNER/ADMIN | Delete log |

### Caretaker — `/api/caretaker`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/request-link` | CARETAKER | Request link to patient |
| GET | `/my-link-requests` | CARETAKER | Outgoing requests |
| GET | `/incoming-link-requests` | OWNER/ADMIN | Pending requests for patient |
| POST | `/link-requests/{id}/accept` | OWNER/ADMIN | Accept link |
| POST | `/link-requests/{id}/reject` | OWNER/ADMIN | Reject link |
| DELETE | `/revoke/{caretakerId}` | OWNER/ADMIN | Revoke caretaker access |
| GET | `/list` | OWNER/ADMIN | Caretakers for current patient |
| GET | `/patients` | CARETAKER | Linked patients |
| GET | `/patient/{ownerId}/medicines` | CARETAKER | Patient medicines |
| GET | `/patient/{ownerId}/adherence` | CARETAKER | Adherence report |

---

## Scheduled jobs

Enabled via `@EnableScheduling` on `SolmedbackendApplication`:

| Job | Default schedule | Purpose |
|-----|------------------|---------|
| `processMissedDoses` | Every 15 minutes | Mark overdue doses as missed |
| `extendDoseHorizon` | Daily 00:15 (Asia/Kolkata) | Extend rolling dose window for all user medicines |

Override with properties: `solmed.missed-dose-interval-ms`, `solmed.dose-extend-cron`, `solmed.app-zone`.

---

## Project structure

```
src/main/java/com/solmed/solmedbackend/
├── SolmedbackendApplication.java
├── config/              # SecurityConfig
├── webConfigs/          # CORS
├── jwt/                 # JwtUtil, JwtAuthFilter, JwtProperties
├── Controller/          # AuthController
├── Service/             # AuthService
├── user/                # User entity, UserController, UserService
├── medicine/            # Medicine catalog
├── UserMedicine/        # Patient schedules
├── MedicineLog/         # Dose & stock logs
├── dose/                # DoseLog, missed-dose processing
├── caretaker/           # Link requests, relationships, adherence DTOs
├── schedule/            # SolMedSchedulers
└── DTOs/                # Login, register, auth responses
```

---

## Security model

- **Stateless sessions** — JWT in `Authorization: Bearer` header
- **Password hashing** — BCrypt
- **Roles** — `ROLE_OWNER`, `ROLE_CARETAKER`, `ROLE_ADMIN` (from `User.role` and lenient `position`)
- **CORS** — Permissive for development (`*` origins); tighten for production

---

## Development with frontend

1. Start MySQL and create `sol_meddb`
2. Configure `application.properties`
3. Run `./mvnw spring-boot:run` (port **8081**)
4. In `Project-SolMed-frontend`, run `npm start` (port **5000**)
5. Open http://localhost:5000

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Cannot connect to MySQL | Verify URL, credentials, and that MySQL is running |
| 403 on medicine routes | User needs `OWNER` or `ADMIN` role |
| Caretaker endpoints 403 | Token must belong to a `CARETAKER` account |
| Tables not created | Check `ddl-auto=update` and DB user privileges |
| JWT invalid after restart | Ensure `jwt.secret` is stable across restarts |

---


