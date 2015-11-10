package com.ryonday.automation.twitter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.*;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

@Configuration
public class TwitterConfig {

    Logger logger = LoggerFactory.getLogger( TwitterConfig.class );

    @Bean
    public Twitter twitter() {
        Twitter twitter = TwitterFactory.getSingleton();
        logger.info("Returning Twitter interface: {}", twitter);
        return twitter;
    }

    @Bean
    public User currentUser(Twitter twitter) throws TwitterException {
        checkArgument( twitter != null, "Null Twitter interface received." );

        User user =  twitter.verifyCredentials();

        logger.info("Returning currently logged-in user: {}", user);

        return user;
    }

    @Bean
    public TwitterStream twitterStream( Collection<UserStreamAdapter> listeners ) {
        checkArgument( listeners != null, "Null event listener collection received." );
        checkArgument(listeners.size() > 0, "Empty event listener collection received.");

        TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

        listeners.stream().forEach(
                x -> {
                    logger.info( "Adding event listener '{}' to Stream.", x.getClass().getName() );
                    twitterStream.addListener( x );
                } );

        logger.info( "Returning TwitterStream: {}", twitterStream );
        return twitterStream;
    }
}
