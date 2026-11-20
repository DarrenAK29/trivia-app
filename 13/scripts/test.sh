#!/bin/sh
set -eu
cd "$(dirname "$0")/.."
./scripts/compile.sh
mkdir -p build/test-classes
find src/test/java -name '*.java' -print > build/test-sources.txt
javac -cp build/classes -d build/test-classes @build/test-sources.txt
java -cp build/classes:build/test-classes QuizInteractorTest
java -cp build/classes:build/test-classes QuizPresenterTest
java -cp build/classes:build/test-classes QuizControllerTest
