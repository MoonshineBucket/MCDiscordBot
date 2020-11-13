package com.github.dreamsmoke.discord.bot.api;

import com.github.dreamsmoke.discord.bot.configuration.DSConfiguration;
import com.github.dreamsmoke.discord.bot.managers.DSBotManager;
import cpw.mods.fml.common.FMLLog;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class DSBotAPI {

    protected static DSBotAPI instance;

    public static final String COMMAND_SPLIT = DSConfiguration.instance().getCommandSplit();
    protected Map<String, Command> commandMap;

    public DSBotAPI() {
        commandMap = new HashMap<>();
    }

    public static DSBotAPI instance() {
        if(instance == null) instance = new DSBotAPI();
        return instance;
    }

    public static Command getCommand(String string) {
        DSBotAPI instance = instance();
        String s = string.toLowerCase().substring(1);
        if(instance.commandMap.containsKey(s)) return instance.commandMap.get(s);
        for(Command command : instance.commandMap.values()) {
            if(command == null) continue;
            if(command.aliases.equals(s))
                return command;
        }

        return null;
    }

    public static void registerCommands(Command... commands) {
        for(Command command : commands) {
            if(command == null) continue;

            String string = command.label;
            DSBotAPI instance = instance();

            if(instance.commandMap.containsKey(string)) continue;
            else {
                instance.commandMap.put(string, command);
                FMLLog.info("Registered DSBotAPI.Command: %s, aliases: %s, as class: %s.", string,
                        command.aliases.toString(), command.getClass().getName());
            }
        }
    }

    public static boolean equalsCommand(String string) {
        boolean equals = string.length() > 1 && string.startsWith(COMMAND_SPLIT);
        if(equals) {
            String s = string.substring(1);

            DSBotAPI instance = instance();
            if(instance.commandMap.containsKey(s)) return true;
            for(Command command : instance.commandMap.values()) {
                if(command == null) continue;
                if(command.aliases.equals(s))
                    return true;
            }
        }

        return false;
    }

    public static String getException(Throwable throwable) {
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;

        String string = throwable.toString();

        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);

            throwable.printStackTrace(printWriter);
            string = stringWriter.toString();
        } finally {
            try {
                if(stringWriter != null) stringWriter.close();
                if(printWriter != null) printWriter.close();
            } catch (IOException exception) {

            }

        }

        return string;
    }

    public static class Message {
        protected String prefix, string, channel;
        protected boolean isGame, isDiscord;

        public Message(String str) {
            this("[BOT]", str);
        }

        public Message(String s, String str) {
            prefix = s;
            string = str;
        }

        public static DSBotAPI.Message discordMessage(String s, String str) {
            return new DSBotAPI.Message(s, str).setDiscord(true).setChannel(DSBotAPI.Channel.BOT_CHANNEL);
        }

        public String getPrefix() {
            return prefix;
        }

        public String getString() {
            return string;
        }

        public DSBotAPI.Message setPrefix(String string) {
            prefix = string;
            return this;
        }

        public boolean isGame() {
            return isGame;
        }

        public DSBotAPI.Message setGame(boolean b) {
            isGame = b;
            return this;
        }

        public boolean isDiscord() {
            return isDiscord;
        }

        public DSBotAPI.Message setDiscord(boolean b) {
            isDiscord = b;
            return this;
        }

        public String getChannel() {
            return channel;
        }

        public DSBotAPI.Message setChannel(String string) {
            channel = string;
            return this;
        }

        public String toString() {
            return String.format("%s %s", prefix, string);
        }
    }

    public static class Channel {
        protected String name, command, channel, permission;
        protected boolean chatColor;

        public static final String BOT_CHANNEL = DSConfiguration.instance().getBotChannel();

        public String getName() {
            return name;
        }

        public DSBotAPI.Channel setName(String string) {
            name = string;
            return this;
        }

        public String getCommand() {
            return command;
        }

        public DSBotAPI.Channel setCommand(String string) {
            command = string;
            return this;
        }

        public String getChannel() {
            return channel;
        }

        public DSBotAPI.Channel setChannel(String string) {
            channel = string;
            return this;
        }

        public boolean isChatColor() {
            return chatColor;
        }

        public DSBotAPI.Channel setChatColor(boolean b) {
            chatColor = b;
            return this;
        }

        public String getPermission() {
            return permission;
        }

        public DSBotAPI.Channel setPermission(String string) {
            permission = string;
            return this;
        }
    }

    public static abstract class Command {
        protected DSBotManager botManager;

        protected String label;
        protected Aliases aliases;

        public Command(@Nonnull String string) {
            this(string, new String[0]);
        }

        public Command(@Nonnull String string, @Nonnull String... strings) {
            botManager = DSBotManager.instance();

            label = string.toLowerCase();
            aliases = new Aliases(strings);
        }

        public abstract boolean onReceive(User user, TextChannel textChannel, String... args);

        public String getLabel() {
            return label;
        }

        public static class Aliases {
            protected String[] aliases;

            public Aliases() {
                this(new String[0]);
            }

            public Aliases(@Nonnull String[] strings) {
                aliases = strings;
            }

            public String[] getAliases() {
                return aliases;
            }

            public boolean equals(String string) {
                for(int i = 0; i < aliases.length; ++i) {
                    String s = aliases[i];
                    if(s == null || s.isEmpty()) continue;
                    if(s.equalsIgnoreCase(string)) return true;
                }

                return false;
            }

            @Override
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < aliases.length; ++i) {
                    stringBuilder.append(aliases[i]);
                    if(i + 1 < aliases.length) stringBuilder.append(", ");
                }

                if(stringBuilder.length() == 0) stringBuilder.append("[]");
                return stringBuilder.toString();
            }
        }
    }

}
