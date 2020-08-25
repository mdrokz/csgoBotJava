package com.mdrokz.csgoBotJava;

// Java Standard Library
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

// Discord4j Imports
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class App {

    public static final String siteUrl = "https://csgostash.com/";

    public static final String commandMessage = "`!get gets skin data from specific category - example !get p2000 Fire Elemental` \n`!alias shows skin aliases for !get command` \n`!featured gets all the featured skins`";

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello World!");

        File configFile = new File("resources/discord_config.txt");

        Scanner fileReader = new Scanner(configFile);

        String discordToken = fileReader.nextLine();

        fileReader.close();

        String alias = getAlias();

        GatewayDiscordClient client = DiscordClientBuilder.create(discordToken).build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
            User self = event.getSelf();

            System.out.println(String.format("Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
        });

        client.getEventDispatcher().on(MessageCreateEvent.class).map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith("!")).filter(message -> {
                    if (message.getContent().contains("get")) {
                        int length = message.getContent().split(" ").length;

                        if (length < 3 || length < 2) {
                            message.getChannel().subscribe(channel -> {
                                channel.getRestChannel().createMessage("Not enough arguments for !get").subscribe();
                            });
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }).subscribe(message -> {

                    String content = message.getContent();

                    if (content.contains("get")) {
                        String[] splitContent = content.split(" ");

                        String url = App.siteUrl + "weapon/" + Utils.Alias.get(splitContent[1]);

                        String itemContent = splitContent[2];

                        message.getChannel().subscribe(channel -> channel.createMessage("Fetching.....").subscribe());

                        Optional<Scraper> scraper = App.getScraper(url);

                        if (!scraper.isEmpty()) {
                            Scraper s = scraper.get();

                            Item item = s.getIndex().get().get(itemContent);
                            if (item != null) {
                                App.sendItem(message.getChannel(), item, client);
                            } else {
                                message.getChannel()
                                        .subscribe(channel -> channel
                                                .createMessage(String.format("No Skin named %s was found", itemContent))
                                                .block());
                            }
                        } else {
                            message.getChannel()
                                    .subscribe(channel -> channel.createMessage("Error Occurred").subscribe());
                        }

                    } else if (content.contains("alias")) {
                        message.getChannel().subscribe(channel -> channel.createMessage("`" + alias + "`").subscribe());
                    } else if (content.contains("commands")) {
                        message.getChannel()
                                .subscribe(channel -> channel.createMessage(App.commandMessage).subscribe());
                    } else if (content.contains("featured")) {
                        message.getChannel().subscribe(channel -> channel.createMessage("Fetching.....").subscribe());

                        Optional<Scraper> scraper = App.getScraper(App.siteUrl);

                        if (!scraper.isEmpty()) {
                            Scraper s = scraper.get();

                            Map<String, Item> items = s.getIndex().get();

                            Object[] itemKeys = items.keySet().toArray();

                            for (Object itemKey : itemKeys) {
                                Item item = items.get(itemKey);
                                if (item != null) {
                                    App.sendItem(message.getChannel(), item, client);
                                } else {
                                    message.getChannel().subscribe(channel -> channel
                                            .createMessage(String.format("`Sorry couldnt get skin %s`", itemKey)));
                                }
                            }

                        }
                    }

                });

        client.onDisconnect().block();
    }

    public static void sendItem(Mono<MessageChannel> messageChannel, Item item, GatewayDiscordClient client) {
        String description = "[Steam Listings]( " + item.steamInfo.listings + ")";
        int[] rgb = getRGB(255, 255, 255);
        User user = client.getSelf().block();

        messageChannel.subscribe(channel -> {
            channel.createEmbed(embed -> {
                embed.setTitle("CSGO Skins");

                embed.addField("Skin Name", item.name, true);
                embed.addField("Quality", item.quality, true);
                embed.addField("Price", item.price.price, true);
                embed.addField("Stat Trak Price", item.price.factoryPrice, true);

                embed.setDescription(description);

                embed.setColor(Color.of(rgb[0], rgb[1], rgb[2]));

                embed.setTimestamp(Instant.now());

                embed.setImage(item.src);

                embed.setFooter(user.getUsername(), user.getAvatarUrl());

            }).subscribe();
        });
    }

    public static String getAlias() {
        Object[] keys = Utils.Alias.keySet().toArray();
        String alias = "";

        for (Object key : keys) {
            alias = alias + key + "\n";
        }

        return alias;
    }

    public static int[] getRGB(int r, int g, int b) {
        int[] rgb = { 0, 0, 0 };

        rgb[0] = (int) Math.floor(Math.random() * r);
        rgb[1] = (int) Math.floor(Math.random() * g);
        rgb[2] = (int) Math.floor(Math.random() * b);

        return rgb;
    }

    public static Optional<Scraper> getScraper(String url) {
        Optional<Scraper> scraper = Optional.ofNullable(null);

        try {
            scraper = Optional.of(new Scraper(url));
        } catch (IOException e) {
            System.out.println(String.format("Failed to Get Document Error: %s", e.getMessage()));
        }

        return scraper;
    }
}
