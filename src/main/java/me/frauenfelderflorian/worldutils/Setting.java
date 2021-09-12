package me.frauenfelderflorian.worldutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for plugin settings
 */
public enum Setting {
    ;

    /**
     * Get a Command object (a "setting") from a command and a setting String
     *
     * @param command String of the command
     * @param setting String of the setting
     * @return a Command object if setting belongs to command, null otherwise
     */
    public static Command get(String command, String setting) {
        switch (command) {
            //optimizable? pass enum as argument?
            case "position" -> {
                for (Position stg : Position.values())
                    if (setting.equals(stg.subKey)) return stg;
            }
            case "personalposition" -> {
                for (PersonalPosition stg : PersonalPosition.values())
                    if (setting.equals(stg.subKey)) return stg;
            }
            case "sendposition" -> {
                for (SendPosition stg : SendPosition.values())
                    if (setting.equals(stg.subKey)) return stg;
            }
            case "reset" -> {
                for (Reset stg : Reset.values())
                    if (setting.equals(stg.subKey)) return stg;
            }
            case "settings" -> {
                for (Settings stg : Settings.values())
                    if (setting.equals(stg.subKey)) return stg;
            }
        }
        return null;
    }

    /**
     * Get all possible settings
     *
     * @return List of Command objects containing all settings
     */
    public static List<Command> getAll() {
        List<Command> settings = new ArrayList<>();
        settings.addAll(List.of(Position.values()));
        settings.addAll(List.of(PersonalPosition.values()));
        settings.addAll(List.of(SendPosition.values()));
        settings.addAll(List.of(Reset.values()));
        settings.addAll(List.of(Settings.values()));
        return settings;
    }

    /**
     * Get all possible commands
     *
     * @return List of Strings containing all commands
     */
    public static List<String> getCommands() {
        return List.of("position", "personalposition", "sendposition", "reset", "settings");
    }

    /**
     * Get all settings of a command
     *
     * @param command String of the command
     * @return List of Strings containing all settings if command is found, null otherwise
     */
    public static List<String> getSettings(String command) {
        switch (command) {
            case "position" -> {
                List<String> settings = new ArrayList<>();
                for (Position setting : Position.values())
                    settings.add(setting.subKey);
                return settings;
            }
            case "personalposition" -> {
                List<String> settings = new ArrayList<>();
                for (PersonalPosition setting : PersonalPosition.values())
                    settings.add(setting.subKey);
                return settings;

            }
            case "sendposition" -> {
                List<String> settings = new ArrayList<>();
                for (SendPosition setting : SendPosition.values())
                    settings.add(setting.subKey);
                return settings;

            }
            case "reset" -> {
                List<String> settings = new ArrayList<>();
                for (Reset setting : Reset.values())
                    settings.add(setting.subKey);
                return settings;

            }
            case "settings" -> {
                List<String> settings = new ArrayList<>();
                for (Settings setting : Settings.values())
                    settings.add(setting.subKey);
                return settings;

            }
        }
        return null;
    }

    /**
     * Interface for settings
     */
    public interface Command {
        /**
         * Get the config key for the setting
         *
         * @return String containing the key
         */
        String getKey();

        /**
         * Get the default value for the setting
         *
         * @return boolean default value
         */
        boolean getDefault();
    }

    /**
     * Enum for settings for command "position"
     * <p>
     * implements Command
     */
    public enum Position implements Command {
        /**
         * Saves the author of a position on creation
         * <p>
         * default: false
         */
        SAVE_AUTHOR("saveAuthor", false);

        private final String subKey;
        private final boolean defaultValue;

        Position(String subKey, boolean defaultValue) {
            this.subKey = subKey;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return "position." + subKey;
        }

        @Override
        public boolean getDefault() {
            return defaultValue;
        }
    }

    /**
     * Enum for settings for command "personalposition"
     * <p>
     * implements Command
     */
    public enum PersonalPosition implements Command {
        /**
         * Makes everyone's personal positions accessible for everyone else (without tab completion)
         * <p>
         * default: false
         */
        MAKE_ACCESSIBLE_GLOBALLY("makeAccessibleGlobally", false);

        private final String subKey;
        private final boolean defaultValue;

        PersonalPosition(String subKey, boolean defaultValue) {
            this.subKey = subKey;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return "personalposition." + subKey;
        }

        @Override
        public boolean getDefault() {
            return defaultValue;
        }
    }

    /**
     * Enum for settings for command "sendposition"
     * <p>
     * implements Command
     */
    public enum SendPosition implements Command {
        ;

        private final String subKey;
        private final boolean defaultValue;

        SendPosition(String subKey, boolean defaultValue) {
            this.subKey = subKey;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return "sendposition." + subKey;
        }

        @Override
        public boolean getDefault() {
            return defaultValue;
        }
    }

    /**
     * Enum for settings for command "reset"
     * <p>
     * implements Command
     */
    public enum Reset implements Command {
        /**
         * Resets the server during next start
         * <p>
         * default: false
         */
        RESET("reset", false),
        /**
         * "confirm" needed as argument to reset the server
         * <p>
         * default: true
         */
        NEED_CONFIRM("needConfirm", true),
        /**
         * Deletes all personal and global positions on reset
         * <p>
         * default: true
         */
        DELETE_POSITIONS("deletePositions", true),
        /**
         * Resets all settings on reset
         * <p>
         * default: false
         */
        RESET_SETTINGS("resetSettings", false);

        private final String subKey;
        private final boolean defaultValue;

        Reset(String subKey, boolean defaultValue) {
            this.subKey = subKey;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return "reset." + subKey;
        }

        @Override
        public boolean getDefault() {
            return defaultValue;
        }
    }

    /**
     * Enum for settings for command "settings"
     * <p>
     * implements Command
     */
    public enum Settings implements Command {
        /**
         * OP is needed to change settings
         * <p>
         * default: true
         */
        NEED_OP("needOp", true);

        private final String subKey;
        private final boolean defaultValue;

        Settings(String subKey, boolean defaultValue) {
            this.subKey = subKey;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKey() {
            return "settings." + subKey;
        }

        @Override
        public boolean getDefault() {
            return defaultValue;
        }
    }
}
