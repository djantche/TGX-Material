#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
mkdir -p artifacts/test-classes
javac --release 17 -d artifacts/test-classes app/src/main/java/org/thunderdog/challegram/theme/MaterialColorScheme.java tools/material/tests/MaterialColorSchemeTest.java
java -cp artifacts/test-classes MaterialColorSchemeTest "$@"
git diff --check --ignore-submodules=all
