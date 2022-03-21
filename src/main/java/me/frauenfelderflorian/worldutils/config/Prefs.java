package me.frauenfelderflorian.worldutils.config;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for configuration file, extends config.Config
 */
public class Prefs extends Config {
    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin the plugin to whom the Prefs belongs
     */
    public Prefs(WorldUtils plugin) {
        super(plugin, "config.yml");
    }

    /**
     * Set a setting value
     *
     * @param setting the setting to be set
     * @param value   the value Object of the setting
     * @param log     true if logging messages should be sent
     */
    public void set(Option setting, Object value, boolean log) {
        set(setting.getKey(), value, log);
    }

    /**
     * Set a personal setting value
     *
     * @param player  the Player whose setting to set
     * @param setting the setting to be set
     * @param value   the value Object of the setting
     * @param log     true if logging messages should be sent
     */
    public void set(Player player, Option setting, Object value, boolean log) {
        set(getPrefix(player) + setting.getKey(), value, log);
    }

    /**
     * Check if the setting contains a value
     *
     * @param setting the setting to be checked
     * @return true if something is found, false if not
     */
    public boolean contains(Option setting) {
        return contains(setting.getKey());
    }

    /**
     * Check if the personal setting contains a value
     *
     * @param player  the Player whose setting to check
     * @param setting the setting to check
     * @return true if something is found, false if not
     */
    public boolean contains(Player player, Option setting) {
        return contains(getPrefix(player) + setting.getKey());
    }

    /**
     * Get a List value from a setting
     *
     * @param setting the setting
     * @return the List at the setting's path
     */
    public List<?> getList(Option setting) {
        return config.getList(setting.getKey());
    }

    /**
     * Get a boolean value from a setting
     *
     * @param setting the setting
     * @return the boolean at the setting's path
     */
    public boolean getBoolean(Option setting) {
        return config.getBoolean(setting.getKey());
    }

    /**
     * Get a boolean value from a setting
     *
     * @param player  the Player whose setting to get
     * @param setting the setting
     * @return the boolean at the setting's path
     */
    public boolean getBoolean(Player player, Option setting) {
        return config.getBoolean(getPrefix(player) + setting.getKey());
    }

    /**
     * Get an int value from a setting
     *
     * @param setting the setting
     * @return the int at the setting's path
     */
    public int getInt(Option setting) {
        return config.getInt(setting.getKey());
    }

    /**
     * Get an int value from a setting
     *
     * @param player  the player whose setting to get
     * @param setting the setting
     * @return the int at the setting's path
     */
    public int getInt(Player player, Option setting) {
        return config.getInt(getPrefix(player) + setting.getKey());
    }

    /**
     * Remove a setting
     *
     * @param setting the setting to remove
     */
    public void remove(Option setting) {
        remove(setting.getKey());
    }

    /**
     * Remove a personal setting
     *
     * @param player  the player whose setting to remove
     * @param setting the setting to remove
     */
    public void remove(Player player, Option setting) {
        remove(getPrefix(player) + setting.getKey());
    }

