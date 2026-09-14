#!/usr/bin/env bash
# Smoke check: confirms the running application serves its key pages.
#
#   BASE_URL  where the application is running (default http://localhost:8080)
#
# To check a new page, add a line to PAGES in the form "path|text it must contain".
# Only public pages belong here: pages behind login redirect to /login instead.
#
# Week 1 tutorial, Part 6: the /login line checks the exact login heading.
set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"

PAGES=(
  "/actuator/health|UP"
  "/|TUS Online Student Gallery"
  "/team|Our Team"
  "/login|<h1>Sign in to TUS Gallery</h1>"
)

echo "Waiting for $BASE_URL to start..."
for attempt in $(seq 1 60); do
  if curl -fs "$BASE_URL/actuator/health" > /dev/null; then
    echo "Application is up."
    break
  fi
  sleep 2
done

failed=0
for entry in "${PAGES[@]}"; do
  path="${entry%%|*}"
  expected="${entry#*|}"
  if body=$(curl -fs "$BASE_URL$path") && grep -qF "$expected" <<< "$body"; then
    echo "PASS $path contains \"$expected\""
  else
    echo "FAIL $path does not contain \"$expected\""
    failed=1
  fi
done

exit $failed
