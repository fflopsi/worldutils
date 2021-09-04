package me.frauenfelderflorian.worldutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Enum for all Settings per command
 */
public enum Settings {
    /**
     * saveAuthor: save the player that added the position
     */
    POSITION("position", Map.of(
            "saveAuthor", false)),
    /**
     * makeAccessibleGlobally: make all personal positions available with "/personalposition playername positionname"
     */
    PERSONALPOSITION("personalposition", Map.of(
            "makeAccessibleGlobally", false)),
    /**
     * reset: used for resetting on load
     * <p>
     * needConfirm: command needs confirm as argument to work
     * <p>
     * deletePositions: delete all positions and personal positions when resetting
     * <p>
     * resetSettings: reset all settings when resetting
     */
    RESET("reset", Map.of(
            "reset", false,
            "needConfirm", true,
            "deletePositions", true,
            "resetSettings", false)),
    /**
     * needOp: player needs OP rank to change settings
     */
    SETTINGS("settings", Map.of(
            "needOp", true));
    private final String command;
    private final Map<String, Boolean> settings;

    Settings(String command, Map<String, Boolean> settings) {
        this.command = command;
        this.settings = settings;
    }

    /**
     * Get a Settings object from a command
     *
     * @param command name of the command
     * @return Settings object with command
     */
    public static Settings get(String command) {
        for (Settings setting : values())
            if (command.equals(setting.getCommand())) return setting;
        return null;
    }

    /**
     * Get the String of the command of the key
     *
     * @param key the key containing the command
     * @return String of the command
     */
    public static String getCommandFromKey(String key) {
        if (contains(key.substring(0, key.indexOf("."))))
            return key.substring(0, key.indexOf("."));
        return null;
    }

    /**
     * Get a Settings object from a key
     *
     * @param key the key belonging to the SEttings object
     * @return Settings object with key
     */
    public static Settings getFromKey(String key) {
        if (contains(getCommandFromKey(key)))
            return get(getCommandFromKey(key));
        return null;
    }

    /**
     * Get all commands
     *
     * @return List of Strings containing all commands
     */
    public static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        for (Settings setting : values())
            commands.add(setting.getCommand());
        return commands;
    }

    /**
     * Get all possible config keys
     *
     * @return List of Strings containing all keys
     */
    public static List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (Settings setting : values())
            for (String setting2 : setting.getSettings().keySet())
                keys.add(setting.getKey(setting2));
        return keys;
    }

    /**
     * Get the default value for a key
     *
     * @param key the key for which the defautl value should be returned
     * @return the default value of the key (true, false) or null if key does not exist
     */
    public static Boolean getDefaultFromKey(String key) {
        if (getKeys().contains(key))
            return Objects.requireNonNull(getFromKey(key)).getSettings().get(key.substring(key.lastIndexOf(".") + 1));
        return null;
    }

    /**
     * Check if a command exists
     *
     * @param command the command to be checked
     * @return true if command is found, false if not
     */
    public static boolean contains(String command) {
        for (Settings setting : values())
            if (command.equals(setting.getCommand())) return true;
        return false;
    }

    /**
     * Check if a command contains a setting
     *
     * @param setting the setting to be checked
     * @return true is command contains setting, fals if not
     */
    public boolean containsSetting(String setting) {
        for (String stg : getSettings().keySet())
            if (setting.equals(stg)) return true;
        return false;
    }

    /**
     * Get the command
     *
     * @return String of the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Get all Settings
     *
     * @return List of Strings containing all Settings
     */
    public Map<String, Boolean> getSettings() {
        return settings;
    }

    /**
     * Get a config key via setting string
     *
     * @param setting String to be searched for
     * @return String of the key
     */
    public String getKey(String setting) {
        return getSettings().containsKey(setting) ? getCommand() + "." + setting : null;
    }
}
