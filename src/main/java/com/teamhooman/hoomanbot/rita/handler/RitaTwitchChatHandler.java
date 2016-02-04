package com.teamhooman.hoomanbot.rita.handler;

import com.google.common.base.Joiner;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rita.RiMarkov;

import java.security.SecureRandom;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Profile("rita")
public class RitaTwitchChatHandler extends ListenerAdapter {

    private static final Logger logger = getLogger(RitaTwitchChatHandler.class);

    private static final SecureRandom rando = new SecureRandom();

    private static final Joiner joiner = Joiner.on(". ").skipNulls();

    private final RiMarkov markov;

    @Autowired
    public RitaTwitchChatHandler(RiMarkov markov) {
        this.markov = checkNotNull(markov);
    }

    @Override
    public void onMessage(MessageEvent messageEvent) {
        logger.debug("Received Channel Message: {}", messageEvent);

        String message = messageEvent.getMessage();
        String nickname = messageEvent.getTags().get("display-name");

        if (Objects.equals(nickname, messageEvent.getBot().getNick())) {
            logger.debug("Will not take action on my own messages.");
            return;
        }

        if( !message.matches(".+[.?!]")) {
            logger.warn("auto-completing sentence.");
            message += '.';
        }

        markov.loadText(message);

        if (message.startsWith("!speak") && markov.ready()) {
            String generated = joiner.join(markov.generateSentences(rando.nextInt(2) + 1));
            logger.info("Responding with generated sentence(s): {}", generated);
            messageEvent.respond(generated);
        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        super.onPrivateMessage(event);
    }
}
