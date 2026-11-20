#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
./scripts/compile.sh
java -cp build/classes app.Main
