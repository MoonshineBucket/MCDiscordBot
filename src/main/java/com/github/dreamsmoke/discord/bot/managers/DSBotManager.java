package com.github.dreamsmoke.discord.bot.managers;

import com.github.dreamsmoke.discord.bot.DSBot;
import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class DSBotManager {

    protected static DSBotManager instance;

    protected List<String> utfTranslate;
    protected Server server;

    public DSBotManager() {
        utfTranslate = new ArrayList<>(Arrays.asList("а", "a", "б", "b", "в", "v", "г", "g", "д", "d",
                "е", "e", "ё", "e", "ж", "j", "з", "z", "и", "i", "й", "i", "к", "k", "л", "l", "м", "m", "н", "n",
                "о", "o", "п", "p", "р", "r", "с", "s", "т", "t", "у", "u", "ф", "f", "х", "h", "ц", "c", "ч",
                "ch", "ш", "sh", "щ", "sh", "ъ", "'", "ы", "i", "ь", "'", "э", "e", "ю", "y", "я", "ya"));
    }

    public static DSBotManager instance() {
        if(instance == null) instance = new DSBotManager();
        return instance;
    }

    public JDA getJDA() {
        return DSBot.instance().getJDA();
    }

    public Server getServer() {
        if(server == null) server = Bukkit.getServer();
        return server;
    }

    public List<TextChannel> getTextChannel(String channel) {
        TextChannel textChannel = getJDA().getTextChannelById(channel);
        if(textChannel == null) return new ArrayList<>(getJDA().getTextChannelsByName(channel
                .toLowerCase(), true));
        return new ArrayList<>(Arrays.asList(textChannel));
    }

    public String getSender(String string) {
        return string.replace("Server", "Администратор");
    }

    public String formatName(String string, boolean ignoreCase) {
        if(ignoreCase) string = string.toLowerCase();
        for(int i = 0; i < utfTranslate.size(); i += 2) {
            if(string.contains(utfTranslate.get(i))) string = string.replace(utfTranslate
                    .get(i), utfTranslate.get(i + 1));
        }

        return string;
    }

    public void clearChannel(String channel) {
        for(TextChannel textChannel : getTextChannel(channel)) {
            for(Message message : textChannel.getIterableHistory()) {
                message.delete().queue();
            }

            sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[BOT]",
                    "Очередь очистки сообщений построена, ожидайте!"), true);
        }
    }

    public void sendChannel(String type, String sender, String channel, String string) {
        if(type.equals("message")) DSBotManager.instance()
                .sendMessage(DSBotAPI.Message.discordMessage("[INFO]",
                        String.format("%s сказал: %s", getSender(sender), string)).setChannel(channel));
        else if(type.equals("file")) DSBotManager.instance().sendFile(MinecraftServer.getServer()
                .getFile(string), channel);
    }

    public void sendFile(File file, String channel) {
        try {
            if(file != null && file.exists()) {
                sendMessage(DSBotAPI.Message.discordMessage("[INFO]",
                        String.format("Загрузка файла: %s добавлена в очередь!", file.getName())));
                sendFile(new FileInputStream(file), formatName(file.getName(),
                        true), channel);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void sendFile(FileInputStream fis, String fileName, String channel) {
        for(TextChannel textChannel : getTextChannel(channel)) {
            textChannel.sendFile(fis, fileName, AttachmentOption.SPOILER).queue();
        }
    }

    public void sendMessage(String string, String channel, Object... objects) {
        sendMessage(new DSBotAPI.Message(String.format(string, objects)).setChannel(channel));
    }

    public void sendMessage(DSBotAPI.Message message) {
        new Thread(() -> {
            if(message.isDiscord()) sendMessageDiscord(message);
            else if(message.isGame()) broadcast(message.toString());
        }).start();
    }

    public void sendMessageDiscord(DSBotAPI.Message message) {
        String string = message.getChannel();
        if(string == null || string.trim().equals("")) return;

        for(TextChannel textChannel : getTextChannel(string)) {
            sendMessageDiscord(textChannel, message, true);
        }
    }

    public void sendMessageDiscord(TextChannel textChannel, DSBotAPI.Message message, boolean queue) {
        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        for(String s : message.getString().split("\n")) {
            MessageAction messageAction = textChannel.sendMessage(String.format("[%s] %s %s",
                    time, message.getPrefix(), s));
            if(queue) messageAction.queue();
        }
    }

    public void broadcast(@Nonnull String string) {
        server.broadcastMessage(translateAlternateColorCodes('&', string));
    }

}
