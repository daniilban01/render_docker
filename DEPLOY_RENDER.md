# Deploy to Render (Docker runtime) - manual steps

This file is a shorter checklist. For a full beginner walkthrough, read:
- `RENDER_STEP_BY_STEP.md`

## Render setup

1. Render Dashboard -> **New +** -> **Web Service**
2. Connect GitHub and select the repo
3. Configure:
   - Runtime/Environment: **Docker**
   - Branch: `main`
4. Create service

## Deploy Hook

1. Open Render service -> **Settings**
2. **Deploy Hooks** -> Create hook
3. Copy the hook URL

## GitHub Secret

1. GitHub repo -> Settings -> Secrets and variables -> Actions
2. Add secret:
   - Name: `RENDER_DEPLOY_HOOK`
   - Value: (the hook URL)

## Deploy

Push to `main` -> GitHub Actions runs -> triggers Render deploy.
