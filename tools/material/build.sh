#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
export JAVA_HOME="$PWD/.toolchain/jdk"
export ANDROID_HOME="$PWD/.toolchain/android"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export GRADLE_USER_HOME="$PWD/.toolchain/gradle"
export RUSTUP_HOME="$PWD/.toolchain/rustup"
export CARGO_HOME="$PWD/.toolchain/cargo"
export PATH="$PWD/.toolchain/bin:$JAVA_HOME/bin:$CARGO_HOME/bin:$PATH"
export CMAKE_BUILD_PARALLEL_LEVEL=4
export CARGO_BUILD_JOBS=4
./gradlew :app:assembleLatestDebug -Pabi=arm64 --max-workers=4 '-Dorg.gradle.jvmargs=-Xmx4G -XX:ActiveProcessorCount=4 -Dfile.encoding=UTF-8' "$@"
