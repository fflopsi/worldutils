package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import me.frauenfelderflorian.worldutils.config.Option
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

/** [TabExecutor] for command timer */
data class CTimer(val plugin: WorldUtils) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        if (sender.isOp || plugin.timer.containsPlayer(sender as Player)) {
            when (args.size) {
                1 -> {
                    when (args[0]) {
                        "visible" -> {
                            //add or remove player: changes visibility status for one single player
                            if (sender is Player) {
                                if (plugin.timer.containsPlayer(sender)) {
                                    plugin.timer.removePlayer(sender)
                                } else {
                                    plugin.timer.addPlayer(sender)
                                }
                                sendMessage(
                                    plugin, sender, "§eTimer set to ${
                                        if (plugin.timer.containsPlayer(sender)) {
                                            "visible."
                                        } else {
                                            "invisible."
                                        }
                                    }"
                                )
                            } else {
                                notConsole(plugin, sender)
                            }
                            return true
                        }

                        "running" -> {
                            //change running status
                            plugin.timer.setRunning(!plugin.prefs.getBoolean(Option.TIMER_RUNNING))
                            return true
                        }

                        "reverse" -> {
                            //change reverse status
                            plugin.prefs.set(
                                Option.TIMER_REVERSE,
                                !plugin.prefs.getBoolean(Option.TIMER_REVERSE),
                                true
                            )
                            sendMessage(
                                plugin, "§eTimer reversed, now in §b${
                                    if (plugin.prefs.getBoolean(Option.TIMER_REVERSE)) {
                                        "reverse"
                                    } else {
                                        "normal"
                                    }
                                }§e mode."
                            )
                            return true
                        }

                        "reset" -> {
                            //set timer to 0
                            plugin.timer.setRunning(false)
                            plugin.timer.setTime(0)
                            sendMessage(plugin, "§eTimer set to 0.")
                            return true
                        }
                    }
                }

                2, 3, 4, 5 -> {
                    when (args[0]) {
                        "set" -> {
                            //set time to input values
                            try {
                                plugin.timer.setTime(getTime(args))
                                sendMessage(
                                    plugin,
                                    "§eTimer set to §b${Timer.formatTime(plugin.prefs.getInt(Option.TIMER_TIME))}"
                                )
                                return true
                            } catch (e: IllegalStateException) {
                                wrongArgumentNumber(plugin, sender)
                            } catch (e: NumberFormatException) {
                                wrongArguments(plugin, sender)
                            }
                        }

                        "add" -> {
                            //add input values to current time
                            try {
                                plugin.timer.setTime(
                                    plugin.prefs.getInt(Option.TIMER_TIME) + getTime(args)
                                )
                                sendMessage(
                                    plugin,
                                    "§eAdded §b${Timer.formatTime(getTime(args))} §eto timer"
                                )
                                return true
                            } catch (e: IllegalStateException) {
                                wrongArgumentNumber(plugin, sender)
                            } catch (e: NumberFormatException) {
                                wrongArguments(plugin, sender)
                            }
                        }
                    }
                }
            }
        } else if (args.size == 1 && args[0] == "visible") {
            plugin.timer.addPlayer(sender)
            return true
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
        if (sender.isOp || plugin.timer.containsPlayer(sender as Player)) {
            when (args.size) {
                1 -> {
                    StringUtil.copyPartialMatches(
                        args[0],
                        listOf("visible", "running", "reverse", "reset", "set", "add"),
                        completions
                    )
                }

                2, 3, 4, 5 -> if (args[0] in listOf("set", "add")) completions.add("<time>")
            }
        } else if (args.size == 1) {
            StringUtil.copyPartialMatches(args[0], listOf("visible"), completions)
        }
        return completions
    }

    companion object {
        const val CMD = "timer"

        /** Get the time in seconds from array containing all input arguments (length 2 - 5) */
        @Throws(java.lang.IllegalStateException::class)
        fun getTime(args: Array<String>): Int {
            return when (args.size) {
                2 -> args[1].toInt()
                3 -> 60 * args[1].toInt() + args[2].toInt()
                4 -> 3600 * args[1].toInt() + 60 * args[2].toInt() + args[3].toInt()
                5 -> 86400 * args[1].toInt() + 3600 * args[2].toInt() + 60 * args[3].toInt() + args[4].toInt()
                else -> throw IllegalStateException("Array length must be >= 2 and <= 5")
            }
        }
    }
}
