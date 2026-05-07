---
name: commit-and-release
description: Use when work is complete and user asks to commit, push, tag, or create a GitHub release with APK
---

# Commit and Release

## Workflow

### 1. Bump Version

In `app/build.gradle.kts`, increment `versionCode` by 1 and update `versionName`:
- Patch bump (bugfix/style): `1.2.0` → `1.2.1`
- Minor bump (new feature): `1.2.1` → `1.3.0`
- Major bump (breaking change): `1.3.0` → `2.0.0`

### 2. Build Release APK

```bash
mise run build:release
```

APK output: `app/build/outputs/apk/release/app-release.apk`

### 3. Commit

```bash
git add -A && git commit -m "type(scope): concise message"
```

Commit format per AGENTS.md:
- Types: `feat`, `fix`, `refactor`, `style`, `docs`, `test`, `chore`, `perf`
- Scope: affected area (`theme`, `about`, `news`, `favourites`, `settings`, `cache`, `legal`, `components`, `build`, `deps`)

### 4. Push + Tag

```bash
git push origin master
git tag -a vX.Y.Z -m "Short description" && git push origin vX.Y.Z
```

Tag name matches `versionName` prefixed with `v`.

### 5. GitHub Release

```bash
gh release create vX.Y.Z \
  app/build/outputs/apk/release/app-release.apk \
  --title "vX.Y.Z — Short Title" \
  --notes "### Changes
- Bullet list of what changed"
```

## Partial Workflows

Not every request needs the full flow. Match what the user asks:

| Request | Steps |
|---------|-------|
| "commit and push" | 3, 4 (no tag) |
| "commit, push, tag" | 3, 4 |
| "create a release" | 1–5 |
| "bump version and release" | 1–5 |

Only bump version and build release APK when creating a tag/release.
