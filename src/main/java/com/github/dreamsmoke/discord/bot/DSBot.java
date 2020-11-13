package com.github.dreamsmoke.discord.bot;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import com.github.dreamsmoke.discord.bot.commands.DiscordCommand;
import com.github.dreamsmoke.discord.bot.commands.bot.BroadcastCommand;
import com.github.dreamsmoke.discord.bot.commands.bot.HelpCommand;
import com.github.dreamsmoke.discord.bot.commands.bot.RoleCommand;
import com.github.dreamsmoke.discord.bot.commands.bot.StopCommand;
import com.github.dreamsmoke.discord.bot.configuration.DSConfiguration;
import com.github.dreamsmoke.discord.bot.listeners.DSListener;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.Compression;
import net.minecraft.command.ServerCommandManager;

@Mod(modid = "DiscordBot", name = "DiscordBot", version = "1.6.4")
public class DSBot {

    protected static DSBot instance;

    protected DSConfiguration configuration;
    protected JDA jda;

    @Mod.EventHandler
    public void invoke(FMLInitializationEvent e) {
        if(e.getSide() == Side.SERVER) {
            instance = this;

            configuration = DSConfiguration.instance();
            configuration.init();

            register();
        }
    }

    @Mod.EventHandler
    public void invoke(FMLServerStartingEvent e) {
        ServerCommandManager commandManager = (ServerCommandManager) e.getServer().getCommandManager();
        commandManager.registerCommand(new DiscordCommand());
    }

    public static DSBot instance() {
        return instance;
    }

    public DSConfiguration getConfiguration() {
        return configuration;
    }

    public JDA getJDA() {
        return jda;
    }

    public void register() {
        if(configuration.isEnable()) {
            try {
                jda = JDABuilder.createDefault(configuration.getToken())
                        .setBulkDeleteSplittingEnabled(false).setCompression(Compression.NONE)
                        .addEventListeners(new DSListener()).build();
            } catch (Throwable throwable) {
                FMLLog.warning("Ошибка загрузки модификации DiscordBot: %s.", throwable.getMessage());
            }

            if(configuration.isEnableCommands()) DSBotAPI.registerCommands(new HelpCommand(),
                    new RoleCommand(), new BroadcastCommand(), new StopCommand());
        }
    }

}
