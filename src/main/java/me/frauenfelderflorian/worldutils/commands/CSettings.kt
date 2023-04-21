package me.frauenfelderflorian.worldutils.commands

import me.frauenfelderflorian.worldutils.*
import me.frauenfelderflorian.worldutils.config.Option
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

/** [TabExecutor] for command settings */
class CSettings(val plugin: WorldUtils) : TabExecutor {
    /** Done when the [sender] sends the registered [command] with [args] */
    override fun onCommand(
        sender: CommandSender, command: Command, alias: String, args: Array<String>,
    ): Boolean {
        if (args.size == 3) {
            //correct number of arguments
            val setting = Option[args[0], args[1]]
            if (setting != null && args[2] in listOf("true", "false", "null")) {
                //correct setting and value
                if (!setting.isGlobal && sender is Player) {
                    //personal setting
                    when (args[2]) {
                        "true" -> {
                            //set to true
                            plugin.prefs.set(
                                player = sender, setting = setting, value = true, log = true
                            )
                            sendMessage(plugin, sender, settingSetMessage(setting, "true"))
                            return true
                        }

                        "false" -> {
                            //set to false
                            plugin.prefs.set(
                                player = sender, setting = setting, value = false, log = true
                            )
                            sendMessage(plugin, sender, settingSetMessage(setting, "false"))
                            return true
                        }

                        "null" -> {
                            //remove setting
                            plugin.prefs.remove(sender, setting)
                            sendMessage(plugin, sender, settingSetMessage(setting, "null"))
                            sendMessage(
                                plugin,
                                sender,
                                "§cUse with caution: §oThe plugin might not work correctly!"
                            )
                            return true
                        }
                    }
                } else if (setting.isGlobal && (sender.isOp || !plugin.prefs.getBoolean(Option.SETTINGS_NEED_OP))) {
                    //global setting
                    when (args[2]) {
                        "true" -> {
                            //set to true
                            plugin.prefs.set(setting = setting, value = true, log = true)
                            sendMessage(plugin, settingSetMessage(setting, "true"))
                            return true
                        }

                        "false" -> {
                            //set to false
                            plugin.prefs.set(setting = setting, value = false, log = true)
                            sendMessage(plugin, sender, settingSetMessage(setting, "false"))
                            return true
                        }

                        "null" -> {
                            //remove setting
                            plugin.prefs.remove(setting)
                            sendMessage(plugin, settingSetMessage(setting, "null"))
                            sendMessage(
                                plugin, "§cUse with caution: §oThe plugin might not work correctly!"
                            )
                            return true
                        }
                    }
                }
            } else {
                wrongArguments(plugin, sender)
            }
        } else {
            wrongArgumentNumber(plugin, sender)
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
                //command being entered
                if (sender.isOp || !plugin.prefs.getBoolean(Option.SETTINGS_NEED_OP)) {
                    StringUtil.copyPartialMatches(args[0], Option.getCommands(true), completions)
                }
                if (sender is Player) {
                    StringUtil.copyPartialMatches(args[0], Option.getCommands(false), completions)
                }
            }

            2 -> {
                //setting being entered
                if (Option.commands.contains(args[0])) {
                    if (sender.isOp || !plugin.prefs.getBoolean(Option.SETTINGS_NEED_OP)) {
                        StringUtil.copyPartialMatches(
                            args[1], Option.getSettings(args[0], true), completions
                        )
                    }
                    if (sender is Player) {
                        StringUtil.copyPartialMatches(
                            args[1], Option.getSettings(args[0], false), completions
                        )
                    }
                }
            }

            3 -> {
                //value being entered
                val setting = Option[args[0], args[1]]
                if (setting != null && (sender is Player && !setting.isGlobal || (sender.isOp || !plugin.prefs.getBoolean(
                        Option.SETTINGS_NEED_OP
                    )) && setting.isGlobal)
                ) {
                    StringUtil.copyPartialMatches(args[2], listOf("true", "false"), completions)
                }
            }
        }
        return completions
    }

    companion object {
        const val CMD = "settings"
    }
}
