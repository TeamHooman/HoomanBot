package com.ryonday.automation.twitch.config;


import com.google.common.base.Splitter;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@org.springframework.context.annotation.Configuration
public class TwitchConfig {

    private static final Logger logger = LoggerFactory.getLogger(TwitchConfig.class);

    @Bean(name = "twitchIRCConfig")
    public Configuration twitchIRCConfig( @Value("${twitch.host}") String host,
                                          @Value("${twitch.port}") int port,
                                          @Value("${twitch.nick}") String nick,
                                          @Value("${twitch.oauth}") String oAuth,
                                          @Value("${twitch.autojoin}") String autoJoin,
                                          List<Listener> listeners) {

        checkArgument(!isNullOrEmpty(host));
        checkArgument(port > 0 );
        checkArgument( !isNullOrEmpty( nick ) );
        checkArgument( !isNullOrEmpty( oAuth ));
        checkArgument( !isNullOrEmpty( autoJoin ));
        checkArgument( listeners != null && listeners.size() > 0 );

        // Some config items from https://github.com/TheLQ/pircbotx/wiki/Twitch.tv-support
        // such as the CAP handlers and the autoNickChange/JoinOnWhoEnabled settings.

        Configuration config = new Configuration.Builder()
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .setName(nick)
                .setAutoNickChange(false)
                .setOnJoinWhoEnabled(false)
                .setAutoReconnect(true)
                .addAutoJoinChannels(Splitter.on(',').trimResults().omitEmptyStrings().split(autoJoin))
                .setServerPassword(oAuth)
                .addListeners( listeners )
                .buildForServer(host, port);


        logger.info( "Constructed Twitch IRC Configuration: {}", config );

        return config;
    }

    @Bean( name="bot")
    public PircBotX bot( Configuration config ) {
        PircBotX bot = new PircBotX( config );
        return bot;
    }
}
