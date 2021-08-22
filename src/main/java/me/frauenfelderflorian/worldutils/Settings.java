package me.frauenfelderflorian.worldutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for all Settings per command
 */
public enum Settings {
    /**
     * index - setting
     * <p>
     * 0 - saveAuthor
     */
    POSITION("position", List.of("saveAuthor")),
    /**
     * index - setting
     * <p>
     * 0 - makeAccessibleGlobally
     */
    PERSONALPOSITION("personalposition", List.of("makeAccessibleGlobally")),
    /**
     * index - setting
     * <p>
     * 0 - needConfirm
     * <p>
     * 1 - restartAfterReset
     */
    RESET("reset", List.of("needConfirm", "restartAfterReset"));
    private final String command;
    private final List<String> settings;

    Settings(String command, List<String> settings) {
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
     * Check if a command exists
     *
     * @param command the command to be checked
     * @return true if command is found, false if not
     */
    public static boolean contains(String command) {
        return get(command) != null;
    }

    /**
     * Check if a command contains a setting
     *
     * @param setting the setting to be checked
     * @return true is command contains setting, fals if not
     */
    public boolean containsSetting(String setting) {
        for (String stg : getSettings())
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
    public List<String> getSettings() {
        return settings;
    }

    /**
     * Get a config key via index
     *
     * @param index number of the setting
     * @return String of the key
     */
    public String getKey(int index) {
        return getCommand() + "." + getSettings().get(index);
    }

    /**
     * Get a config key via setting string
     *
     * @param setting String to be searched for
     * @return String of the key
     */
    public String getKey(String setting) {
        return getSettings().contains(setting) ? getCommand() + "." + setting : null;
    }
}
