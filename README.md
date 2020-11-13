DiscordBot for your minecraft server
===
To a greater extent, this development is useful as an example of creating and writing a bot for your server, or using the API I have written.

---
I did not have any particularly interesting ideas for implementation, therefore this bot implements only text channels (it listens to the channels of your Discord server, is able to respond to minecraft commands), for processing commands I made a rather convenient handler, through abstractions, which carries a large potential.

In addition to the above, you can safely supplement the functionality by implementing the JDA API, from a library attached to the repository, which has a huge carriage of actions.

---
Installation
-
- For stable work, you need to have a ready-made minecraft server version 1.6.4, with support for installing modifications (forge)
- Having your own bot in Discord, from which you need to know its ID (token) through which the bot and the modification will connect, as well as the server to which the bot itself will be invited
- Java version 1.8+

Using
-
- You can put the bot on your server if you need a small amount of functionality (listen to the written commands in the Discord channel, listen to the commands written on the minecraft server)
- Supplement this modification with the functions you need
- Use the modification as an API and write your own, with the implementation of some events
- Help me advance development by adding my own functions or just suggesting an interesting idea

---


Let me remind you that this is my first experience with the [Discord API](https://github.com/DV8FromTheWorld/JDA) in java, please do not judge strictly if something is not done as expected =)

---

Thank you for your support, I hope my development interests you at least a little :)
