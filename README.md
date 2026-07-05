# Quick-LAN

A Fabric client-side mod for Minecraft that automatically opens singleplayer worlds to LAN in offline mode as soon as you load in — no need to open the pause menu and click "Open to LAN" every time.

## Features

- **Auto-opens to LAN** — as soon as your world finishes loading, the mod publishes it to LAN on port `25565` automatically.

- **Offline-mode identity for everyone** — every player who logs in (host included) is assigned a deterministic offline UUID derived from their username (the same scheme vanilla uses for cracked/offline accounts), so no Mojang authentication is required for LAN players to join. Premium accounts can still join though.

- **Consistent player data across hosts** — if multiple people take turns hosting the same world (e.g. a shared/symlinked world folder on one machine, or copied between machines), each player's inventory, position, and stats stay tied to their own username no matter who currently has the world open. This works around a Minecraft 26.1+ behavior where the game would otherwise redirect the current host's data to whichever UUID last hosted the world.

- **Gamemode matches the host** — new players who join always start in whatever gamemode the host is currently in.

## Requirements

| | |
|---|---|
| Minecraft | 26.1.2 |
| Fabric Loader | 0.19.3 |
| Fabric API | 0.154.0+26.1.2 |
| Loom (dev only) | 1.17.13 |

## Building from source

```bash
git clone <repo-url>
cd quick-lan
./gradlew build
```

## How it works

Quick-LAN is implemented entirely through Mixins targeting the client/integrated-server side of the game:

- **`ExampleClientMixin`** — injects into `IntegratedServer#tickServer`, waiting until the client connection and player are fully ready, then disables server authentication and calls `publishServer(...)` using the host's current gamemode.
- **`ServerLoginPacketListenerImplMixin`** — intercepts profile assignment during login (`startClientVerification`) and rewrites it to an offline-derived profile for every connecting player, regardless of whether they authenticated online or not.
- **`IntegratedServerMixin`** — disables the game's built-in "singleplayer owner" identity shortcut (`isSingleplayerOwner`), which otherwise causes the currently-hosting player to inherit — and eventually overwrite — whichever player's data was saved as the world's owner in a previous session. With this disabled, every player's data is looked up strictly by their own name-derived UUID, host or not.

## Known limitations

- Since the "singleplayer owner" identity shortcut is fully disabled, convenience behaviors tied to it are also disabled — for example, the host is no longer automatically protected from `/kick` or automatically granted elevated permissions. If your group uses commands and needs the host to have operator permissions, grant them explicitly (e.g. via `ops.json`).
- This mod is intended for informal, trusted LAN play (e.g. a shared world folder used by a small group taking turns hosting). It does not add any additional security — anyone on your local network can join while the world is published.
