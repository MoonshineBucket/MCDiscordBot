package com.github.dreamsmoke.discord.bot.configuration;

import cpw.mods.fml.common.FMLLog;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DSConfiguration {

    protected static DSConfiguration instance;

    protected String split = "getSettings";
    protected FileConfiguration configuration;
    protected File file;

    protected String token, commandSplit, botChannel;
    protected boolean enable, enableCommands;

    public DSConfiguration() {
        File dataFolder = new File(new File("./").getParentFile(), "config");
        file = new File(dataFolder, "DSBot.yml");
    }

    public static DSConfiguration instance() {
        if(instance == null) instance = new DSConfiguration();
        return instance;
    }

    public String getPath(@Nonnull String string) {
        return String.format("%s.%s", split, string);
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public File getFile() {
        return file;
    }

    public String getToken() {
        return token;
    }

    public String getCommandSplit() {
        return commandSplit;
    }

    public String getBotChannel() {
        return botChannel;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isEnableCommands() {
        return enableCommands;
    }

    public void init() {
        reloadConfig();
        loadConfig();

        if(configuration == null) return;
        token = configuration.getString(getPath("getToken"), "none");
        enable = token.equals("none") ? false : configuration.getBoolean(getPath("isEnable"), false);
        if(enable) {
            enableCommands = configuration.getBoolean(getPath("isEnableCommands"), false);
            if(enableCommands) commandSplit = configuration.getString(getPath("getSplit"), "?");
            botChannel = configuration.getString(getPath("getChannel"), "none");
        }
    }

    public void reloadConfig() {
        if(file.exists()) FMLLog.info("Обнаружена готовая конфигурация!");
        else {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream(file.getName());
                if(is != null) {
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];

                    while(is.available() > 0) {
                        fos.write(buffer, 0, is.read(buffer));
                    }

                    is.close();
                    fos.close();
                }
            } catch (Throwable throwable) {
                FMLLog.warning("Ошибка загрузки конфигурации: %s.", throwable.getMessage());
            }
        }
    }

    public void loadConfig() {
        try {
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Throwable throwable) {
            FMLLog.warning("Неудачная попытка преобразования файла конфигурации!");
        }
    }

    public void saveConfig() {
        try {
            configuration.save(file);
        } catch (Throwable throwable) {
            FMLLog.warning("Неудачная попытка сохранения файла конфигурации: %s.", throwable.getMessage());
        }
    }

}
