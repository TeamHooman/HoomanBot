package com.teamhooman.hoomanbot.rita.config;

import com.google.common.base.Preconditions;
import com.teamhooman.hoomanbot.twitch.domain.TwitchChatMessage;
import com.teamhooman.hoomanbot.twitch.repo.TwitchChatMessageRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rita.RiMarkov;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Profile("rita")
@Component
public class RitaSQLLoader {

    private static final Logger logger = getLogger(RitaSQLLoader.class);

    private final RiMarkov markov;

    private final TwitchChatMessageRepository chatMessageRepo;

    @Autowired
    public RitaSQLLoader(RiMarkov markov, TwitchChatMessageRepository chatMessageRepo) {
        this.markov = Preconditions.checkNotNull(markov, "Markov chain supplier required.");
        this.chatMessageRepo = Preconditions.checkNotNull(chatMessageRepo, "Twitch Chat Message Repository required.");
        logger.info("Constructed Markov Chain SQL Feeder.");
    }

    @EventListener
    public void feedMarkovGeneratorFromSQL(ContextRefreshedEvent event) {
        logger.info("Feeding Markov chain generator...");
        Optional<Iterable<TwitchChatMessage>> messages = chatMessageRepo.findAll();

        if( !messages.isPresent()) {
            logger.warn("No existing chat messages to feed to the markov chain generator.");
            return;
        }

        messages.get().forEach( msg -> {
            logger.debug("Feeding chat message to markov chain generator: {}", msg.getText());
            markov.loadText( msg.getText() );
        });

        logger.info("Completed feeding Markov chain generator; model is now of size {}", markov.size());
    }
}
