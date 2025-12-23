# Spring Boot (Java 17 + Maven) - Render deploy (Docker) + GitHub Actions

This repo contains a simple **Spring Boot** app.
This variant is prepared for **Render Docker Web Service**.

## Run locally

```bash
mvn clean package
java -jar target/*.jar
```

Open:
- http://localhost:5000

(Port is configured as `server.port=${PORT:5000}`.)

## Deploy (Render + GitHub Actions)

Read the beginner guide:
- `RENDER_STEP_BY_STEP.md`

Docker-specific notes:
- Render Runtime: **Docker**
- Render will build `Dockerfile` and run the container.
- GitHub Actions triggers deploy using a Render **Deploy Hook** secret: `RENDER_DEPLOY_HOOK`.

Workflow:
- `.github/workflows/render.yml`
