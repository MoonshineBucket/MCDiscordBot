package com.github.dreamsmoke.discord.bot.commands.bot;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand extends DSBotAPI.Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean onReceive(User user, TextChannel textChannel, String... args) {
        if(args.length == 0) {
            botManager.sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[INFO]",
                    "?help: Просмотр списка возможностей.\n?donate: Выдать привилегию игроку.\n" +
                            "?role: Вывести случайное число.\n?broadcast <string>: Оповестить игроков на сервере.\n" +
                            "?stop: Остановить сервер."), true);
            return true;
        }

        return false;
    }

}
