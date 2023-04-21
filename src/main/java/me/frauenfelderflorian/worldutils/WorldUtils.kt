package me.frauenfelderflorian.worldutils

import me.frauenfelderflorian.worldutils.commands.*
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.config.Positions
import me.frauenfelderflorian.worldutils.config.Prefs
import me.frauenfelderflorian.worldutils.listeners.LTimerPaused
import me.frauenfelderflorian.worldutils.listeners.Listeners
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/** Main plugin class */
class WorldUtils : JavaPlugin() {
    /** [Prefs] for this plugin */
    val prefs = Prefs(this)

    /** [Timer] for this plugin */
    val timer = Timer(this)

    /** Contains all personal [Timer]s */
    private val timers = mutableMapOf<Player, Timer>()

    /** Done on plugin load before world loading*/
    override fun onLoad() {
        //set plugin instance
        instance = this
        //load config and set defaults
        Option.values().filter { it.isGlobal && !prefs.contains(it) && it.isVanilla }
            .forEach { prefs.set(it, it.default, true) }
        //reset if needed
        if (prefs.getBoolean(Option.RESET_RESET)) {
            //reset worlds
            listOf("world", "world_nether", "world_the_end").forEach { world ->
                try {
                    Files.walk(Paths.get(world)).sorted(Comparator.reverseOrder())
                        .map { it.toFile() }.filter { it.delete() }
                        .forEach { logger.info("§e File §b${it.name}§e deleted.") }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            prefs.set(setting = Option.RESET_RESET, value = false, log = true)
            //delete positions if needed
            if (prefs.getBoolean(Option.RESET_DELETE_POSITIONS)) {
                dataFolder.listFiles()!!
                    .filter { it.name.startsWith("positions") && it.name.endsWith(".yml") && it.delete() }
                    .forEach { logger.info("§e File §b${it.name}§e deleted.") }
            }
            //reset Settings if needed
            if (prefs.getBoolean(Option.RESET_RESET_SETTINGS)) {
                Option.values().forEach { prefs.set(it, it.default, true) }
            }
            logger.warning("Server reset")
        }
    }

    /** Done on plugin enabling */
    override fun onEnable() {
        //load positions
        val positions = Positions(this)
        //set CommandExecutors and TabCompleters
        getCommand(CPosition.CMD)?.setExecutor(CPosition(this, positions))
        getCommand(CPosition.CMD)?.tabCompleter = CPosition(this, positions)
        getCommand(CPPosition.CMD)?.setExecutor(CPPosition(this, positions))
        getCommand(CPPosition.CMD)?.tabCompleter = CPPosition(this, positions)
        getCommand(CSendPosition.CMD)?.setExecutor(CSendPosition(this))
        getCommand(CSendPosition.CMD)?.tabCompleter = CSendPosition(this)
        getCommand(CTimer.CMD)?.setExecutor(CTimer(this))
        getCommand(CTimer.CMD)?.tabCompleter = CTimer(this)
        getCommand(CPTimer.CMD)?.setExecutor(CPTimer(this))
        getCommand(CPTimer.CMD)?.tabCompleter = CPTimer(this)
        getCommand(CReset.CMD)?.setExecutor(CReset(this))
        getCommand(CReset.CMD)?.tabCompleter = CReset(this)
        getCommand(CSettings.CMD)?.setExecutor(CSettings(this))
        getCommand(CSettings.CMD)?.tabCompleter = CSettings(this)
        //register Listeners
        server.pluginManager.registerEvents(Listeners(this), this)
        server.pluginManager.registerEvents(LTimerPaused(this), this)
    }

    /** Done on plugin disabling */
    override fun onDisable() {}

    /**
     * Add a personal [Timer] for the [player]
     *
     * @throws IllegalArgumentException if the [player] already has a [Timer] assigned
     */
    fun addTimer(player: Player) {
        require(!timers.containsKey(player)) { "This player already has a timer assigned." }
        timers[player] = Timer(this, player)
    }

    /** Get the [player]'s personal [Timer] */
    fun getTimer(player: Player) = timers[player]

    /** Remove the [player]'s personal [Timer] */
    fun removeTimer(player: Player) = timers.remove(player)

    companion object {
        /** Get an instance of this plugin, if it is running */
        var instance: WorldUtils? = null
            private set
    }
}
