# TGX Material

Personal Android redesign based on Telegram X 0.29.1.1823, commit `b8fd691`.
The upstream GPL license and copyright notices apply.

## Interface

- Material You colors from Android 12 and later; a monochrome system style (nearly grey accent) falls back to a vivid Material 3 blue palette, in light and dark.
- Material surfaces for the default light and dark themes; custom imported themes retain their color definitions.
- Bottom navigation for chats, calls, contacts and settings, with TalkBack labels, selected states and keyboard focus.
- Rounded search field in the main toolbar, retaining Telegram X search and chat folders.
- Chat folders shown as text labels with a pill indicator; the primary compose button is filled with the primary color.
- Bubbles in every chat, channels included; plain surface instead of the inherited pattern wallpaper (a wallpaper picked by the user is kept), flat bubbles without shadow or outline.
- Settings lists grouped into rounded sections (24dp outer corners, 4dp between rows) on every screen built with the settings adapter.
- Navigation drawer with a rounded trailing edge and pill pressed states; rounded option sheets with a drag handle; rounded overflow menus with Material elevation.
- Chat previews opened by holding a chat stay open after the finger is lifted: the chat can be scrolled without marking messages as read, header opens the chat, footer actions are tappable, back or a tap outside closes it.
- Launcher icon with a vivid blue-to-violet gradient and a themed monochrome layer.
- Archived Chats can be hidden from the chat list in Settings > Chat Folders, and its held preview stays open like chat previews.
- Pinned chats reorder locally while dragged, so neighbouring rows make room immediately; the new order is saved once dropped.
- Soft skeleton bubbles with a moving sheen while a chat's first messages load from the network.
- More spacious conversation rows and avatars.
- Rounded message bubbles, media, composer, send action and compose action.
- Rounded settings cards, larger Material switches and a distinct adaptive launcher icon.
- Classic, the default light theme, goes through the same Material layer as Blue and the night themes.
- Compact chat rows: 16sp titles, 14sp previews, 48–56dp avatars, single-line previews centered vertically.
- Message bubbles with 12dp inner padding and 12dp screen margins; composer text keeps its own padding around the emoji and action buttons.

Telegram messaging, media, encryption, accounts and calls continue to use the upstream implementation.

## Local build

The application ID is `dev.davcodeur.tgx.material`, and the launcher name is `TGX Material`.
It installs beside the official app with its own Android data directory. Account sign-in is required separately.

```sh
git submodule update --init --recursive --depth=1
bash tools/material/setup-toolchain.sh
bash tools/material/fetch-lfs.sh
bash tools/material/test.sh
bash tools/material/build.sh
```

Build tooling is kept in the ignored `.toolchain` directory. The setup script uses `/opt/android-sdk` for the command-line installer and existing accepted SDK licenses.
Only the ARM64 `latestDebug` variant is requested for the Pixel 6.

`local.properties` is ignored by Git. The initial personal development configuration comes from the upstream sample. Obtain your own Telegram API credentials at https://my.telegram.org before distributing a build. Firebase push notifications in a separately named application also require the corresponding Firebase configuration; the official application's push registration is not reused.

## Verification

`tools/material/test.sh` compiles and runs standalone Java tests for text contrast, semantic state colors and fallback compatibility, then checks the diff for whitespace errors. It can also receive twenty ARGB hex values, ten for a device's light scheme and ten for its dark scheme, to exercise a measured Android palette.

The APK still needs Android build validation and device checks for sign-in, navigation, search, folders, message rendering, composer, calls, settings, keyboard insets and light/dark switching. Color tests alone do not validate those flows.
