# Setup — IntelliJ IDEA

Get the app running, tests passing, and Git working without leaving the IDE. Prefer the terminal? Use [SETUP-CLI.md](SETUP-CLI.md) instead.

## Before you start

1. **IntelliJ IDEA Ultimate**, free with your student email: https://www.jetbrains.com/community/education/
   Ultimate matters here — Spring Boot run configurations, the Thymeleaf editor and the Services/Docker tool windows are Ultimate-only. Community Edition works too; see the notes at the bottom.
2. **Docker Desktop** — for MySQL from Week 3.
3. You do **not** need to install Java or Maven separately. IntelliJ downloads JDK 17 for you and the repo ships the Maven wrapper.

## 1. Clone the repo from IntelliJ

Welcome screen → **Get from VCS** → paste your team repo URL → **Clone**.

Already cloned? **File → Open**, and select the folder containing `pom.xml` — not a file inside it.

IntelliJ detects the Maven project and imports it. Wait for "Importing Maven projects" in the status bar to finish before touching anything — the first import downloads every dependency and takes a few minutes.

## 2. Set the JDK

**File → Project Structure → Project** → set **SDK** to a **Java 17** JDK. This is the version the Dockerfile and CI use, so matching it means your local build behaves like theirs.

No JDK 17 in the list? Open the dropdown → **Download JDK** → version 17, vendor Eclipse Temurin.

## 3. Switch to `develop`

Click the **branch widget** in the bottom-right status bar (or **Git → Branches**) → under *Remote*, pick `origin/develop` → **Checkout**.

## 4. Start MySQL

Open `docker-compose.yml` and click the **green ▶ in the gutter** beside the `db:` service to start just that container. It appears in the **Services** tool window.

> Not needed until Week 3 — start it now anyway, so you hit any Docker problems this week instead of mid-sprint.

## 5. Run the app

Open `src/main/java/ie/tus/gallery/GalleryApplication.java` and click the **green ▶** beside the class declaration → **Run 'GalleryApplication'**.

`dev` is the default profile, so it just works. Set it explicitly anyway on the run configuration IntelliJ just created — **Run → Edit Configurations** → select `GalleryApplication` → **Active profiles**: `dev` → **OK** — so that later, when you need `test` or `prod`, you already know where that field lives.

Open http://localhost:8080 and log in with `student1 / Password123!`.

Stop with the red ■ in the **Run** tool window; restart with **Shift+F10**.

## 6. Run the tests

Right-click `src/test/java` → **Run 'Tests in gallery'**. All green means your dev environment works.

While you're working, click the ▶ beside a single test class to run just that one — much faster than the full suite.

> **Checkpoint ✅** App running locally, tests passing.

## Day-to-day Git in IntelliJ

| Task | IntelliJ | Shortcut |
| --- | --- | --- |
| Get latest `develop` | **Git → Update Project** | Ctrl+T / ⌘T |
| New branch | Branch widget → **New Branch from 'develop'** | — |
| See what you changed | **Commit** tool window | Alt+0 / ⌘0 |
| Commit | Commit window → tick files, write message → **Commit** | Ctrl+K / ⌘K |
| Push | **Git → Push** | Ctrl+Shift+K / ⌘⇧K |
| Switch branch | Branch widget → branch → **Checkout** | — |
| Review history | **Git** tool window → **Log** tab | Alt+9 / ⌘9 |

Name branches in the New Branch dialog exactly as the rules require: `feature/<issue-number>-<short-name>`.

**Reviewing a teammate's PR**: enable the bundled **GitHub** plugin, then **Git → GitHub → View Pull Requests**. You can read the diff, comment on lines, and check out their branch to run it — which the review rules require you to actually do.

## Running the CI build locally

CI runs Maven, not IntelliJ, so this is the command that decides whether your PR goes green. Use the **Terminal** tool window (Alt+F12):

```bash
./mvnw verify
```

If it passes in IntelliJ but fails in CI, run that first — the difference is nearly always the answer. Full command reference in [SETUP-CLI.md](SETUP-CLI.md).

## Community Edition notes

Community Edition has no Spring Boot run configuration, so:

- Run `GalleryApplication` as an ordinary Java application — the gutter ▶ still works.
- Set the profile by hand: **Edit Configurations → Modify options → Add VM options** → `-Dspring.profiles.active=dev`.
- Docker support isn't bundled. Either install the **Docker** plugin from Settings → Plugins, or start MySQL from the terminal with `docker compose up -d db`.
- Thymeleaf templates get no autocomplete or navigation. They still render fine.

Everything else here is identical.

## Troubleshooting

| Symptom | Fix |
| --- | --- |
| Red squiggles everywhere, imports unresolved | Maven import didn't finish. **Maven** tool window → **Reload All Maven Projects** |
| `Invalid source release: 17` | Wrong JDK. **Project Structure → Project → SDK** → Java 17 |
| Port 8080 already in use | An old run is still going. Red ■ in the **Run** window, or check **Services** |
| App starts but login fails | Active profile isn't `dev`. Check **Edit Configurations** |
| `Cannot connect to the Docker daemon` | Docker Desktop isn't running. Start it, then retry |
| Changes to a template don't show | Rebuild with Ctrl+F9 / ⌘F9, then refresh the browser |
