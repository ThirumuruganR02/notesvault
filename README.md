# NotesVault

Full-stack secure notes app: **Spring Boot** API (JWT, encrypted note bodies, tags, search) + **React** UI.

## Prerequisites

- **Java 17+** and **Maven 3.9+**
- **Node.js 20+** (LTS) and **npm**
- **MySQL 8** — database `notesvault` is auto-created when allowed by your JDBC URL (see `backend/.../application.properties`)

## Run locally (two terminals)

### 1. Backend (port 8080)

```bash
cd backend
mvn spring-boot:run
```

### 2. Frontend (port 5173)

```bash
cd frontend
npm install
npm run dev
```

### Open the app

**Main UI:** [http://localhost:5173](http://localhost:5173)

- Register or sign in, then create notes, tags, filters, and search.
- The dev server **proxies** `/api` and `/notes` to `http://localhost:8080`, so keep the backend running.

**API docs (optional):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Production build (frontend)

```bash
cd frontend
npm install
npm run build
```

Serve the `frontend/dist` folder with any static host; set `VITE_API_BASE_URL` to your API origin and adjust API calls if you deploy separately (see `frontend/src/api/client.ts` for a single place to change the base URL later).

## Push to GitHub

```bash
git add .
git commit -m "Add NotesVault frontend and full-stack setup"
git remote add origin https://github.com/YOUR_USER/YOUR_REPO.git
git push -u origin main
```

Resume later: clone the repo, run backend + `npm install && npm run dev` in `frontend` as above.

## API overview

| Method | Path | Auth |
|--------|------|------|
| POST | `/api/auth/register` | No |
| POST | `/api/auth/login` | No |
| GET | `/notes?tag=&search=` | Bearer JWT |
| POST / PUT / DELETE | `/notes`, `/notes/{id}` | Bearer JWT |

## Tests (backend only)

```bash
cd backend && mvn test
```

Uses H2 in-memory; no MySQL required.
