package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

/** [TabExecutor] for command sendposition */
class CSendPosition(val plugin: WorldUtils) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        if (sender is Player) {
            when (args.size) {
                0 -> {
                    //send to all players
                    sendMessage(plugin, positionMessage(sender.name, sender.location))
                    return true
                }

                1 -> {
                    //send to one player
                    try {
                        sendMessage(
                            plugin,
                            Bukkit.getPlayer(args[0])!!,
                            positionMessage(sender.name, sender.location)
                        )
                    } catch (e: NullPointerException) {
                        playerNotFound(plugin, sender)
                    }
                    return true
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
        if (args.size == 1) {
            StringUtil.copyPartialMatches(
                args[0], Bukkit.getOnlinePlayers().map { it.name }, completions
            )
        }
        return completions
    }

    companion object {
        const val CMD = "sendposition"
    }
}
