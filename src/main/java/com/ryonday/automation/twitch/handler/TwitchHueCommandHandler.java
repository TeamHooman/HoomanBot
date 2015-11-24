package com.ryonday.automation.twitch.handler;


import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.ryonday.automation.command.HueCommander;
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

    private final static String COMMAND_HUE = "!hue ";
    private final static String COMMAND_OFF = "!off ";
    private final static String COMMAND_ON = "!on ";
    private final static String COMMAND_BRIGHTEN = "!brighter ";
    private final static String COMMAND_DARKEN = "!darker ";
    private final static String COMMAND_SATURATE = "!saturate ";
    private final static String COMMAND_DESATURATE = "!desaturate ";
    private final static String COMMAND_CYCLE = "!desaturate ";

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

        String message = snippy.trimAndCollapseFrom(nullToEmpty(messageEvent.getMessage()), ' ');

        if (!nullToEmpty(message).startsWith(COMMAND_HUE)) {
            logger.info("Not a command: {}", message);
            return;
        }

        List<String> command = splitty.splitToList(message.substring(COMMAND_HUE.length()));

        if (command.size() == 2) { // It's $LIGHT {$COLORNAME, $HEX, $COMMAND}
            try {
                hueCommander.color(
                    command.get(0),
                    Color.web(command.get(1)));
            } catch (Exception ex) {
                logger.error("Error while parsing command: {}, {}", command, ex);
            }

        } else if (command.size() == 4) { // It's $LIGHT $RED $GREEN $BLUE
            hueCommander.color(
                command.get(0),
                Color.rgb(Integer.parseInt(command.get(1)),
                    Integer.parseInt(command.get(2)),
                    Integer.parseInt(command.get(3))
                ));
        } else {
            logger.warn("Unrecognized command format: {}", command);
        }
    }
}
