package com.ryonday.automation.twitch.handler;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TwitchMessageSQLHandler extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TwitchMessageSQLHandler.class);

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        logger.info("Received PrivateMessageEvent: message: {}", event);
        event.respond("HELLO HOOMAN");
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        logger.info("Received message: {}", event);
        if( event.getMessage().startsWith( "HoomanBot")) {
            event.respond("HELLO HOOMAN");
        }

    }
}
