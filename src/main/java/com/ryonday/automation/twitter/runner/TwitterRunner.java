package com.ryonday.automation.twitter.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import twitter4j.TwitterStream;

import static com.google.common.base.Preconditions.checkNotNull;


@Controller
@Profile("twitter")
public class TwitterRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(TwitterRunner.class);

    private final TaskExecutor taskExecutor;
    private final TwitterStream twitterStream;

    @Autowired
    public TwitterRunner(TaskExecutor taskExecutor, TwitterStream twitterStream ) {

        checkNotNull(this.taskExecutor = taskExecutor);
        checkNotNull(this.twitterStream = twitterStream);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskExecutor.execute(twitterStream::user);
    }
}
