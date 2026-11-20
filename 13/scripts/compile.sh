#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
mkdir -p build/classes
find src/main/java -name '*.java' -print > build/sources.txt
javac -d build/classes @build/sources.txt