    /**
     * Enum used for plugin settings
     */
    public enum Option {
        /**
         * Saves the author of a position on creation (default: false)
         */
        POSITION_SAVE_AUTHOR(Command.POSITION, "saveAuthor", false, true, true),
        /**
         * Makes the player's personal positions accessible for everyone else (without tab completion) (default: false)
         */
        PPOSITION_ACCESS_GLOBAL(Command.PPOSITION, "makeAccessibleGlobally", false, true, false),
        /**
         * The current time of the timer (default: 0)
         */
        TIMER_TIME(Command.TIMER, "time", 0, false, true),
        /**
         * If the timer is running (default: false)
         */
        TIMER_RUNNING(Command.TIMER, "running", false, false, true),
        /**
         * If the timer was running when the last player left the server the last time (default: false)
         */
        TIMER_WAS_RUNNING(Command.TIMER, "wasRunning", false, false, true),
        /**
         * If the timer is running reversed (default: false)
         */
        TIMER_REVERSE(Command.TIMER, "reverse", false, false, true),
        /**
         * Add a player to the timer automatically when joining (default: true)
         */
        TIMER_ADD_PLAYER_ON_JOIN(Command.TIMER, "addPlayerOnJoin", false, true, true),
        /**
         * Start the timer automatically when the first player joins if the timer was running when the last player left
         * the last time (default: false)
         */
        TIMER_START_IF_WAS_RUNNING(Command.TIMER, "startOnPlayerJoinIfWasRunning", false, true, true),
        /**
         * Timer progress bar displays the progress of the current minute instead of hour (default: false)
         */
        TIMER_PROGRESS_MINUTE(Command.TIMER, "progressMinute", false, true, true),
        /**
         * Allows the timer to go below zero into negative values (default: false)
         */
        TIMER_ALLOW_BELOW_ZERO(Command.TIMER, "allowBelowZero", false, true, true),
        /**
         * Disables player interactions when timer is paused (default: true)
         */
        TIMER_DISABLE_ACTIONS_ON_PAUSE(Command.TIMER, "disableInteractionsOnPause", true, true, true),
        /**
         * Disables player movement when timer is paused (default: false)
         */
        TIMER_DISABLE_MOVEMENT_ON_PAUSE(Command.TIMER, "disableMovementOnPause", false, true, true),
        /**
         * Timer pauses when the ender dragon is defeated (default: true)
         */
        TIMER_PAUSE_ON_DRAGON_DEATH(Command.TIMER, "pauseOnDragonDeath", true, true, true),
        /**
         * The current time of the personal timer (default: 0)
         */
        PTIMER_TIME(Command.PTIMER, "time", 0, false, false),
        /**
         * If the personal timer is visible (default: false)
         */
        PTIMER_VISIBLE(Command.PTIMER, "visible", false, false, false),
        /**
         * If the personal timer is running (default: false)
         */
        PTIMER_RUNNING(Command.PTIMER, "running", false, false, false),
        /**
         * If the personal timer was running when the player left the server the last time (default: false)
         */
        PTIMER_WAS_RUNNING(Command.PTIMER, "wasRunning", false, false, false),
        /**
         * If the personal timer is running reversed (default: false)
         */
        PTIMER_REVERSE(Command.PTIMER, "reverse", false, false, false),
        /**
         * Show the personal timer automatically when the player joins (default: false)
         */
        PTIMER_VISIBLE_ON_JOIN(Command.PTIMER, "visibleOnJoin", false, true, false),
        /**
         * Start the timer automatically when the player joins if the timer was running when the player left the last
         * time (default: false)
         */
        PTIMER_START_IF_WAS_RUNNING(Command.PTIMER, "startOnPlayerJoinIfWasRunning", false, true, false),
        /**
         * Personal timer progress bar displays the progress of the current minute instead of hour (default: false)
         */
        PTIMER_PROGRESS_MINUTE(Command.PTIMER, "progressMinute", false, true, false),
        /**
         * Allows the personal timer to go below zero into negative values (default: false)
         */
        PTIMER_ALLOW_BELOW_ZERO(Command.PTIMER, "allowBelowZero", false, true, false),
        /**
         * Personal timer pauses when the ender dragon is defeated (default: true)
         */
        PTIMER_PAUSE_ON_DRAGON_DEATH(Command.PTIMER, "pauseOnDragonDeath", true, true, false),
        /**
         * Make the personal timer "joinable" for other players (default: false)
         */
        PTIMER_JOINABLE(Command.PTIMER, "joinable", false, true, false),
        /**
         * If a reset has been requested and not yet been confirmed (default: false)
         */
        RESET_REQUESTED(Command.RESET, "requested", false, false, true),
        /**
         * If the world is going to be reset on next load (default: false)
         */
        RESET_RESET(Command.RESET, "reset", false, false, true),
        /**
         * Player needs OP to reset the server (default: true)
         */
        RESET_NEED_OP(Command.RESET, "needOp", true, true, true),
        /**
         * Deletes all positions on server reset (default: true)
         */
        RESET_DELETE_POSITIONS(Command.RESET, "deletePositions", true, true, true),
        /**
         * Resets settings to defaults on server reset (default: false)
         */
        RESET_RESET_SETTINGS(Command.RESET, "resetSettings", false, true, true),
        /**
         * Restarts the server after the reset command has been entered (default: true)
         */
        RESET_RESTART_AFTER_RESET(Command.RESET, "restartAfterReset", true, true, true),
        /**
         * Player needs OP to change settings (default: true)
         */
        SETTINGS_NEED_OP(Command.SETTINGS, "needOp", true, true, true),

