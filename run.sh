#!/usr/bin/env bash
set -eu
cd "$(dirname "$0")"
if [ "$#" -eq 0 ]; then
    exec gradle --quiet run
fi
exec gradle --quiet run --args="$*"