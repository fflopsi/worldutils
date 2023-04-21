package me.frauenfelderflorian.worldutils.config

import me.frauenfelderflorian.worldutils.WorldUtils
import org.bukkit.entity.Player

/** Wrapper class for configuration file, extends [Config] */
class Prefs(plugin: WorldUtils) : Config(plugin, "config.yml") {
    /**
     * Set the [setting] to [value]
     *
     * @param log true if logging messages should be sent
     */
    fun set(setting: Option, value: Any?, log: Boolean) = set(setting.configKey, value, log)

    /**
     * Set the personal [setting] of the [player] to [value]
     *
     * @param log true if logging messages should be sent
     */
    fun set(player: Player, setting: Option, value: Any?, log: Boolean) =
        set(getPrefix(player) + setting.configKey, value, log)

    /** Check if the [setting] contains a value */
    operator fun contains(setting: Option) = setting.configKey in config

    /** Check if the personal [setting] of the [player] contains a value */
    fun contains(player: Player, setting: Option) = getPrefix(player) + setting.configKey in config

    /** Get a [List] value from the [setting] */
    fun getList(setting: Option): List<*> = config.getList(setting.configKey)!!

    /** Get a [Boolean] value from the [setting] */
    fun getBoolean(setting: Option) = config.getBoolean(setting.configKey)

    /** Get a [Boolean] value from the personal [setting] of the [player] */
    fun getBoolean(player: Player, setting: Option) =
        config.getBoolean(getPrefix(player) + setting.configKey)

    /** Get an [Int] from the [setting] */
    fun getInt(setting: Option) = config.getInt(setting.configKey)

    /** Get an [Int] value from the personal [setting] of the [player] */
    fun getInt(player: Player, setting: Option) =
        config.getInt(getPrefix(player) + setting.configKey)

    /** Remove the [setting] */
    fun remove(setting: Option) = remove(setting.configKey)

    /** Remove the personal [setting] of the [player] */
    fun remove(player: Player, setting: Option) = remove(getPrefix(player) + setting.configKey)

}
