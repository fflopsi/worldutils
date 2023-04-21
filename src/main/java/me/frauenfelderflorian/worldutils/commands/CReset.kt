package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.WorldUtils
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.notAllowed
import me.frauenfelderflorian.worldutils.sendMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

/** [TabExecutor] for command reset */
data class CReset(val plugin: WorldUtils) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        when (args.size) {
            0 -> {
                //request a reset
                if (plugin.prefs.getBoolean(Option.RESET_REQUESTED)) {
                    sendMessage(plugin, "§eA server reset has already been requested.")
                } else {
                    plugin.prefs.set(setting = Option.RESET_REQUESTED, value = true, log = true)
                    sendMessage(
                        plugin,
                        "§eA server reset has been requested. Use §o/reset confirm§r§e or §o/reset cancel."
                    )
                }
                return true
            }

            1 -> {
                if (sender.isOp || !plugin.prefs.getBoolean(Option.RESET_NEED_OP)) {
                    when (args[0]) {
                        "cancel" -> {
                            //cancel a requested reset
                            if (plugin.prefs.getBoolean(Option.RESET_REQUESTED)) {
                                plugin.prefs.set(
                                    setting = Option.RESET_REQUESTED, value = false, log = true
                                )
                                sendMessage(plugin, "§bThe server reset has been canceled.")
                            } else {
                                sendMessage(plugin, "§bThere is no server reset requested.")
                            }
                            return true
                        }

                        "confirm" -> {
                            //confirm a requested reset
                            if (plugin.prefs.getBoolean(Option.RESET_REQUESTED)) {
                                sendMessage(plugin, "§c§oResetting server in 10 seconds.")
                                //kick players 1 second before restarting or shutting down
                                Bukkit.getScheduler().runTaskLater(
                                    plugin, Runnable {
                                        Bukkit.getOnlinePlayers().forEach {
                                            it.kickPlayer("§e§oResetting server.§r You can rejoin in a few moments.")
                                        }
                                    }, 200
                                )
                                //restart or shut down
                                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                                    plugin.prefs.set(
                                        setting = Option.RESET_RESET, value = true, log = true
                                    )
                                    plugin.prefs.set(
                                        setting = Option.RESET_REQUESTED, value = false, log = true
                                    )
                                    if (plugin.prefs.getBoolean(Option.RESET_RESTART_AFTER_RESET)) {
                                        Bukkit.spigot().restart()
                                    } else {
                                        Bukkit.shutdown()
                                    }
                                }, 220)
                            } else {
                                sendMessage(
                                    plugin,
                                    "§eThere is no server reset requested. You can request one by using §o/reset"
                                )
                            }
                            return true
                        }
                    }
                } else {
                    notAllowed(plugin, sender)
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
    ): List<String> = emptyList()

    companion object {
        const val CMD = "reset"
    }
}