        //all the following options are only used in the other plugins that depend on WorldUtils
        //defaultValue: null, settable: false for all the following options
        /**
         * If the AllItems project is running (default: null)
         */
        WUP_ALLITEMS_RUNNING(Command.WUPROJECTS_ALLITEMS, "running", null, false, true),
        /**
         * List which contains all obtainable items (default: null)
         */
        WUP_ALLITMES_ITEMS(Command.WUPROJECTS_ALLITEMS, "items", null, false, true),
        /**
         * Index of the next item to collect (default: null)
         */
        WUP_ALLITMES_INDEX(Command.WUPROJECTS_ALLITEMS, "index", null, false, true),
        /**
         * List which contains all already obtained items (default: null)
         */
        WUP_ALLITEMS_OBTAINED(Command.WUPROJECTS_ALLITEMS, "obtained", null, false, true),
        /**
         * If the LevelBorder project is running (default: null)
         */
        WUP_LEVELBORDER_RUNNING(Command.WUPROJECTS_LEVELBORDER, "running", null, false, true),
        /**
         * If the LevelBorder project is running in multiplayer-compatible shared-experience mode (default: null)
         */
        WUP_LEVELBORDER_SHARED_EXP(Command.WUPROJECTS_LEVELBORDER, "sharedExp", null, false, true),
        ;

        /**
         * The command to which the option belongs
         */
        private final Command command;
        /**
         * The subkey of the option
         */
        private final String subKey;
        /**
         * The default value for the option that is used when no other value is present
         */
        private final Object defaultValue;
        /**
         * True if this option should be user-settable via the settings command (e.g. be included in tab completion)
         */
        private final boolean settable;
        /**
         * True if this option applies for all users
         */
        private final boolean global;

        Option(Command command, String subKey, Object defaultValue, boolean settable, boolean global) {
            this.command = command;
            this.subKey = subKey;
            this.defaultValue = defaultValue;
            this.settable = settable;
            this.global = global;
        }

        /**
         * Get a Settings object from command and setting sub-key
         *
         * @param command command of setting
         * @param subKey  sub-key of setting
         * @return Settings object if found, else null
         */
        public static Option get(String command, String subKey) {
            for (Option setting : Option.values())
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
         * Get commands that either contain global or personal settings
         *
         * @param global true if "global commands" should be returned, false if "personal commands" should be returned
         * @return List of Strings containing the commands
         */
        public static List<String> getCommands(boolean global) {
            List<String> commands = new ArrayList<>();
            for (Command command : Command.values()) {
                boolean scope = false;
                for (Option stg : values()) {
                    if (stg.command == command && (global && stg.isGlobal() || !global && !stg.isGlobal())) {
                        scope = true;
                        break;
                    }
                }
                if (scope) commands.add(command.getCommand());
            }
            return commands;
        }

        /**
         * Get all (user) settable settings for a command
         *
         * @param command command to be searched for
         * @return List of Strings of setting subkeys
         */
        public static List<String> getSettings(String command) {
            List<String> settings = new ArrayList<>();
            for (Option stg : Option.values())
                if (command.equals(stg.command.getCommand()) && stg.settable) settings.add(stg.subKey);
            return settings;
        }

        /**
         * Get all (user) settable global or personal settings for a command
         *
         * @param command command to be searched for
         * @param global  true if global settings should be returned, false if personal settings should be returned
         * @return List of Strings containing setting subkeys
         */
        public static List<String> getSettings(String command, boolean global) {
            List<String> settings = new ArrayList<>();
            for (Option stg : values()) {
                if (command.equals(stg.command.getCommand()) && stg.settable
                        && (global && stg.isGlobal() || !global && !stg.isGlobal()))
                    settings.add(stg.subKey);
            }
            return settings;
        }

        /**
         * Check if option is vanilla, which means belongs to this plugin
         *
         * @return true if in this plugin, false otherwise
         */
        public boolean isVanilla() {
            return command.isVanilla();
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
         * Check if option is global, which means applies to all users
         *
         * @return true if global, false otherwise
         */
        public boolean isGlobal() {
            return global;
        }

        /**
         * Enum containing all plugin commands
         */
        private enum Command {
            POSITION("position", true),
            PPOSITION("personalposition", true),
            SENDPOSITION("sendposition", true),
            TIMER("timer", true),
            PTIMER("personaltimer", true),
            RESET("reset", true),
            SETTINGS("settings", true),

            //all the following commands are only used in the other plugins that depend on WorldUtils
            //vanilla: false for all the following options
            WUPROJECTS_ALLITEMS("wuprojects.allitems", false),
            WUPROJECTS_LEVELBORDER("wuprojects.levelborder", false),
            ;

            /**
             * The String of the command which the user enters
             */
            private final String command;
            /**
             * True if this Command belongs to this plugin
             */
            private final boolean vanilla;

            Command(String command, boolean vanilla) {
                this.command = command;
                this.vanilla = vanilla;
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
             * Check if command is vanilla, which means belongs to this plugin
             *
             * @return true if in this plugin, false otherwise
             */
            public boolean isVanilla() {
                return vanilla;
            }
        }
    }
}
