#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
mkdir -p .toolchain/bin .toolchain/git-lfs
if [[ ! -x .toolchain/bin/git-lfs ]]; then
  curl --fail --location --retry 3 --output .toolchain/git-lfs.tar.gz https://github.com/git-lfs/git-lfs/releases/download/v3.8.1/git-lfs-linux-amd64-v3.8.1.tar.gz
  printf '%s\n' 'e72cf723a4a660438862282e8aecf3885fc8123e88bfcad534cf6b941a354b75  .toolchain/git-lfs.tar.gz' | sha256sum --check
  tar -xzf .toolchain/git-lfs.tar.gz --strip-components=1 -C .toolchain/git-lfs
  cp .toolchain/git-lfs/git-lfs .toolchain/bin/git-lfs
fi
export PATH="$PWD/.toolchain/bin:$PATH"
if [[ "${1:-}" == --download-only ]]; then
  exit 0
fi
git -C tdlib config filter.lfs.required true
git -C tdlib config filter.lfs.process "'$PWD/.toolchain/bin/git-lfs' filter-process"
git -C tdlib config filter.lfs.clean "'$PWD/.toolchain/bin/git-lfs' clean -- %f"
git -C tdlib config filter.lfs.smudge "'$PWD/.toolchain/bin/git-lfs' smudge -- %f"
git -C tdlib lfs pull
