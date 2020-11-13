package com.github.dreamsmoke.discord.bot.commands.bot;

import com.github.dreamsmoke.discord.bot.api.DSBotAPI;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.util.NumberConversions;

import java.util.Random;

public class RoleCommand extends DSBotAPI.Command {

    public RoleCommand() {
        super("role");
    }

    @Override
    public boolean onReceive(User user, TextChannel textChannel, String... args) {
        if(args.length == 0) {
            botManager.sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[INFO]",
                    "?role <number/min-max>: Случайное число."), true);
            return true;
        }

        if(args.length == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();

            try {
                int number = NumberConversions.toInt(args[0]);
                stringBuilder.append(random.nextInt(number))
                        .append(String.format(", от числа: %s.", number));
            } catch (Throwable e) {
                String[] strings = args[0].trim().split("-");
                if(strings.length == 2) {
                    int min = NumberConversions.toInt(strings[0]),
                            max = Math.min(NumberConversions.toInt(strings[1]), Integer.MAX_VALUE);
                    stringBuilder.append(min + random.nextInt(max - min)).append(String
                            .format(", от массива чисел: %s-%s.", min, max));
                }
            }

            if(stringBuilder.length() == 0) {
                botManager.sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[INFO]",
                        "Произошла ошибка трассировки."), true);
                return true;
            }

            botManager.sendMessageDiscord(textChannel, DSBotAPI.Message.discordMessage("[INFO]", String
                    .format("Получено число %s", stringBuilder.toString())), true);
            return true;
        }

        return false;
    }

}
