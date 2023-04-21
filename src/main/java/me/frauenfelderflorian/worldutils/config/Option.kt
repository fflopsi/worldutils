package me.frauenfelderflorian.worldutils.config

/** Enum used for plugin settings */
enum class Option(
    /** The [Command] to which the option belongs */
    private val command: Command,
    /** The subkey of the option */
    private val key: String,
    /** The default value for the option that is used when no other value is present */
    val default: Any?,
    /** True if this option should be user-settable via the settings command (e.g. be included in tab completion) */
    private val settable: Boolean,
    /** True if this option applies for all users */
    val isGlobal: Boolean,
) {
    /** Saves the author of a position on creation (default: false) */
    POSITION_SAVE_AUTHOR(Command.POSITION, "saveAuthor", false, true, true),

    /** Makes the player's personal positions accessible for everyone else (without tab completion) (default: false) */
    PPOSITION_ACCESS_GLOBAL(Command.PPOSITION, "makeAccessibleGlobally", false, true, false),

    /** The current time of the timer (default: 0) */
    TIMER_TIME(Command.TIMER, "time", 0, false, true),

    /** If the timer is running (default: false) */
    TIMER_RUNNING(Command.TIMER, "running", false, false, true),

    /** If the timer was running when the last player left the server the last time (default: false) */
    TIMER_WAS_RUNNING(Command.TIMER, "wasRunning", false, false, true),

    /** If the timer is running reversed (default: false) */
    TIMER_REVERSE(Command.TIMER, "reverse", false, false, true),

    /** Add a player to the timer automatically when joining (default: true) */
    TIMER_ADD_PLAYER_ON_JOIN(Command.TIMER, "addPlayerOnJoin", false, true, true),

    /** Start the timer automatically when the first player joins if the timer was running when the last player left the last time (default: false) */
    TIMER_START_IF_WAS_RUNNING(
        Command.TIMER, "startOnPlayerJoinIfWasRunning", false, true, true
    ),

    /** Timer progress bar displays the progress of the current minute instead of hour (default: false) */
    TIMER_PROGRESS_MINUTE(Command.TIMER, "progressMinute", false, true, true),

    /** Allows the timer to go below zero into negative values (default: false) */
    TIMER_ALLOW_BELOW_ZERO(Command.TIMER, "allowBelowZero", false, true, true),

    /** Disables player interactions when timer is paused (default: true) */
    TIMER_DISABLE_ACTIONS_ON_PAUSE(
        Command.TIMER, "disableInteractionsOnPause", true, true, true
    ),

    /** Disables player movement when timer is paused (default: false) */
    TIMER_DISABLE_MOVEMENT_ON_PAUSE(Command.TIMER, "disableMovementOnPause", false, true, true),

    /** Timer pauses when the ender dragon is defeated (default: true) */
    TIMER_PAUSE_ON_DRAGON_DEATH(Command.TIMER, "pauseOnDragonDeath", true, true, true),

    /** The current time of the personal timer (default: 0) */
    PTIMER_TIME(Command.PTIMER, "time", 0, false, false),

    /** If the personal timer is visible (default: false) */
    PTIMER_VISIBLE(Command.PTIMER, "visible", false, false, false),

    /** If the personal timer is running (default: false) */
    PTIMER_RUNNING(Command.PTIMER, "running", false, false, false),

    /** If the personal timer was running when the player left the server the last time (default: false) */
    PTIMER_WAS_RUNNING(Command.PTIMER, "wasRunning", false, false, false),

    /** If the personal timer is running reversed (default: false) */
    PTIMER_REVERSE(Command.PTIMER, "reverse", false, false, false),

    /** Show the personal timer automatically when the player joins (default: false) */
    PTIMER_VISIBLE_ON_JOIN(Command.PTIMER, "visibleOnJoin", false, true, false),

    /** Start the timer automatically when the player joins if the timer was running when the player left the last time (default: false) */
    PTIMER_START_IF_WAS_RUNNING(
        Command.PTIMER, "startOnPlayerJoinIfWasRunning", false, true, false
    ),

    /** Personal timer progress bar displays the progress of the current minute instead of hour (default: false) */
    PTIMER_PROGRESS_MINUTE(Command.PTIMER, "progressMinute", false, true, false),

    /** Allows the personal timer to go below zero into negative values (default: false) */
    PTIMER_ALLOW_BELOW_ZERO(Command.PTIMER, "allowBelowZero", false, true, false),

    /** Personal timer pauses when the ender dragon is defeated (default: true) */
    PTIMER_PAUSE_ON_DRAGON_DEATH(Command.PTIMER, "pauseOnDragonDeath", true, true, false),

    /** Make the personal timer "joinable" for other players (default: false) */
    PTIMER_JOINABLE(Command.PTIMER, "joinable", false, true, false),

    /** If a reset has been requested and not yet been confirmed (default: false) */
    RESET_REQUESTED(Command.RESET, "requested", false, false, true),

    /** If the world is going to be reset on next load (default: false) */
    RESET_RESET(Command.RESET, "reset", false, false, true),

    /** Player needs OP to reset the server (default: true) */
    RESET_NEED_OP(Command.RESET, "needOp", true, true, true),

    /** Deletes all positions on server reset (default: true) */
    RESET_DELETE_POSITIONS(Command.RESET, "deletePositions", true, true, true),

    /** Resets settings to defaults on server reset (default: false) */
    RESET_RESET_SETTINGS(Command.RESET, "resetSettings", false, true, true),

    /** Restarts the server after the reset command has been entered (default: true) */
    RESET_RESTART_AFTER_RESET(Command.RESET, "restartAfterReset", true, true, true),

    /** Player needs OP to change settings (default: true) */
    SETTINGS_NEED_OP(Command.SETTINGS, "needOp", true, true, true),

    //all the following options are only used in the other plugins that depend on WorldUtils
    //defaultValue: null, settable: false for all the following options

    /** If the AllItems project is running (default: null) */
    WUP_ALLITEMS_RUNNING(Command.WUPROJECTS_ALLITEMS, "running", null, false, true),

    /** List which contains all obtainable items (default: null) */
    WUP_ALLITMES_ITEMS(Command.WUPROJECTS_ALLITEMS, "items", null, false, true),

    /** Index of the next item to collect (default: null) */
    WUP_ALLITMES_INDEX(Command.WUPROJECTS_ALLITEMS, "index", null, false, true),

    /** List which contains all already obtained items (default: null) */
    WUP_ALLITEMS_OBTAINED(Command.WUPROJECTS_ALLITEMS, "obtained", null, false, true),

    /** If the LevelBorder project is running (default: null) */
    WUP_LEVELBORDER_RUNNING(Command.WUPROJECTS_LEVELBORDER, "running", null, false, true),

    /** If the LevelBorder project is running in multiplayer-compatible shared-experience mode (default: null) */
    WUP_LEVELBORDER_SHARED_EXP(Command.WUPROJECTS_LEVELBORDER, "sharedExp", null, false, true);

    /** Check if option is vanilla, which means belongs to this plugin */
    val isVanilla = command.isVanilla
    val configKey = "${command.command}.$key"

    /** Enum containing all plugin commands */
    private enum class Command(val command: String, val isVanilla: Boolean) {
        POSITION("position", true),
        PPOSITION("personalposition", true),
        TIMER("timer", true),
        PTIMER("personaltimer", true),
        RESET("reset", true),
        SETTINGS("settings", true),

        //all the following commands are only used in the other plugins that depend on WorldUtils
        //vanilla: false for all the following options
        WUPROJECTS_ALLITEMS("wuprojects.allitems", false),
        WUPROJECTS_LEVELBORDER("wuprojects.levelborder", false);
    }

    companion object {
        /** Get a [Option] from [command] and [subKey] */
        operator fun get(command: String, subKey: String): Option? {
            values().forEach {
                if (command == it.command.command && subKey == it.key) return it
            }
            return null
        }

        /** Get all commands */
        val commands = Command.values().map { it.command }

        /** Get commands that either contain [global] or personal settings */
        fun getCommands(global: Boolean) =
            values().filter { global == it.isGlobal }.map { it.command.command }.toSet()

        /** Get all (user) settable settings for the [command] */
        fun getSettings(command: String) =
            values().filter { command == it.command.command && it.settable }.map { it.key }

        /** Get all (user) settable [global] or personal settings for the [command] */
        fun getSettings(command: String, global: Boolean) =
            values().filter { command == it.command.command && it.settable && global == it.isGlobal }
                .map { it.key }
    }
}