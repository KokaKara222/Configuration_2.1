#!/usr/bin/env bash
set -eu
cd "$(dirname "$0")"
if [ "$#" -eq 0 ]; then
    exec ./gradlew --quiet run
fi
exec ./gradlew --quiet run --args="$*"
