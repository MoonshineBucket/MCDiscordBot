package com.github.dreamsmoke.discord.bot.commands.bot;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BroadcastCommand extends DSBotAPI.Command {

    public BroadcastCommand() {
        super("broadcast", "bc");
    }

    @Override
    public boolean onReceive(User user, TextChannel textChannel, String... args) {
        if(args.length == 0) {
            botManager.sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[INFO]",
                    "?broadcast <string>: Оповестить игроков на сервере."), true);
            return true;
        }

        if(args.length >= 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < args.length; ++i) {
                stringBuilder.append(args[i]);
                if(i + 1 < args.length) stringBuilder.append(" ");
            }

            String string = stringBuilder.toString();
            botManager.sendMessageDiscord(textChannel, DSBotAPI.Message
                    .discordMessage("[INFO]", String.format("Оповещение: %s отправлено!", string)), true);
            botManager.broadcast(string);
            return true;
        }

        return false;
    }

}
