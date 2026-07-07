# CLAUDE.md — AnotherCastle Component

This file defines the AnotherCastle component within BlackHole. Start every session by reading **CLAUDE_SHARED.md** in blackhole-private/, then read this file.

---

## What AnotherCastle Is

AnotherCastle is the **Capture layer** of BlackHole. It's a suite of client applications that:

1. **Route URLs silently** — Register at the OS level as a browser. When a link is tapped (from email, messaging, social media, etc.), intercept it and route to the correct browser based on user-defined domain rules.

2. **Provide optional ingestion** — After routing, offer a non-intrusive overlay to save the URL to Toadstool with project/collection and quick tags.

3. **Store local state** — Domain routing rules, browser mappings, and bookmarks are stored locally first, with optional sync to Toadstool.

**Primary function:** Silent URL routing.  
**Secondary function:** Optional ingestion to Toadstool.

---

## Repository Structure

```
anothercastle/
├── CLAUDE.md                  (This file)
├── android/                   (Kotlin + Jetpack Compose)
├── ios/                       (Swift, planned for v1+)
├── desktop-win/               (Tauri, planned for v1+)
├── desktop-mac/               (Tauri, planned for v1+)
├── desktop-lnx/               (Tauri, planned for v1+)
└── browser-extension/         (JavaScript, planned for v1+)
```

This is a **monorepo** for all AnotherCastle clients. They share design principles, API contracts, and documentation, but each platform has independent code.

---

## Tech Stack

### Android (Primary in v1)
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + StateFlow
- **Min SDK:** API 26 (Android 8.0)
- **Package:** `dev.anothercastle.app`
- **Dependencies:** Room (local storage), coroutines

### Desktop (Planned for v1+)
- **Framework:** Tauri (cross-platform, minimal overhead)
- **Language:** Rust (core) + HTML/CSS/JavaScript (UI)
- **Local storage:** SQLite or similar

### iOS (Planned for v1+)
- **Language:** Swift
- **Framework:** SwiftUI
- **Min version:** iOS 14

### Browser Extension (Planned for v1+)
- **Language:** JavaScript (Manifest v3)
- **Target:** Chrome, Firefox, Safari
- **Storage:** Browser local storage, optional sync to Toadstool

---

## Architecture & Design

### URL Routing (Core Feature)

When a URL arrives:

1. Check if a domain rule exists for that URL
2. If yes → route silently to the mapped browser, finish
3. If no → check for a default browser
4. If yes → route silently to default, finish
5. If no → show browser picker UI (fallback)

**Silent passthrough is the default.** The picker UI is a fallback, not the primary experience.

### Domain Rules

Rules are stored locally:
- Format: `{ domain_pattern, browser_package, browser_activity }`
- Example: `{ "slack.com", "com.google.android.apps.chromium", "...MainActivityChrome" }`
- Matching: domain patterns can be exact (`slack.com`) or wildcards (`*.slack.com`)

### Optional Ingestion

After routing, an optional overlay appears:
- "Save this URL?" with project/collection and tag input
- Dismissible in one tap (defaults to no)
- If accepted, POST to Toadstool ingest API with:
  - `url` (string)
  - `source` (e.g., "anothercastle-android")
  - `tags` (array)
  - `collection_id` (optional)

### Bookmark Sync (Future)

Bookmarks and domain rules can be synced to Toadstool for cross-device access. In v1, this is storage only; sync logic comes later.

---

## Current State

### Android App
- ✅ Intent filter registration (registers as browser)
- ✅ Browser picker fallback UI
- ✅ PackageManager querying
- ✅ Basic Compose theme

**Missing:**
- Domain routing rules system (data model + Room DB)
- Silent passthrough logic
- Settings screen
- Quick capture overlay
- Toadstool API client

### Other Platforms
- Placeholder folders only. iOS, desktop, and browser extension are v1+ scope.

---

## Current Phase

**Phase 0.1 & 0.2: Toadstool Foundation**

AnotherCastle is not actively being developed in phase 0.1. Priority is building Toadstool's skeleton and API contract. Once that's solid, AnotherCastle development can resume (phase 0.3: URL routing).

**Phase 0.3: AnotherCastle URL Routing (Android)**

Build out the Android app:
1. Domain rules data model + Room DB
2. Silent passthrough logic
3. Settings screen for managing rules
4. Default browser configuration

**Phase 0.4: Optional Ingestion (Android)**

Add the quick capture overlay and Toadstool API client integration.

---

## Code Style & Patterns

### Kotlin/Android
- **Architecture:** MVVM with StateFlow (not LiveData)
- **Naming:** camelCase for variables/functions, PascalCase for classes
- **Composables:** Single-responsibility; extract complex UI into smaller composables
- **Async:** Use coroutines + Flow; never block the main thread
- **Database:** Use Room DAO pattern; expose data via Flow<>

### Dependency Management
- Update both `build.gradle.kts` AND `libs.versions.toml` when adding dependencies
- Keep versions centralized in `libs.versions.toml`
- Prefer stable, widely-used libraries

### Commits
- Use conventional commit messages: `feat:`, `fix:`, `chore:`, `refactor:`, `test:`
- Example: `feat: add domain routing rules storage` or `fix: handle null URL in MainActivity`

---

## Key Rules for AnotherCastle Development

1. **Offline-first always** — Nothing in AnotherCastle should require Toadstool to be running. Routing works without network.

2. **Silent routing is primary** — The picker UI is a fallback. Optimize for the path where routing rules exist and passthrough is seamless.

3. **Toadstool integration is opt-in and additive** — Users can route URLs forever without ever configuring a Toadstool instance.

4. **Keep local state local** — Domain rules, bookmarks, and user settings live on the device first. Sync to Toadstool is optional.

5. **Preserve user intent** — If a user sets a default browser, route to it silently. Never surprise them with a picker unless necessary.

---

## What to Ask Before Doing

- **Adding a new dependency:** Ask before adding. Keep the list lean.
- **Major UI restructure:** Ask. Describe what's changing and why.
- **Changing the routing logic:** Ask. Show me the new decision tree.
- **Adding a new platform (iOS, desktop, etc.):** Ask. Even though folders exist, building a new platform needs design work first.
- **Changing the domain rule format or storage:** Ask. This affects all platforms eventually.

---

## What NOT to Do

- Do not make Toadstool a hard requirement for any feature
- Do not refactor unrelated code while working on a feature
- Do not change the manifest without understanding Android browser registration
- Do not skip the settings screen for domain rules—users need a way to configure routing
- Do not assume the picker UI is "good enough"—silent passthrough is the goal

---

## Context & References

- **CLAUDE_SHARED.md** — Shared principles and working agreement
- **blackhole-private/platform-docs/ANOTHERCASTLE.md** — Detailed technical overview
- **blackhole-private/platform-docs/DESIGN_p0.0.md** — Phase strategy and architecture
- **blackhole-private/platform-docs/user-stories/** — Product requirements

---

*Update this file when tech stack decisions change or when a new platform enters active development.*
