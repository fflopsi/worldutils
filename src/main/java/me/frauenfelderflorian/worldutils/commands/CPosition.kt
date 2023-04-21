package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.config.Positions
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import java.util.regex.Pattern

/** [TabExecutor] for command position */
data class CPosition(val plugin: WorldUtils, val positions: Positions) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        when (args.size) {
            1 -> {
                //command or position name entered
                return when (args[0]) {
                    "list" -> {
                        //send all position info
                        positions.positions.forEach {
                            sendMessage(
                                plugin, sender, positionMessage(it, positions.getLocation(it))
                            )
                        }
                        true
                    }

                    "clear" -> {
                        //remove all positions
                        sendMessage(plugin, "§e§oCleared positions")
                        positions.positions.forEach { positions.removePosition(it) }
                        true
                    }

                    else -> {
                        //position name entered
                        if (args[0] in positions) {
                            //existing position, send info
                            if (positions.containsAuthor(args[0])) {
                                sendMessage(
                                    plugin, sender, positionMessage(
                                        args[0],
                                        positions.getAuthor(args[0]),
                                        positions.getLocation(args[0])
                                    )
                                )
                            } else {
                                sendMessage(
                                    plugin,
                                    sender,
                                    positionMessage(args[0], positions.getLocation(args[0]))
                                )
                            }
                        } else if (sender is Player) {
                            //new position name, save position
                            if (!Pattern.matches("\\w+", args[0])) {
                                wrongArguments(plugin, sender)
                            } else {
                                positions.set(args[0], sender.location, true)
                                if (plugin.prefs.getBoolean(Option.POSITION_SAVE_AUTHOR)) {
                                    positions.setAuthor(args[0], sender.getName())
                                }
                                sendMessage(
                                    plugin, "§aAdded§r position ${
                                        positionMessage(
                                            args[0],
                                            sender.getName(),
                                            positions.getLocation(args[0])
                                        )
                                    }"
                                )
                            }
                        } else {
                            notConsole(plugin, sender)
                        }
                        true
                    }
                }
            }

            2 -> {
                //command and position entered
                when (args[0]) {
                    "tp" -> {
                        //teleport player to position if OP
                        if (sender is Player && sender.isOp()) {
                            sender.teleport(positions.getLocation(args[1]))
                        } else if (sender is Player) {
                            notAllowed(plugin, sender)
                        } else {
                            notConsole(plugin, sender)
                        }
                        return true
                    }

                    "del" -> {
                        //delete position
                        try {
                            val old = positions.getLocation(args[1])
                            positions.removePosition(args[1])
                            sendMessage(
                                plugin, "§cDeleted§r position ${positionMessage(args[1], old)}"
                            )
                        } catch (e: UnsupportedOperationException) {
                            wrongArguments(plugin, sender)
                        } catch (e: NullPointerException) {
                            wrongArguments(plugin, sender)
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Done while the [sender] enters the registered [command] with [args]
     *
     * @return List of Strings for tab completion
     */
    override fun onTabComplete(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): List<String> {
        val completions = mutableListOf<String>()
        when (args.size) {
            1 -> {
                //command or position name being entered
                StringUtil.copyPartialMatches(args[0], listOf("list", "clear", "del"), completions)
                if (sender is Player && sender.isOp()) {
                    StringUtil.copyPartialMatches(args[0], listOf("tp"), completions)
                }
                StringUtil.copyPartialMatches(args[0], positions.positions, completions)
            }

            2 -> {
                //position name being entered
                if (args[0] == "del" || sender is Player && sender.isOp() && args[0] == "tp") {
                    StringUtil.copyPartialMatches(args[1], positions.positions, completions)
                }
            }
        }
        return completions
    }

    companion object {
        const val CMD = "position"
    }
}
