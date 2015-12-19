package com.teamhooman.hoomanbot.twitch.handler;


import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.teamhooman.hoomanbot.command.HueCommander;
import javafx.scene.paint.Color;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Will eventually replace this with Parboiled or Antlr4 command parser because this is bullshit code.
 */
@Component
public class TwitchHueCommandHandler extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TwitchHueCommandHandler.class);

    private final static String COMMAND_HUE = "!hue";
    private final static String COMMAND_OFF = "!off";
    private final static String COMMAND_ON = "!on";
    private final static String COMMAND_BRIGHTEN = "!brighten";
    private final static String COMMAND_BRIGHTNESS = "!brightness";
    private final static String COMMAND_DARKEN = "!darken";
    private final static String COMMAND_SATURATE = "!saturate";
    private final static String COMMAND_DESATURATE = "!desaturate";
    private final static String COMMAND_CYCLE = "!cycle";

    private final static CharMatcher snippy = CharMatcher.WHITESPACE;

    private final static Splitter splitty = Splitter.on(' ');

    private final HueCommander hueCommander;

    @Autowired
    public TwitchHueCommandHandler(HueCommander hueCommander) {
        Preconditions.checkNotNull(this.hueCommander = hueCommander);
    }

    @Override
    public void onMessage(MessageEvent messageEvent) throws Exception {
        logger.debug("Received Channel Message: {}", messageEvent);

        String message = snippy.trimAndCollapseFrom(nullToEmpty(messageEvent.getMessage()), ' ').toLowerCase();

        List<String> command = splitty.splitToList(message);

        if( command.size() < 2 ) {
            logger.warn("Invalid command received: {}", message);
        }

        String lightName = command.get( 1 );

        switch (command.get(0)) {
            case COMMAND_HUE:
                if (command.size() == 3) {
                    hueCommander.color(
                        lightName,
                        Color.web(command.get(2)));
                } else if (command.size() == 5) {
                    hueCommander.color(
                        lightName,
                        Color.rgb(Integer.parseInt(command.get(2)),
                            Integer.parseInt(command.get(3)),
                            Integer.parseInt(command.get(4))
                        ));
                } else {
                    logger.warn("Invalid command received: {}", message);
                }
                break;
            case COMMAND_ON:
                hueCommander.on( lightName );
                break;
            case COMMAND_OFF:
                hueCommander.off( lightName );
                break;
            case COMMAND_BRIGHTNESS:
                hueCommander.brightness( lightName,
                    Integer.parseInt(command.get(2)));
                break;
            case COMMAND_BRIGHTEN:
                hueCommander.brighter( lightName );
                break;
            case COMMAND_DARKEN:
                hueCommander.darker(lightName );
                break;
            case COMMAND_SATURATE:
                hueCommander.saturate( lightName );
                break;
            case COMMAND_DESATURATE:
                hueCommander.desaturate( lightName );
                break;
            case COMMAND_CYCLE:
                hueCommander.cycle( lightName);
                break;
            default:
                logger.warn("Invalid command received: {}", message);
                break;
        }
    }
}
