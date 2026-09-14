# Setup — Command Line

Get the app running, the build passing, and Git working from the terminal with the editor of your choice. Prefer an IDE to do it for you? Use [SETUP-INTELLIJ.md](SETUP-INTELLIJ.md) instead.

These are also the exact commands CI runs, so this guide is the reference when a PR goes red.

## Before you start

You need three things installed:

| Tool | Check it works | Notes |
| --- | --- | --- |
| **JDK 17** | `java -version` → `17.x` | Same version as the Dockerfile and CI. [Adoptium](https://adoptium.net/) is the easy install |
| **Docker** | `docker --version` | Docker Desktop on Windows/macOS; Docker Engine on Linux |
| **Git** | `git --version` | — |

You do **not** need Maven — the repo ships the Maven wrapper (`mvnw`), which downloads the right version itself.

> **Windows**: every `./mvnw` below is `mvnw.cmd` in PowerShell or CMD. In Git Bash, `./mvnw` works as written.

## 1. Clone and switch to `develop`

```bash
git clone git@github.com:<org>/<team-repo>.git
cd <team-repo>
git switch develop
```

## 2. Start MySQL

```bash
docker compose up -d db
```

Confirm it's up with `docker compose ps`.

> Not needed until Week 3 — start it now anyway, so you hit any Docker problems this week instead of mid-sprint.

## 3. Run the app

```bash
./mvnw spring-boot:run
```

The `dev` profile is the default, so no extra flags. To be explicit, or to run another profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Open http://localhost:8080 and log in with `student1 / Password123!`.

Stop it with **Ctrl+C**.

## 4. Run the build and the smoke check

```bash
./mvnw verify
```

This compiles the app and runs its one test, which only checks that the app starts. There are no other tests to run.

With the app running (step 3, in another terminal), run the smoke check — the same script CI runs:

```bash
bash .github/smoke-check.sh
```

Every line should say `PASS`. On Windows, run it from **Git Bash**, not PowerShell.

> **Checkpoint ✅** App running locally, build passing, smoke check passing.

## The command CI runs

```bash
./mvnw verify
```

CI runs this first, then starts the app and runs the smoke check. It compiles the app, checks it starts, and packages it. Run it — and the smoke check — before you push and you'll catch most red builds on your own machine.

## Day-to-day Git

```bash
# Get latest develop, then branch from it
git switch develop
git pull
git switch -c feature/1-add-yourname-to-team-page

# See what you changed
git status
git diff

# Stage, commit, push
git add src/main/resources/templates/team.html
git commit -m "Add yourname to team page (#1)"
git push -u origin feature/1-add-yourname-to-team-page
```

Then open the PR on GitHub — into `develop`, never `main`.

**Reviewing a teammate's PR** means running their code, not just reading it:

```bash
git fetch origin
git switch feature/1-add-partnername-to-team-page
./mvnw spring-boot:run
```

Optional but worth it: the [GitHub CLI](https://cli.github.com/) (`gh`) lets you do the whole PR cycle from the terminal — `gh pr create`, `gh pr checkout <number>`, `gh pr review`, `gh run watch` to follow CI live.

## Troubleshooting

| Symptom | Fix |
| --- | --- |
| `mvnw: Permission denied` | `chmod +x mvnw` (macOS/Linux, first clone only) |
| `Invalid source release: 17` | Wrong JDK on your PATH. Check `java -version`; set `JAVA_HOME` to a 17 JDK |
| `'mvnw' is not recognized` | You're in PowerShell/CMD — use `.\mvnw.cmd` |
| Port 8080 already in use | An old run is still going. macOS/Linux: `lsof -i :8080` then `kill <pid>`. Windows: `netstat -ano \| findstr :8080` then `taskkill /PID <pid> /F` |
| `Cannot connect to the Docker daemon` | Docker Desktop isn't running. Start it, then retry |
| CI's smoke check fails but the app works for you | Run `bash .github/smoke-check.sh` against your running app — the `FAIL` line names the page and the text it expected |
| `$'\r': command not found` running the smoke check | The script has Windows line endings. Run `git add --renormalize .` and commit |
