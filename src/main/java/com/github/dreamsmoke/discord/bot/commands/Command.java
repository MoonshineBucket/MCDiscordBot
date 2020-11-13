package com.github.dreamsmoke.discord.bot.commands;

import com.github.dreamsmoke.discord.bot.DSBot;
import com.github.dreamsmoke.discord.bot.configuration.DSConfiguration;
import com.github.dreamsmoke.discord.bot.managers.DSBotManager;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public abstract class Command extends CommandBase {

    protected DSBot bot;
    protected DSConfiguration configuration;
    protected DSBotManager botManager;

    protected String permission;

    public Command(String string) {
        bot = DSBot.instance();
        configuration = bot.getConfiguration();
        botManager = DSBotManager.instance();

        permission = string;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        if(commandSender instanceof EntityPlayer) return botManager.getServer()
                .getPlayer(commandSender.getCommandSenderName()).hasPermission(permission);
        return super.canCommandSenderUseCommand(commandSender);
    }

    @Override
    public final void processCommand(ICommandSender commandSender, String... strings) {
        processCommand(commandSender, new ArrayList<>(Arrays.asList(strings)));
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo((ICommand) o);
    }

    protected abstract void processCommand(ICommandSender commandSender, List<String> list);

    public void sendMessage(ICommandSender commandSender, String string) {
        string = translateAlternateColorCodes('&', string);
        if(commandSender instanceof EntityPlayer) ((EntityPlayer) commandSender).addChatMessage(string);
        else FMLLog.info(stripColor(string));
    }

}
