package me.frauenfelderflorian.worldutils

import me.frauenfelderflorian.worldutils.config.Option
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/** Broadcast the [message] with the [plugin] name as a prefix */
fun sendMessage(plugin: JavaPlugin, message: String) =
    Bukkit.broadcastMessage("[§3§l${plugin.name}§r] $message")

/** Send the [message] with the [plugin] name as a prefix to the [sender] */
fun sendMessage(plugin: JavaPlugin, sender: CommandSender, message: String) =
    sender.sendMessage("[§3§l${plugin.name}§r] $message")

/** Get a formatted message with the position information of [location] */
fun positionMessage(location: Location) = location.messageString()

/** Get a formatted message with the position information of [location] with [name] */
fun positionMessage(name: String, location: Location) = "§b$name§r ${location.messageString()}"

/** Get a formatted message with the position information of [location] with [name] and [author] */
fun positionMessage(name: String, author: String, location: Location) =
    "§b$name§r from §b§o$author§r ${location.messageString()}"

/** Get a formatted message with the [setting] and its new [value] */
fun settingSetMessage(setting: Option, value: String) =
    "Setting §b${setting.configKey}§r set to §b$value§r."

/** Send a "not allowed to do this" message to the [target] */
fun notAllowed(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§c§lYou are not allowed to do this.")

/** Send a "not a console command" message to the [target] */
fun notConsole(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§e§oThis is not a console command.")

/** Send a "wrong argument(s) for this command" message to the [target] */
fun wrongArguments(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§e§oWrong argument(s) were entered for this command.")

/** Send a "wrong number of arguments" message to the [target] */
fun wrongArgumentNumber(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§e§oA wrong number of arguments was entered for this command.")

/** Send a "name does not belong to an online player" message to the [target] */
fun playerNotFound(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§e§oThe entered name does not belong to an online player.")

/** Send an "entered position name cannot be found" message to the target */
fun positionNotFound(plugin: JavaPlugin, target: CommandSender) =
    sendMessage(plugin, target, "§e§oThe entered position name cannot be found.")

@Override
fun Location.messageString() = "(${
    when (this.world?.name) {
        "world" -> "§aoverworld§r"
        "world_nether" -> "§cnether§r"
        "world_the_end" -> "§eend§r"
        else -> this.world?.name
    }
}): ${this.blockX}  ${this.blockY}  ${this.blockZ}"
