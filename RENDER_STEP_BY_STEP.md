# Deploy Spring Boot on Render (FREE) with GitHub Actions - step by step (beginner friendly)

This repo is a simple **Java Spring Boot** app built with **Maven**.
Goal: **CI** runs on every push, and **CD** triggers a deploy on **Render** when code reaches `main`.

You have 2 valid setups:

- **Option A (NO Docker)**: Render uses *Build Command* + *Start Command*.
- **Option B (WITH Docker)**: Render builds the `Dockerfile`.

Both options can use the same GitHub Actions workflow:
- It runs `mvn clean package` (so you see build errors early)
- Then it triggers a Render deploy via a **Deploy Hook** (a webhook URL stored as a GitHub Secret)

---

## 0) Quick overview (what happens when you push)

1. You push code to GitHub (branch `main`).
2. GitHub Actions runs:
   - checkout code
   - install Java
   - run `mvn clean package`
   - if success -> calls the Render Deploy Hook URL
3. Render receives the hook and redeploys the service.

---

## 1) Prerequisites

- A GitHub account + this repo on GitHub
- A Render account
- Java version: **17**
- Maven build works locally

---

## 2) Run locally (to be sure it works)

From repo root:

```bash
mvn clean package
java -jar target/*.jar
```

Open in browser:

- http://localhost:5000

Note: this project uses:

- `server.port=${PORT:5000}`

So locally it runs on **5000**.
In Render, Render sets `PORT`, so the app will bind on the correct port automatically.

---

## 3) Option A - Deploy to Render WITHOUT Docker (recommended for beginners)

### 3.1 Create the Render Web Service

1. Login to Render Dashboard
2. Click **New +** -> **Web Service**
3. Connect your **GitHub** account (if asked)
4. Pick the repo

### 3.2 Configure settings

Set:

- **Runtime / Environment**: `Java`
- **Branch**: `main`
- **Build Command**:
  ```bash
  mvn clean package
  ```
- **Start Command**:
  ```bash
  java -jar target/*.jar
  ```

Important:
- Render will set `PORT`. Because we have `server.port=${PORT:5000}`, Spring Boot will automatically listen on Render's port.

5. Click **Create Web Service**

Wait until first deploy is green.

### 3.3 Create a Deploy Hook in Render

1. Open your service in Render
2. Go to **Settings**
3. Find **Deploy Hooks**
4. Create a new deploy hook (give it a name)
5. Copy the generated URL (looks like a webhook)

---

## 4) Option B - Deploy to Render WITH Docker

### 4.1 Create the Render Web Service

1. Login to Render Dashboard
2. Click **New +** -> **Web Service**
3. Connect GitHub
4. Pick the repo

### 4.2 Configure settings

Set:

- **Runtime / Environment**: `Docker`
- **Branch**: `main`

Render will build the `Dockerfile` from the repo.

Notes:
- The Docker image starts Spring Boot like this:
  - `java -Dserver.port=${PORT:-5000} -jar /app/app.jar`
- So it will listen on the correct Render port.

5. Click **Create Web Service**

Wait until first deploy is green.

### 4.3 Create a Deploy Hook in Render

Same as Option A:
- Service -> Settings -> Deploy Hooks -> Create -> Copy URL

---

## 5) GitHub setup (for BOTH options)

### 5.1 Add the Deploy Hook as GitHub Secret

1. In GitHub repo: **Settings**
2. **Secrets and variables** -> **Actions**
3. Click **New repository secret**
4. Add:

- Name: `RENDER_DEPLOY_HOOK`
- Value: (paste the Deploy Hook URL from Render)

### 5.2 Workflow file

The workflow should exist here:

- `.github/workflows/render.yml`

It will:
- build with Maven
- trigger Render deploy

---

## 6) Deploy test (does it work?)

1. Make a small change (ex: edit README)
2. Commit and push to `main`
3. In GitHub repo -> **Actions** tab
4. Open the workflow run
5. If it is green, Render should start a new deploy automatically.

In Render:
- open service -> **Deploys**
- you should see a new deploy triggered by the hook.

---

## 7) Troubleshooting (common issues)

### "Build failed" on GitHub Actions
- open the Actions logs
- fix Maven errors locally first:
  ```bash
  mvn clean package
  ```

### Render deploy triggered but service fails to start
- open Render service -> Logs
- common problems:
  - wrong Java version
  - app not binding to Render `PORT`
  - missing env vars

### Port problems
This repo uses:

- `server.port=${PORT:5000}`

So in Render it should bind correctly.

If you override Start Command, make sure you do NOT force a fixed port that ignores `PORT`.

---

## 8) What you should explain to a beginner (short)

- CI = build/test automatically on every push
- CD = deploy automatically when CI is successful
- Render Deploy Hook = a URL that tells Render "redeploy now"
- GitHub Secret = safe place to store the hook URL so it is not visible in code
