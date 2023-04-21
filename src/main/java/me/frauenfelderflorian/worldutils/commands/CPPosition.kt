package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.config.Positions
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

/** [TabExecutor] for command personalposition */
class CPPosition(val plugin: WorldUtils, private val positions: Positions) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        if (sender is Player) {
            when (args.size) {
                1 -> {
                    //command or position name entered
                    return when (args[0]) {
                        "list" -> {
                            //send all position info
                            positions.getPositions(sender).forEach {
                                sendMessage(
                                    plugin,
                                    sender,
                                    positionMessage(it, positions.getPersonalLocation(sender, it))
                                )
                            }
                            true
                        }

                        "clear" -> {
                            //remove all positions
                            sendMessage(plugin, sender, "§e§oCleared personal positions")
                            positions.getPositions(sender)
                                .forEach { positions.removePosition(sender, it) }
                            true
                        }

                        else -> {
                            //position name entered
                            if (positions.containsPersonal(sender, args[0])) {
                                //existing position, send info
                                sendMessage(
                                    plugin, sender, positionMessage(
                                        args[0], positions.getPersonalLocation(sender, args[0])
                                    )
                                )
                            } else {
                                //new position name, save position
                                positions.setPersonal(sender, args[0])
                                sendMessage(
                                    plugin, sender, "§aAdded§r personal position ${
                                        positionMessage(
                                            args[0], positions.getPersonalLocation(sender, args[0])
                                        )
                                    }"
                                )
                            }
                            true
                        }
                    }
                }

                2 -> {
                    //command and position entered
                    return when (args[0]) {
                        "tp" -> {
                            //teleport player to position if OP
                            if (sender.isOp) {
                                sender.teleport(positions.getPersonalLocation(sender, args[1]))
                            } else {
                                notAllowed(plugin, sender)
                            }
                            true
                        }

                        "del" -> {
                            //delete position
                            sendMessage(
                                plugin, sender, "§cDeleted§r personal position ${
                                    positionMessage(
                                        args[1], positions.getPersonalLocation(sender, args[1])
                                    )
                                }"
                            )
                            positions.removePosition(sender, args[1])
                            true
                        }

                        else -> otherPlayersPosition(sender, args)
                    }
                }
            }
        } else {
            if (args.size == 2) return otherPlayersPosition(sender, args)
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
        if (sender is Player) {
            when (args.size) {
                1 -> {
                    //command or position name being entered
                    StringUtil.copyPartialMatches(
                        args[0], listOf("list", "clear", "del"), completions
                    )
                    if (sender.isOp) {
                        StringUtil.copyPartialMatches(args[0], listOf("tp"), completions)
                    }
                    StringUtil.copyPartialMatches(
                        args[0], positions.getPositions(sender), completions
                    )
                }

                2 -> {
                    //position name being entered
                    if (args[0] == "del" || sender.isOp && args[0] == "tp") {
                        StringUtil.copyPartialMatches(
                            args[1], positions.getPositions(sender), completions
                        )
                    }
                }
            }
        }
        return completions
    }

    /** Get another player's private position if possible */
    private fun otherPlayersPosition(sender: CommandSender, args: Array<String>): Boolean {
        val other = Bukkit.getPlayer(args[0])
        if (other != null && other.isOnline) {
            //get personalposition from player
            if (plugin.prefs.getBoolean(other, Option.PPOSITION_ACCESS_GLOBAL)) {
                try {
                    sendMessage(
                        plugin, sender, "Personal position from player ${args[0]}: ${
                            positionMessage(args[1], positions.getPersonalLocation(other, args[1]))
                        }"
                    )
                } catch (e: NullPointerException) {
                    positionNotFound(plugin, sender)
                }
            } else {
                notAllowed(plugin, sender)
            }
            return true
        }
        playerNotFound(plugin, sender)
        return false
    }

    companion object {
        const val CMD = "personalposition"
    }
}
