package com.ryonday.automation.twitch.runner;

import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@Profile("twitch")
public class TwitchRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(TwitchRunner.class);


    private final BlockingQueue<String> commandQueue;
    private final TaskExecutor taskExecutor;
    private final PircBotX twitchBot;


    @Autowired
    public TwitchRunner(@Value("#{commandQueue}") BlockingQueue<String> commandQueue,
                        TaskExecutor taskExecutor,
                        PircBotX twitchBot) {

        checkNotNull(this.commandQueue = commandQueue);
        checkNotNull(this.taskExecutor = taskExecutor);
        checkNotNull(this.twitchBot = twitchBot);
    }

    @Override
    public void run(String... args) throws Exception {
        String command;

       taskExecutor.execute(() -> {
        try {
            twitchBot.startBot();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    });

    }
}
