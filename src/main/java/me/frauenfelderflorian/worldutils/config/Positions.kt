package me.frauenfelderflorian.worldutils.config

import me.frauenfelderflorian.worldutils.WorldUtils
import org.bukkit.entity.Player

/** Wrapper class for configuration file for positions, extends [Config] */
class Positions(plugin: WorldUtils) : Config(plugin, "positions.yml") {
    /** Set a personal position with [name] for [player] */
    fun setPersonal(player: Player, name: String) =
        set(getPrefix(player) + name, player.location, true)

    /** Set the [author] of the position with [name] */
    fun setAuthor(name: String, author: String) = set("list.$name", author, true)

    /** Check if the position with [name] exists */
    operator fun contains(name: String) = name in config

    /** Check if the personal position with [name] exists for [player] */
    fun containsPersonal(player: Player, name: String) = getPrefix(player) + name in config

    /** Check if the position with [name] has a saved author */
    fun containsAuthor(name: String) = "list.$name" in config

    /** Get the author of the position with [name] */
    fun getAuthor(name: String) = config.getString("list.$name")!!

    /** Get a Location from [name] */
    fun getLocation(name: String) = config.getLocation(name)!!

    /** Get the personal position [name] from [player] */
    fun getPersonalLocation(player: Player, name: String) =
        config.getLocation(getPrefix(player) + name)!!

    /** Remove the position with [name] */
    @Throws(UnsupportedOperationException::class)
    fun removePosition(name: String) {
        if ("--personal--" in name || "list" in name) {
            throw UnsupportedOperationException("Cannot delete this.")
        }
        remove(name)
        if (containsAuthor(name)) remove("list.$name")
    }

    /** Remove the personal position with [name] from [player] */
    fun removePosition(player: Player, name: String) = remove(getPrefix(player) + name)

    /** Get all positions */
    val positions = config.getKeys(false).apply { removeAll(setOf("list", "--personal--")) }.toSet()

    /** Get all personal positions of the [player] */
    fun getPositions(player: Player): Set<String> =
        config.getConfigurationSection(getPrefix(player).dropLast(1))?.getKeys(false) ?: emptySet()
}
