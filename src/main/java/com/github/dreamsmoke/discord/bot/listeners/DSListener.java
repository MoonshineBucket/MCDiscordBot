package com.github.dreamsmoke.discord.bot.listeners;

import com.github.dreamsmoke.discord.bot.DSBot;
import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import com.github.dreamsmoke.discord.bot.configuration.DSConfiguration;
import com.github.dreamsmoke.discord.bot.managers.DSBotManager;
import cpw.mods.fml.common.FMLLog;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DSListener extends ListenerAdapter {

    protected DSBot bot;
    protected DSConfiguration configuration;

    public DSListener() {
        bot = DSBot.instance();
        configuration = bot.getConfiguration();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        if(configuration.isEnable()) FMLLog.info("DiscordBot is ready!");
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        if(configuration.isEnable()) FMLLog.info("DiscordBot is disconnect!");
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(configuration.isEnable() && configuration.isEnableCommands()) {
            String string = event.getMessage().getContentRaw();
            String[] strings = string.split(" ");

            if(strings.length > 0 && DSBotAPI.equalsCommand(strings[0])) {
                DSBotAPI.Command botCommand = DSBotAPI.getCommand(strings[0]);
                if(botCommand == null) return;

                try {
                    List<String> args = new ArrayList<>();
                    if(strings.length > 1) {
                        for(int i = 1; i < strings.length; ++i) {
                            args.add(strings[i]);
                        }
                    }

                    if(botCommand.onReceive(event.getAuthor(),
                            event.getTextChannel(), args.toArray(new String[args.size()])))
                        event.getMessage().delete().queue();
                } catch (Throwable throwable) {
                    DSBotManager.instance().sendMessageDiscord(event.getTextChannel(), DSBotAPI.Message
                            .discordMessage("[ERROR]", DSBotAPI.getException(throwable)), true);
                    event.getMessage().delete().queue();
                }

                FMLLog.info(string);
            }
        }
    }

}
