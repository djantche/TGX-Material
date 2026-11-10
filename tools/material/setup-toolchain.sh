#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
project_dir="$PWD"
toolchain_dir="$project_dir/.toolchain"
mkdir -p "$toolchain_dir/jdk" "$toolchain_dir/android"
if [[ ! -x "$toolchain_dir/jdk/bin/java" ]]; then
  curl --fail --location --retry 3 --output "$toolchain_dir/jdk.tar.gz" 'https://api.adoptium.net/v3/binary/latest/25/ga/linux/x64/jdk/hotspot/normal/eclipse'
  tar -xzf "$toolchain_dir/jdk.tar.gz" --strip-components=1 -C "$toolchain_dir/jdk"
fi
export JAVA_HOME="$toolchain_dir/jdk"
export ANDROID_HOME="$toolchain_dir/android"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export GRADLE_USER_HOME="$toolchain_dir/gradle"
export RUSTUP_HOME="$toolchain_dir/rustup"
export CARGO_HOME="$toolchain_dir/cargo"
export PATH="$JAVA_HOME/bin:$CARGO_HOME/bin:$PATH"
if [[ ! -d "$ANDROID_HOME/licenses" ]]; then
  cp -R /opt/android-sdk/licenses "$ANDROID_HOME/licenses"
fi
/opt/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root="$ANDROID_HOME" 'platforms;android-37.2' 'build-tools;37.0.0' 'ndk;30.0.16248370' 'cmake;3.22.1'
if [[ ! -x "$CARGO_HOME/bin/rustup" ]]; then
  curl --fail --location --retry 3 --output "$toolchain_dir/rustup-init" https://static.rust-lang.org/rustup/dist/x86_64-unknown-linux-gnu/rustup-init
  chmod +x "$toolchain_dir/rustup-init"
  "$toolchain_dir/rustup-init" -y --no-modify-path --profile minimal --default-toolchain 1.99.1
fi
rustup toolchain install 1.99.1 --profile minimal
rustup target add --toolchain 1.99.1 aarch64-linux-android
if [[ ! -f local.properties ]]; then
  python3 - <<'PY'
from pathlib import Path
root = Path.cwd()
config = (root / 'local.properties.sample').read_text()
config = config.replace('sdk.dir=', 'sdk.dir=' + str(root / '.toolchain/android'), 1)
config = config.replace('rust.dir=', 'rust.dir=' + str(root / '.toolchain/rustup'), 1)
config = config.replace('app.id=org.example.tgx', 'app.id=dev.davcodeur.tgx.material')
config = config.replace('app.name=TGX Example', 'app.name=TGX Material')
config += '\napp.experimental=true\n'
(root / 'local.properties').write_text(config)
PY
fi
printf 'Toolchain ready: %s\n' "$toolchain_dir"
