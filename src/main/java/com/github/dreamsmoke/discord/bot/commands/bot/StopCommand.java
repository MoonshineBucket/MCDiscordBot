package com.github.dreamsmoke.discord.bot.commands.bot;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.server.MinecraftServer;

public class StopCommand extends DSBotAPI.Command {

    public StopCommand() {
        super("stop");
    }

    @Override
    public boolean onReceive(User user, TextChannel textChannel, String... args) {
        if(args.length == 0) {
            botManager.sendMessageDiscord(textChannel,
                    DSBotAPI.Message.discordMessage("[INFO]", "Сервер выключается!"), true);
            MinecraftServer.getServer().initiateShutdown();
            return true;
        }

        return false;
    }

}
