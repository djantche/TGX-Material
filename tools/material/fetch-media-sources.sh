#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
for flavor in latest legacy lollipop; do
  module="app/thirdparty/androidx-media/$flavor"
  commit=$(git ls-tree HEAD "$module" | awk '{print $3}')
  git -C "$module" config remote.origin.promisor true
  git -C "$module" config remote.origin.partialclonefilter blob:none
  git -C "$module" sparse-checkout set --cone \
    libraries/decoder_ffmpeg/src/main \
    libraries/decoder_flac/src/main \
    libraries/decoder_opus/src/main \
    libraries/decoder_vp9/src/main
  git -C "$module" fetch --filter=blob:none --depth=1 origin "$commit"
  git -C "$module" checkout --detach "$commit"
done
