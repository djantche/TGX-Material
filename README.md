# TGX Material

A modern, airy **Material 3** redesign of [Telegram X](https://github.com/TGX-Android/Telegram-X), the experimental Telegram client built on [TDLib](https://core.telegram.org/tdlib).

TGX Material keeps everything Telegram X does well (speed, TDLib, encryption, calls, media) and reworks how it looks and feels: Material You colors, rounded surfaces, lighter typography, grouped settings, pill navigation and a few interaction fixes.

> **Unofficial project.** TGX Material is a personal fork. It is not affiliated with, endorsed by or supported by Telegram or the Telegram X team. It installs beside the official app under its own application ID.

Based on Telegram X **0.29.1.1823** (upstream commit [`b8fd691`](https://github.com/TGX-Android/Telegram-X/commit/b8fd691)).

## What's different

### Look and feel

- **Material You colors** from the wallpaper on Android 12+. When the system palette is monochrome (an almost grey accent), the app switches to a vivid Material 3 blue palette instead, in light and dark.
- **Material surfaces** for the built-in light and dark themes, including the default *Classic* theme. Custom imported themes keep their own colors.
- **New launcher icon**: blue-to-violet gradient, white paper plane, themed monochrome layer for Android 13+.

### Chat list

- **Folders as text tabs** (*All Chats · Unread · Personal…*) with a pill indicator on the active one, instead of the icon strip.
- **Bottom navigation bar** for Chats, Calls, Contacts and Settings, with TalkBack labels and keyboard focus.
- **Compact, airy rows**: 16sp titles, 14sp previews, 48–56dp avatars, no divider lines, single-line previews centered vertically.
- **Filled primary compose button** with Material 3 corner radius.
- **Archived Chats can be hidden** from Settings › Chat Folders › *Hide Archived Chats*.
- **Pinned chats reorder smoothly**: neighbouring rows make room while dragging, and the new order is saved once dropped.

### Conversations

- **Bubbles everywhere**, channels included.
- **Plain Material surface** instead of the pattern wallpaper inherited from the original theme. A wallpaper you picked yourself is kept.
- **Flat bubbles** with no shadow or outline, more inner padding (12dp) and screen margins (12dp), neutral date pills.
- **Pill-shaped composer** that keeps its emoji and action buttons clear of the text.
- **Soft skeleton bubbles** with a moving sheen while a chat's first messages load from the network.

### Previews

Hold a chat (or *Archived Chats*) to preview it. The preview **stays open after you lift your finger**:

- scroll through the chat without marking anything as read;
- tap the header to open the chat;
- tap the footer actions (mute, archive, delete…);
- tap outside or press back to close.

Sliding onto an action while still holding works as before.

### Settings, menus and sheets

- **Grouped settings** on every settings screen: rows share one rounded section (24dp outer corners, 4dp between rows).
- **Navigation drawer** with a rounded trailing edge and pill press states.
- **Option sheets** with rounded top corners and a drag handle.
- **Overflow menus** with rounded corners and Material elevation.

The full list, with implementation notes, is in [MATERIAL_REDESIGN.md](MATERIAL_REDESIGN.md).

## Building

The scripts in [`tools/material`](tools/material) set up a self-contained toolchain in the ignored `.toolchain/` directory and build the ARM64 `latestDebug` variant.

### Requirements

- Linux x86_64 with `git`, `curl`, `python3` and `tar`
- Android SDK command-line tools in `/opt/android-sdk/cmdline-tools/latest`, with licenses already accepted in `/opt/android-sdk/licenses`
- At least 8 GB of RAM and roughly 30 GB of free disk space for sources, toolchain and build outputs

### Steps

```sh
git clone git@github.com:djantche/TGX-Material.git
cd TGX-Material
git submodule update --init --recursive --depth=1

bash tools/material/setup-toolchain.sh     # JDK, SDK platform, NDK, CMake, Rust, local.properties
bash tools/material/fetch-lfs.sh           # Git LFS files of the tdlib submodule
bash tools/material/fetch-media-sources.sh # sparse androidx-media decoder sources
bash tools/material/test.sh                # color contrast tests + whitespace check
bash tools/material/build.sh               # assembles the APK
```

The APK is written to `app/build/outputs/apk/latest/debug/`. Install it with:

```sh
adb install -r app/build/outputs/apk/latest/debug/TGX-Material-*-arm64-v8a-debug.apk
```

Any other Telegram X flavor or ABI can still be built with the regular Gradle tasks; see the [upstream build instructions](https://github.com/TGX-Android/Telegram-X#build-instructions).

### Configuration

`setup-toolchain.sh` creates `local.properties` from `local.properties.sample` when it does not exist. It is ignored by Git. The keys that matter for this fork:

```properties
app.id=dev.davcodeur.tgx.material
app.name=TGX Material
telegram.api_id=YOUR_TELEGRAM_API_ID
telegram.api_hash=YOUR_TELEGRAM_API_HASH
```

- Get your own API credentials at [my.telegram.org](https://my.telegram.org) before sharing any build.
- Push notifications for a separate application ID need their own Firebase configuration (`app/google-services.json`); the official app's registration is not reused.
- You sign in to your account separately in TGX Material, since its data directory is separate from the official app's.

## Where the code lives

| Area | Files |
| --- | --- |
| Color scheme and theme wrapper | `theme/MaterialColorScheme.java`, `theme/MaterialTheme.java`, `theme/ThemeSet.java` |
| Bottom navigation | `widget/MaterialNavigationBar.java`, `ui/MainController.java` |
| Folder tabs | `navigation/ViewPagerTopView.java` (pill selection) |
| Chat rows | `component/dialogs/ChatView.java`, `data/TGChat.java` |
| Bubbles and composer | `data/TGMessage.java`, `ui/MessagesController.java` |
| Loading skeleton | `widget/MessagesSkeletonView.java`, `component/chat/MessagesManager.java` |
| Grouped settings | `ui/SettingsAdapter.java`, `FillingDrawable.java`, `theme/Theme.java` |
| Pinned previews | `widget/ForceTouchView.java`, `widget/BaseView.java`, `BaseActivity.java` |
| Launcher icon | `res/drawable/material_launcher_*.xml`, `res/mipmap-anydpi-v26`, `res/mipmap-anydpi-v33` |
| Tooling | `tools/material/` |

Paths are relative to `app/src/main/java/org/thunderdog/challegram/` and `app/src/main/`.

## Status

This is a work in progress, developed and checked on a Pixel 6 (Android 16) in the light theme.

- `tools/material/test.sh` checks text contrast, semantic state colors and fallback palettes in light and dark.
- The chat list, conversations, settings, drawer and pinned previews have been checked on device.
- Option sheets, overflow menus, the dark theme, other ABIs and older Android versions still need a visual pass.
- The profile header, search, calls, contacts and the media viewer still use the original Telegram X layouts.

## License

TGX Material is licensed under the terms of the [GNU General Public License v3.0](LICENSE), like Telegram X. Copyright notices of the original authors are kept.

Third-party components may use other licenses; see [docs/THIRDPARTY.md](docs/THIRDPARTY.md) and the `LICENSE` file of each component.

## Credits

All the messaging, media, encryption, accounts and calls code comes from [Telegram X](https://github.com/TGX-Android/Telegram-X) and [TDLib](https://github.com/tdlib/td). TGX Material only changes the interface on top of it.
