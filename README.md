# AnimalHub

![AnimalHub's cat mascot wearing a colorful jacket](.readme-cat.jpg)

AnimalHub is a bilingual (English/German) platform for discovering adoptable animals and interacting with animal shelters. Users can browse animals, save favorites, arrange appointments, subscribe to shelters, and donate. Shelter members manage their shelter and animals, while administrators manage users and shelters.

The application consists of:

- an Angular 16 frontend
- a Java 17 / Spring Boot 3 backend
- a local file-based H2 database

## Run locally

Prerequisites: Java 17, Maven, and Node.js with npm. Node.js 18 LTS is the safest match for this Angular version.

### 1. Start the backend

Open a terminal in the repository root:

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=generateData
```

The `generateData` profile fills an empty database with demo data. For a normal start without generating demo data, use `mvn spring-boot:run`.

The API is available at `http://localhost:8080/api/v1`. Backend management endpoints run on port `8081`. The H2 database, uploaded files, and logs are created locally inside `backend/`.

### 2. Start the frontend

Open a second terminal in the repository root:

```bash
cd frontend
npm ci
npm start
```

Then open <http://localhost:4200>.

### Demo accounts

All generated accounts use the password `password`.

| Role | Email |
| --- | --- |
| Administrator | `admin@animalhub` |
| Shelter member | `member1@animalhub` |
| Shelter member | `member2@animalhub` |
| Shelter member | `member3@animalhub` |
| User | `user@animalhub` |
| User | `donator@animalhub` |

## Tests

```bash
cd backend
mvn test
```

```bash
cd frontend
npm test
```

End-to-end Cypress tests and their own npm setup are located in `e2e/`.

## Project structure

```text
backend/   Spring Boot REST API, persistence, security, and demo-data generation
frontend/  Angular single-page application
e2e/       Cypress end-to-end tests
```
