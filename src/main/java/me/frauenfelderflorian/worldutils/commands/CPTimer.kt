package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import me.frauenfelderflorian.worldutils.config.Option
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

/** [TabExecutor] for command personaltimer */
class CPTimer(val plugin: WorldUtils) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        if (sender is Player) {
            when (args.size) {
                1 -> {
                    when (args[0]) {
                        "visible" -> {
                            //change visibility status
                            plugin.getTimer(sender)?.setVisible(
                                !plugin.prefs.getBoolean(sender, Option.PTIMER_VISIBLE)
                            )
                            return true
                        }

                        "running" -> {
                            //change running status
                            plugin.getTimer(sender)?.setRunning(
                                !plugin.prefs.getBoolean(sender, Option.PTIMER_RUNNING)
                            )
                            return true
                        }

                        "reverse" -> {
                            //change reverse status
                            plugin.prefs.set(
                                sender, Option.PTIMER_REVERSE, !plugin.prefs.getBoolean(
                                    sender, Option.PTIMER_REVERSE
                                ), true
                            )
                            sendMessage(
                                plugin, "§eTimer reversed, now in §b${
                                    if (plugin.prefs.getBoolean(sender, Option.TIMER_REVERSE)) {
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
                            plugin.getTimer(sender)?.setRunning(false)
                            plugin.getTimer(sender)?.setTime(0)
                            sendMessage(plugin, sender, "§ePersonal timer set to 0.")
                            return true
                        }
                    }
                }

                2, 3, 4, 5 -> {
                    when (args[0]) {
                        "invite", "join" -> {
                            //add another player to the personal timer or join another player's personal timer
                            if (args.size == 2) {
                                val other = Bukkit.getPlayer(args[1])
                                if (other != null && other.isOnline) {
                                    if (args[0] == "invite" && !plugin.getTimer(sender)
                                            ?.containsPlayer(other)!!
                                    ) {
                                        plugin.getTimer(sender)!!.addPlayer(other)
                                        return true
                                    } else if (args[0] == "join" && !plugin.getTimer(other)
                                            ?.containsPlayer(sender)!! && plugin.prefs.getBoolean(
                                            other, Option.PTIMER_JOINABLE
                                        )
                                    ) {
                                        plugin.getTimer(other)!!.addPlayer(sender)
                                        return true
                                    }
                                } else {
                                    playerNotFound(plugin, sender)
                                }
                            }
                        }

                        "leave", "remove" -> {
                            //leave another player's personal timer or remove a player from the personal timer
                            if (args.size == 2) {
                                val other = Bukkit.getPlayer(args[1])
                                if (other != null && other.isOnline) {
                                    if (args[0] == "leave" && plugin.getTimer(other)
                                            ?.containsPlayer(sender)!!
                                    ) {
                                        plugin.getTimer(other)!!.removePlayer(sender)
                                        return true
                                    } else if (args[0] == "remove" && plugin.getTimer(sender)
                                            ?.containsPlayer(other)!!
                                    ) {
                                        plugin.getTimer(sender)!!.removePlayer(other)
                                        return true
                                    }
                                } else {
                                    playerNotFound(plugin, sender)
                                }
                            }
                        }

                        "set" -> {
                            //set time to input values
                            try {
                                plugin.getTimer(sender)?.setTime(CTimer.getTime(args))
                                sendMessage(
                                    plugin, sender, "§ePersonal timer set to §b${
                                        Timer.formatTime(
                                            plugin.prefs.getInt(sender, Option.TIMER_TIME)
                                        )
                                    }"
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
                                plugin.getTimer(sender)?.setTime(
                                    plugin.prefs.getInt(sender, Option.TIMER_TIME) + CTimer.getTime(
                                        args
                                    )
                                )
                                sendMessage(
                                    plugin,
                                    sender,
                                    "§eAdded §b${Timer.formatTime(CTimer.getTime(args))} §eto personal timer"
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
        } else {
            notConsole(plugin, sender)
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
        //get names of all online players
        val players = Bukkit.getOnlinePlayers().map { it.name }
        //get names of all players that can view the personal timer
        val removablePlayers = Bukkit.getOnlinePlayers()
            .filter { plugin.getTimer(sender as Player)?.containsPlayer(it)!! }.map { it.name }
        //get names of all players whose personal timer is visible
        val leavableTimers = Bukkit.getOnlinePlayers()
            .filter { plugin.getTimer(it)?.containsPlayer(sender as Player)!! }.map { it.name }
        when (args.size) {
            1 -> StringUtil.copyPartialMatches(
                args[0],
                listOf(
                    "visible",
                    "running",
                    "reverse",
                    "reset",
                    "invite",
                    "join",
                    "leave",
                    "remove",
                    "set",
                    "add"
                ),
                completions,
            )

            2, 3, 4, 5 -> {
                if (args.size == 2) {
                    when (args[0]) {
                        "invite" -> StringUtil.copyPartialMatches(args[1], players, completions)

                        "leave" -> StringUtil.copyPartialMatches(
                            args[1], leavableTimers, completions
                        )

                        "remove" -> StringUtil.copyPartialMatches(
                            args[1], removablePlayers, completions
                        )
                    }
                }
                if (args[0] in listOf("set", "add")) completions.add("<time>")
            }
        }
        return completions
    }

    companion object {
        const val CMD = "personaltimer"
    }
}
