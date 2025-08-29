# Apparel Store (Spring Boot + React)

A full‑stack reference application demonstrating a Spring Boot 3 backend with a React (Vite + TypeScript) frontend. The project follows opinionated guidelines for backend and frontend structure, typed configuration, DTO usage, MapStruct mappers, Flyway migrations, and OpenAPI-driven API documentation.

Last updated: 2025-08-29 20:37

## Project Layout

- Backend (Spring Boot): `src/main/java`, `src/main/resources`
- Frontend (React + Vite + TS): `src/main/frontend`
- OpenAPI specification: `openapi/openapi/openapi.yaml`
- Maven build (includes frontend build via frontend-maven-plugin): `pom.xml`

## Tech Stack

- Java 21, Spring Boot 3.4.x (web, data-jpa, validation)
- H2 (runtime), Flyway (migrations)
- Lombok, MapStruct (with Spring component model)
- React 19, Vite 6, TypeScript 5
- Radix UI + shadcn-style components, Tailwind CSS
- Jest + React Testing Library for frontend tests
- Redocly CLI for OpenAPI lint/preview

## Quick Start

Prerequisites:
- JDK 21
- Maven 3.9+ (wrapper included)
- Node/npm are auto-installed by Maven during build (frontend-maven-plugin)

Run the full stack (backend + serve built frontend):

```bash
./mvnw clean package
./mvnw spring-boot:run
```

Then open http://localhost:8080 — the built frontend is served from `target/classes/static`.

Developer workflow (run frontend dev server + backend together):

```bash
cd src/main/frontend
npm install
npm run dev:concurrent   # runs Vite dev server and backend together
```

- Frontend dev server runs on http://localhost:5173 by default and proxies API calls to the backend (see `.env*`).
- Alternatively, run backend on its own: `./mvnw spring-boot:run` and frontend: `npm run dev` in a separate terminal.

## Configuration

Backend (`src/main/resources/application.properties`):
- spring.jpa.open-in-view=false (OSIV disabled per best practices)
- Flyway enabled with baseline-on-migrate=true; migration location: `classpath:db/migration`
- H2 console enabled for local dev

Frontend environment:
- `.env`, `.env.development`, `.env.production` in `src/main/frontend`
- Key vars:
  - `VITE_API_BASE_URL` (defaults to `/api` for same-origin backend)
  - `VITE_ENABLE_DEBUG`, `VITE_ENABLE_MOCK_API`, timeouts, etc.

Node/npm versions (managed by Maven):
- node: v22.16.0
- npm: 11.4.0

## Build Details

The root `pom.xml` configures:
- frontend-maven-plugin
  - Installs Node/npm
  - `npm install`
  - `npm run build` (Vite) output to `target/classes/static` for Spring Boot to serve
- maven-clean-plugin cleans front-end build artifacts
- MapStruct annotation processor with Spring default component model

Frontend scripts (run from `src/main/frontend`):
- `npm run dev` – Vite dev server
- `npm run build` – Type-check then Vite production build
- `npm run preview` – Preview built assets
- `npm run generate-api` – Generate typed API client from OpenAPI spec
- `npm run watch-api` – Watch OpenAPI files and regenerate client
- `npm run dev:concurrent` – Run frontend dev server and backend together

## OpenAPI

- Main file: `openapi/openapi/openapi.yaml`
- Lint / preview / bundle (run in `openapi` folder):

```bash
cd openapi
npm install
npm test         # redocly lint
npm start        # redocly preview-docs (local preview)
npm run build    # redocly bundle -o dist/bundle.yaml
```

- Frontend client generation: `npm run generate-api` in `src/main/frontend` will read the OpenAPI YAML and generate `src/api` with Axios-based client typings.

## Testing

Backend tests:
```bash
./mvnw test
```

Frontend tests (run in `src/main/frontend`):
```bash
cd src/main/frontend
npm test
npm run test:coverage
```

- Jest is configured with jsdom and React Testing Library. CSS modules are mocked.
- Integration tests exist under `src/__tests__/integration` (e.g., ApparelCreate.integration.test.tsx).

## Development Guidelines Applied

- Constructor injection for Spring services/config (no field/setter injection)
- Package-private visibility where possible for controllers/config
- Typed properties and validation; fail-fast configuration
- Clear transaction boundaries; readOnly where appropriate
- OSIV disabled to avoid N+1 and lazy-loading pitfalls
- DTOs for web layer; MapStruct mappers for conversions and updates
- REST conventions, versioned URLs under `/api/v1`
- Centralized exception handling recommendation (`@RestControllerAdvice`)
- Flyway migrations under `src/main/resources/db/migration` (use H2-compatible SQL)

## H2 & Flyway

- H2 console: http://localhost:8080/h2-console (enable only for dev)
- Default JDBC URL examples (if needed): `jdbc:h2:mem:testdb` or `jdbc:h2:file:./data/apparel`
- Place new Flyway scripts in `src/main/resources/db/migration` using `V<version>__<description>.sql` naming.

## Troubleshooting

- Frontend build not found in production: ensure `./mvnw clean package` built assets into `target/classes/static`.
- OpenAPI codegen errors: run `npm test` in `openapi` to lint, then regenerate client with `npm run generate-api` in the frontend.
- MapStruct mapping issues: make sure annotation processing is enabled and re-run `./mvnw clean compile`.
- Port conflicts in dev: use `dev:concurrent` or run frontend and backend on separate ports; Vite runs at 5173, backend at 8080.

## Useful Commands

- Build everything: `./mvnw clean package`
- Run backend only: `./mvnw spring-boot:run`
- Frontend dev: `cd src/main/frontend && npm run dev`
- Lint OpenAPI: `cd openapi && npm test`