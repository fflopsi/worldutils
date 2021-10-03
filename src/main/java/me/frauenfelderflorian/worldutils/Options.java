package me.frauenfelderflorian.worldutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum used for plugin settings
 */
public enum Options {
    /**
     * Saves the author of a position on creation (default: false)
     */
    POSITION_SAVE_AUTHOR("position", "saveAuthor", false, true),
    /**
     * Makes everyone's personal positions accessible for everyone else (without tab completion) (default: false)
     */
    PERSONALPOSITION_ACCESS_GLOBAL("personalposition", "makeAccessibleGlobally", false, true),
    /**
     * Make the timer visible automatically on server startup (default: false)
     */
    TIMER_TIME("timer", "time", 0, false),
    /**
     * If the timer is running (default: false)
     */
    TIMER_RUNNING("timer", "running", false, false),
    /**
     * If the timer is running reversed (default: false)
     */
    TIMER_REVERSE("timer", "reverse", false, false),
    /**
     * Add a player to the timer automatically when joining (default: true)
     */
    TIMER_ADD_PLAYER_ON_JOIN("timer", "addPlayerOnJoin", false, true),
    /**
     * Timer progress bar displays the progress of the current minute instead of hour (default: false)
     */
    TIMER_PROGRESS_MINUTE("timer", "progressMinute", false, true),
    /**
     * Disables player interactions when timer is paused (default: true)
     */
    TIMER_DISABLE_ACTIONS_ON_PAUSE("timer", "disableInteractionsOnPause", true, true),
    /**
     * Disables player movement when timer is paused (default: false)
     */
    TIMER_DISABLE_MOVEMENT_ON_PAUSE("timer", "disableMovementOnPause", false, true),
    /**
     * Timer stops when the dragon defeated (default: true)
     */
    TIMER_STOP_ON_DRAGON_DEATH("timer", "stopOnDragonDeath", true, true),
    /**
     * If the world is going to be reset on next load (default: false)
     */
    RESET_RESET("reset", "reset", false, false),
    /**
     * Reset command needs confirm as first and only argument to start resetting world (default: true)
     */
    RESET_NEED_CONFIRM("reset", "needConfirm", true, true),
    /**
     * Deletes all positions on server reset (default: true)
     */
    RESET_DELETE_POSITIONS("reset", "deletePositions", true, true),
    /**
     * Resets settings to defaults on server reset (default: false)
     */
    RESET_RESET_SETTINGS("reset", "resetSettings", false, true),
    /**
     * Player needs OP to be able to change settings (default: true)
     */
    SETTINGS_NEED_OP("settings", "needOp", true, true),
    ;

    private final String command;
    private final String subKey;
    private final Object defaultValue;
    private final boolean settable;

    Options(String command, String subKey, Object defaultValue, boolean settable) {
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
    public static Options get(String command, String subKey) {
        for (Options setting : values())
            if (command.equals(setting.command) && subKey.equals(setting.subKey)) return setting;
        return null;
    }

    /**
     * Get all commands
     *
     * @return List of Strings containing all commands
     */
    public static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        for (Options setting : values()) if (!commands.contains(setting.command)) commands.add(setting.command);
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
        for (Options stg : values()) if (command.equals(stg.command) && stg.settable) settings.add(stg.subKey);
        return settings;
    }

    /**
     * Get the config key
     *
     * @return String of the config key
     */
    public String getKey() {
        return command + "." + subKey;
    }

    /**
     * Get the default setting
     *
     * @return Object of the default setting
     */
    public Object getDefault() {
        return defaultValue;
    }
}
