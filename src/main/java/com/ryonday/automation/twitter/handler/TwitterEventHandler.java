package com.ryonday.automation.twitter.handler;

import com.google.common.base.Preconditions;
import com.twitter.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

@Service
public class TwitterEventHandler extends UserStreamAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TwitterEventHandler.class);

    private final static Extractor extractor = new Extractor();

    private final Queue<String> commandQueue;

    private final User currentUser;

    @Autowired
    public TwitterEventHandler( @Value("#{commandQueue}") BlockingQueue<String> commandQueue, User currentUser ) {
        Preconditions.checkNotNull( this.currentUser = currentUser);
        Preconditions.checkNotNull( currentUser.getScreenName());
        Preconditions.checkNotNull( this.commandQueue = commandQueue);
    }

    @Override
    public void onStatus(Status status) {
        logger.info("Received status update: {}", status);

        if( status.getInReplyToUserId() != currentUser.getId()) {
            logger.info("Status update not directed to this user");
            return;
        }

        String reply = status.getText().substring(status.getInReplyToScreenName().length() + 2);

        logger.info("Working with reply '{}'", reply);

        if( !commandQueue.offer( reply )) {
            logger.warn("Could not enqueue message.");
        }
    }

    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        logger.info("Received direct message: {}", directMessage );

        if( directMessage.getRecipientId() != currentUser.getId() || directMessage.getSenderId() == currentUser.getId()) {
            logger.info("Private message sent by or not directed to this user.");
        }
        if( !commandQueue.offer( directMessage.getText() )) {
            logger.warn("Could not enqueue message.");
        }
    }

    @Override
    public void onException(Exception ex) {
        logger.warn("Received Exception: {}", ex);
    }

    @Override
    public void onStallWarning(StallWarning warning) {
        logger.warn("Received Stall warning: {}", warning);
    }
}
