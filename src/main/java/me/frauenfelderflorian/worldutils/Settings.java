package me.frauenfelderflorian.worldutils;

import java.util.ArrayList;
import java.util.List;

public enum Settings {
    POSITION("position", List.of("saveAuthor")),
    PERSONALPOSITION("personalposition", List.of("makeAccessibleGlobally")),
    RESET("reset", List.of("needConfirm", "restartAfterReset"));
    private final String command;
    private final List<String> settings;

    Settings(String command, List<String> settings) {
        this.command = command;
        this.settings = settings;
    }

    public static Settings get(String command) {
        for (Settings setting : values())
            if (command.equals(setting.getCommand())) return setting;
        return null;
    }

    public static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        for (Settings setting : values())
            commands.add(setting.getCommand());
        return commands;
    }

    public static boolean contains(String command) {
        return get(command) != null;
    }

    public boolean containsSetting(String setting) {
        for (String stg : getSettings()) {
            if (setting.equals(stg)) return true;
        }
        return false;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getSettings() {
        return settings;
    }

    public String getKey(int index) {
        return getCommand() + "." + getSettings().get(index);
    }

    public String getKey(String setting) {
        return getSettings().contains(setting) ? getCommand() + "." + setting : null;
    }
}
