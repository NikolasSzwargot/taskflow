# Description
Taskflow is a fullstack management application, impplementing Kanban style of thinking, inspired by tools like Jira.
It allows user to create tasks, organize them into statuses, wchich can be customized by admin users.

The applicationn includes authentication, role based access control and admin panel for managing statuses.

# Tech stack

## Backend
- Java 21
- Spring Boot
- Spring Security
- PostgreSQL
- Flyway
- Keycloak

## Frontend
- Angular
- Angular Material
- RxJS

## Infrastructure
- Docker

# Architecture

## Backend
In the project I tried to implement Hexagonal architecture to distinguish between 
domain and application logic. Features directories are split into:
- Domain - business models, records and rules
- Application - use cases and services
- Adapters - Rest controllers and JPA repositories

## Frontend
Frontend uses a simple feature-based structure:
- core - general auth, guards and interceptors
- features - app features with their components pages and data-access utilities

# Running the application

### Prerequisites
Make sure you have installed:
- Java 21
- Node.js (v18+)
- Maven
- Docker
- Angular CLI

### Create enviroment file
In project root directory:
```bash
cp .env.example .env
```

### Run infrastructure
In project root directory:
```bash
docker compose up -d
```

### Run backend
From the project root directory:
```bash
cd backend
mvn spring-boot:run
```

### Run frontend
From the project root directory:
```bash
cd frontend/frontend-taskflow
ng serve
```

# Admin privileges
1. To grant admin privileges, depending on the value in your env (KEYCLOAK_PORT) go to
```console
localhost:${KEYCLOAK_PORT}
```
2. login to keycloak using the credentials fom your env file (KC_ADMIN_USER and KC_ADMIN_PASSWORD)
3. Select ```taskflow``` realm
4. Go to Users then select desired user, or create a new one after that go to role mapping.
5. Assign ADMIN realm role and save 