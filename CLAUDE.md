# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a network packet analysis service built with a dockerized microservices architecture. The system ingests Wireshark CSV packet captures, stores them in PostgreSQL, and provides REST APIs for network traffic analysis (DNS, HTTP, TCP).

**Stack:**
- Backend: Spring Boot 3.3.4 (Java 21, Gradle, Kotlin DSL)
- Frontend: React 19 + Vite 7
- Database: PostgreSQL 16
- Reverse Proxy: Nginx
- Orchestration: Docker Compose

## Architecture

### Service Communication Flow
```
Client → Nginx (:80) → Frontend (:80) | Backend (:8080) → PostgreSQL (:5432)
                         /             /api/*
```

The reverse-proxy (nginx.conf) routes:
- `/` → frontend service (React static files)
- `/api/*` → backend service (Spring Boot REST APIs)
- `/healthz` → nginx health check

All services communicate via the `app_net` Docker network and are not directly exposed except through the reverse-proxy on `${HTTP_PORT}` (default: 8088).

### Backend Package Structure
```
com.example.demo/
├── DemoApplication.java (main class)
├── HelloController.java
└── net/
    ├── NetController.java       # REST endpoints for packet ingestion & analysis
    ├── NetCsvService.java       # CSV parsing & protocol extraction logic
    ├── NetPacketEntity.java     # JPA entity mapped to net_packets table
    └── NetPacketRepository.java # Spring Data JPA repository
```

### Data Model (NetPacketEntity)
The `net_packets` table stores parsed packet data with protocol-specific fields:
- Core: `ts_utc`, `time_rel`, `src`, `dst`, `protocol`, `length`, `info`
- TCP: `tcp_flags` (e.g., "SYN, ACK")
- HTTP: `http_method`, `http_host`, `http_path`, `http_status`
- DNS: `dns_query`, `dns_answer`

Timestamps are auto-generated as `OffsetDateTime.now()` on insert. Wireshark's relative time is stored in `time_rel`.

### CSV Ingestion Pipeline
NetCsvService.ingestWiresharkCsv() expects Wireshark CSV exports with columns:
`Time, Source, Destination, Protocol, Length, Info`

The service parses the `Info` field to extract:
- TCP flags from `[SYN, ACK]` patterns
- HTTP methods/paths/status from request/response lines
- DNS queries/answers from "Standard query" patterns

## Development Commands

### Environment Setup
```bash
# Create .env from template
cp .env.example .env
# Edit DB_PASSWORD and other vars as needed
```

### Running the Full Stack
```bash
# Start all services (db, backend, frontend, reverse-proxy)
docker-compose up --build

# Access application at http://localhost:8088
# Backend health: http://localhost:8088/api/actuator/health
# Nginx health: http://localhost:8088/healthz
```

### Backend Development
```bash
# From ./backend directory

# Build
./gradlew build

# Run locally (requires local PostgreSQL or update application.yml)
./gradlew bootRun

# Clean build artifacts
./gradlew clean
```

### Frontend Development
```bash
# From ./frontend directory

# Install dependencies
npm install

# Run dev server with HMR (default: http://localhost:5173)
npm run dev

# Build for production
npm run build

# Lint
npm run lint

# Preview production build
npm run preview
```

### Database Access
```bash
# Connect to running PostgreSQL container
docker exec -it app_db psql -U appuser -d appdb

# Useful queries
SELECT protocol, count(*) FROM net_packets GROUP BY protocol;
SELECT * FROM net_packets WHERE protocol = 'DNS' LIMIT 10;
```

## API Endpoints

All backend APIs are prefixed with `/api/net`:

### POST /api/net/ingest/wireshark-csv
Upload Wireshark CSV file for ingestion.
- Content-Type: `multipart/form-data`
- Form field: `file`
- Returns: `{"ingested": <count>}`
- Max file size: 50MB (configured in docker-compose.yml)

### GET /api/net/latest?limit=50
Retrieve latest packets (default 50, max 500).
- Returns: Array of `{ts_utc, src, dst, protocol, length, info}`

### GET /api/net/analysis/dns?seconds=3600
DNS analysis for time window (default 1 hour).
- Returns: Array of `{host, queries, answers}` grouped by `dns_query`

### GET /api/net/analysis/http?seconds=3600
HTTP analysis for time window.
- Returns: Array of `{kind, host, path, status, cnt}` where `kind` is method or "RESP"

### GET /api/net/analysis/tcp?seconds=3600
TCP flag distribution for time window.
- Returns: `{syn, syn_ack, ack_only, rst, total_tcp}`

## Configuration Notes

### Environment Variables (.env)
- `HTTP_PORT`: Exposed port for reverse-proxy (default: 8088)
- `DB_NAME`, `DB_USER`, `DB_PASSWORD`: PostgreSQL credentials
- `SPRING_PROFILE`: Spring profile (typically "prod")
- `CORS_ORIGINS`: Allowed CORS origins for backend

### JPA DDL Mode
Backend uses `hibernate.ddl-auto: update` which auto-creates/updates tables on startup. For production, consider using migrations (Flyway/Liquibase).

### Health Checks
- Backend health check polls `/actuator/health` every 10s (15 retries)
- Database health check uses `pg_isready` every 5s (10 retries)
- Services depend on health checks via `condition: service_healthy`

## Git Workflow

- Main branch: `main`
- Development branch: `develop` (current)
- Recent work includes ping packet handling and CSV analysis features

## Common Issues

### Port Conflicts
If port 8088 is in use, change `HTTP_PORT` in `.env` before running docker-compose.

### CSV Upload Fails
Ensure CSV has exact column headers: `Time, Source, Destination, Protocol, Length, Info`. Case-sensitive.

### Database Connection Issues
Check that `DB_HOST`, `DB_PORT`, `DB_NAME` environment variables match between backend and db service in docker-compose.yml.
