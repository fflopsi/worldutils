package me.frauenfelderflorian.worldutils.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum used for plugin settings
 */
public enum Option {
    /**
     * Saves the author of a position on creation (default: false)
     */
    POSITION_SAVE_AUTHOR(Command.POSITION, "saveAuthor", false, true),
    /**
     * Makes everyone's personal positions accessible for everyone else (without tab completion) (default: false)
     */
    PERSONALPOSITION_ACCESS_GLOBAL(Command.PERSONALPOSITION, "makeAccessibleGlobally", false, true),
    /**
     * The current time of the timer (default: 0)
     */
    TIMER_TIME(Command.TIMER, "time", 0, false),
    /**
     * If the timer is running (default: false)
     */
    TIMER_RUNNING(Command.TIMER, "running", false, false),
    /**
     * If the timer is running reversed (default: false)
     */
    TIMER_REVERSE(Command.TIMER, "reverse", false, false),
    /**
     * Add a player to the timer automatically when joining (default: true)
     */
    TIMER_ADD_PLAYER_ON_JOIN(Command.TIMER, "addPlayerOnJoin", false, true),
    /**
     * Timer progress bar displays the progress of the current minute instead of hour (default: false)
     */
    TIMER_PROGRESS_MINUTE(Command.TIMER, "progressMinute", false, true),
    /**
     * Allows the timer to go below zero into negative values (default: false)
     */
    TIMER_ALLOW_BELOW_ZERO(Command.TIMER, "allowBelowZero", false, true),
    /**
     * Disables player interactions when timer is paused (default: true)
     */
    TIMER_DISABLE_ACTIONS_ON_PAUSE(Command.TIMER, "disableInteractionsOnPause", true, true),
    /**
     * Disables player movement when timer is paused (default: false)
     */
    TIMER_DISABLE_MOVEMENT_ON_PAUSE(Command.TIMER, "disableMovementOnPause", false, true),
    /**
     * Timer pauses when the ender dragon is defeated (default: true)
     */
    TIMER_PAUSE_ON_DRAGON_DEATH(Command.TIMER, "pauseOnDragonDeath", true, true),
    /**
     * If the world is going to be reset on next load (default: false)
     */
    RESET_RESET(Command.RESET, "reset", false, false),
    /**
     * Reset command needs confirm as first and only argument to start resetting world (default: true)
     */
    RESET_NEED_CONFIRM(Command.RESET, "needConfirm", true, true),
    /**
     * Deletes all positions on server reset (default: true)
     */
    RESET_DELETE_POSITIONS(Command.RESET, "deletePositions", true, true),
    /**
     * Resets settings to defaults on server reset (default: false)
     */
    RESET_RESET_SETTINGS(Command.RESET, "resetSettings", false, true),
    /**
     * Restarts the server after the reset command has been entered (default: true)
     */
    RESET_RESTART_AFTER_RESET(Command.RESET, "restartAfterReset", true, true),
    /**
     * Player needs OP to be able to change settings (default: true)
     */
    SETTINGS_NEED_OP(Command.SETTINGS, "needOp", true, true),
    ;

    private final Command command;
    private final String subKey;
    private final Object defaultValue;
    private final boolean settable;

    Option(Command command, String subKey, Object defaultValue, boolean settable) {
        this.command = command;
        this.subKey = subKey;
        this.defaultValue = defaultValue;
        this.settable = settable;
    }

    /**
     * Get a Settings object from command and setting sub-key
     *
     * @param command command of setting
     * @param subKey  sub-key of setting
     * @return Settings object if found, else null
     */
    public static Option get(String command, String subKey) {
        for (Option setting : values())
            if (command.equals(setting.command.getCommand()) && subKey.equals(setting.subKey)) return setting;
        return null;
    }

    /**
     * Get all commands
     *
     * @return List of Strings containing all commands
     */
    public static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        for (Command command : Command.values()) commands.add(command.getCommand());
        return commands;
    }

    /**
     * Get all (user) settable settings for a command
     *
     * @param command command to be searched for
     * @return List of Settings objects for command
     */
    public static List<String> getSettings(String command) {
        List<String> settings = new ArrayList<>();
        for (Option stg : values())
            if (command.equals(stg.command.getCommand()) && stg.settable) settings.add(stg.subKey);
        return settings;
    }

    /**
     * Get the config key
     *
     * @return String of the config key
     */
    public String getKey() {
        return command.getCommand() + "." + subKey;
    }

    /**
     * Get the default setting
     *
     * @return Object of the default setting
     */
    public Object getDefault() {
        return defaultValue;
    }

    /**
     * Enum containing all plugin commands
     */
    private enum Command {
        POSITION("position"),
        PERSONALPOSITION("personalposition"),
        SENDPOSITION("sendposition"),
        TIMER("timer"),
        RESET("reset"),
        SETTINGS("settings"),
        ;

        private final String command;

        Command(String command) {
            this.command = command;
        }

        /**
         * Get the command
         *
         * @return String of the command
         */
        public String getCommand() {
            return command;
        }
    }
}
