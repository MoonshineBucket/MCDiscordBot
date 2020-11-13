package com.github.dreamsmoke.discord.bot.commands;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class DiscordCommand extends Command {

    public DiscordCommand() {
        super("core.discord.commands.discord");
    }

    @Override
    protected void processCommand(ICommandSender commandSender, List<String> list) {
        if(list.size() == 0 || list.size() >= 1 && list.get(0).equals("help")) {
            sendMessage(commandSender, "&9/discord help&7: Просмотр списка возможностей.");
            sendMessage(commandSender, "&9/discord reload&7: Перезагрузка модификации (Регистрация команд, обновление конфигурации).");
            sendMessage(commandSender, "&9/discord send <message/file> <channel> <string>&7: Отправить сообщение/файл в Discord канал.");
            sendMessage(commandSender, "&9/discord clear <channel>&7: Запустить процесс очистки сообщений канала (5 сообщений/сек).");
            return;
        }

        if(list.size() >= 1) {
            if(list.get(0).equals("reload")) {
                configuration.reloadConfig();
                configuration.loadConfig();
                configuration.saveConfig();

                bot.register();

                sendMessage(commandSender, "&7Модификация перезагружена!");
                return;
            } else if(list.get(0).equals("send")) {
                if(list.size() < 4) sendMessage(commandSender, "&9/discord send <message/file> <string>&7:" +
                        " Отправить сообщение/файл в Discord канал.");
                else {
                    botManager.sendChannel(list.get(1),
                            commandSender.getCommandSenderName(), list.get(2), list.get(3));
                    sendMessage(commandSender, String.format("&7Отправляем %s: %s в Discord канал.",
                            (list.get(1).equals("message") ? "сообщение" : "файл"), list.get(3)));
                }
            } else if(list.get(0).equals("clear")) {
                String channel = DSBotAPI.Channel.BOT_CHANNEL;
                if(list.size() >= 2) channel = list.get(1);

                botManager.clearChannel(channel);
                sendMessage(commandSender, String.format("&7Запущен процесс очистки истории сообщений канала!"));
            }
        }
    }

    @Override
    public String getCommandName() {
        return "discord";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "Использование: /discord help: Просмотр списка возможностей.";
    }

}
