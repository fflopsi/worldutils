package me.frauenfelderflorian.worldutils.config

import me.frauenfelderflorian.worldutils.WorldUtils
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException

/** Abstract wrapper class for configuration file */
abstract class Config(private val plugin: WorldUtils, filename: String) {
    /** The [File] in which the [Config] is stored */
    private val file: File

    /** The [YamlConfiguration] for accessing the configs */
    protected val config: YamlConfiguration

    /** Load the config file or create a new one if none is found */
    init {
        //set config file, create if not exists
        if (!plugin.dataFolder.exists() && plugin.dataFolder.mkdirs()) {
            plugin.logger.info("Created ${plugin.name} folder")
        }
        file = File(plugin.dataFolder, filename)
        if (!file.exists()) {
            try {
                if (file.createNewFile()) plugin.logger.info("Created ${file.name}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        config = YamlConfiguration.loadConfiguration(file)
        plugin.logger.info("Loaded config from ${file.name}")
    }

    /**
     * Set the [value] for config at [path]
     *
     * @param log True if logging messages should be sent
     */
    fun set(path: String, value: Any?, log: Boolean) {
        config[path] = value
        if (log) plugin.logger.info("Set config $path to $value")
        save(log)
    }

    /** Remove the config at [path] */
    protected fun remove(path: String) = set(path, null, true)

    /**
     * Save the config file
     *
     * @param log True if logging messages should be sent
     */
    private fun save(log: Boolean) {
        try {
            config.save(file)
            if (log) plugin.logger.info("Saved config to ${file.name}")
        } catch (e: IOException) {
            plugin.logger.severe("Could not save config to ${file.name}")
            plugin.logger.info(config.saveToString())
        }
    }

    /** Get the prefix for personal configs for [player] */
    protected fun getPrefix(player: Player) = "--personal--.${player.uniqueId}."
}
