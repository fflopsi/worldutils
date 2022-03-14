# WorldUtils

This Minecraft Spigot plugin contains various tools, namely for managing both public and personal in-game positions,
sending your own position, setting up a timer, resetting the server and managing some plugin settings.

### Extensions

This plugin can be extended with further functionality. When your own extension works good enough, you can apply to
be listed below in the official extension list.

#### Official Extension List

- [WUProjects](https://github.com/WorldUtils/wu-projects)

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

## Installation

To install the plugin, simply place the *.jar* file in the folder *plugins* of your server. The server must run at least
Spigot, Paper is also fine (CraftBukkit does not work). On the next server (re)start, the plugin should be initialized
and can be run correctly.

## Usage

To use the plugin, enter an appropriate command. All commands have tab completion (except when accessing personal
positions or joining personal timers of other players), so you do not need to know the exact syntax when typing.

The commands and their respective syntax are:

- `/position <name> | list | clear | (tp | del <name>)`
- `/personalposition <name> | list | clear | (tp | del <name>)`
- `/sendposition [<playername>]`
- `/timer visible | running | reverse | reset | (set | add [[[<d>] <h>] <min>] <s>)`
- `/personaltimer <playername> | visible | running | reverse | reset | (set | add [[[<d>] <h>] <min>] <s>)`
- `/reset [confirm]`
- `/settings <commandname> <setting> true | false`

Some of these commands also have an alias that can be used instead of the full command names and allow for faster
command input. These aliases are:

- `position`: `pos`
- `personalposition`: `ppos`
- `sendposition`: `spos`
- `timer`: `tmr`
- `personaltimer`: `ptmr`
- `settings`: `stg`

For further information on how this all works in detail and more,
visit [the wiki](https://github.com/WorldUtils/world-utils/wiki/Usage).

## Contributing

If you are willing to contribute to this project and know how to use GitHub issues, feel free
to [open one](https://github.com/WorldUtils/world-utils/issues/new/choose).

You are also welcome to fork this repository and improve it via
a [pull request](https://github.com/WorldUtils/world-utils/compare).

## Credits

I created this project after the plugin(s) German [streamer](https://www.twitch.tv/BastiGHG)
and [YouTuber](https://www.youtube.com/user/kompetenzGHG) *BastiGHG* is using in his Minecraft challenges. I also
used [this project](https://github.com/IlluminatiDreieck/Challenges) for (very limited) inspiration on how to structure
my own work.

## License

This project is licensed under the MIT license. For more information, read [this file](LICENSE.md).
