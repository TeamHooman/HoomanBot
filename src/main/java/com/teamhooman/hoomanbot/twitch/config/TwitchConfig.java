package com.teamhooman.hoomanbot.twitch.config;


import com.google.common.base.Splitter;
import org.hibernate.validator.constraints.NotBlank;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@ConfigurationProperties(prefix = "twitch")
@org.springframework.context.annotation.Configuration
public class TwitchConfig {

    private static final Logger logger = LoggerFactory.getLogger(TwitchConfig.class);

    @NotBlank(message = "TMI Server must be specified.")
    private String host;

    @NotNull(message = "Server port must be specified.")
    private int port;

    @NotBlank(message = "Must have a nickname.")
    private String nickname;

    @NotBlank(message = "Cannot access server without an OAuth token.")
    private String oAuth;

    @NotBlank(message = "Must join at least one channel.")
    private String autoJoin;

    @NotNull(message = "Must have a valid Twitch Emote URL.")
    private URL emoteUrl;

    public String getAutoJoin() {
        return autoJoin;
    }

    public TwitchConfig setAutoJoin(String autoJoin) {
        this.autoJoin = autoJoin;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TwitchConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public TwitchConfig setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getoAuth() {
        return oAuth;
    }

    public TwitchConfig setoAuth(String oAuth) {
        this.oAuth = oAuth;
        return this;
    }

    public int getPort() {
        return port;
    }

    public TwitchConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public URL getEmoteUrl() {
        return emoteUrl;
    }

    public TwitchConfig setEmoteUrl(URL emoteURL) {
        this.emoteUrl = emoteURL;
        return this;
    }

    @Bean(name = "twitchIRCConfig")
    public Configuration.Builder twitchIRCConfig(List<Listener> listeners) {
        checkArgument(!isNullOrEmpty(host));
        checkArgument(port > 0);
        checkArgument(!isNullOrEmpty(nickname));
        checkArgument(!isNullOrEmpty(oAuth));
        checkArgument(!isNullOrEmpty(autoJoin));
        checkArgument(listeners != null && listeners.size() > 0);

        logger.info("Listeners: {}", listeners);
        // Some config items from https://github.com/TheLQ/pircbotx/wiki/Twitch.tv-support
        // such as the CAP handlers and the autoNickChange/JoinOnWhoEnabled settings.

        Configuration.Builder config = new Configuration.Builder()
            .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
            .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
            .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
            .setName(nickname)
            .setAutoNickChange(false)
            .setOnJoinWhoEnabled(false)
            .setAutoReconnect(true)
            .addAutoJoinChannels(
                Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(autoJoin))
            .setServerPassword(oAuth)
            .addListeners(listeners);

        logger.info("Constructed Twitch IRC Configuration: {}", config);

        return config;
    }

    @Bean(name = "twitchBot")
    public PircBotX twitchBot(Configuration.Builder config) {
        PircBotX bot = new PircBotX(config.buildForServer(host, port));
        return bot;
    }
}
