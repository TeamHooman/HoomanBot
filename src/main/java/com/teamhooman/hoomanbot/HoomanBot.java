package com.teamhooman.hoomanbot;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import twitter4j.TwitterException;

@EntityScan(basePackageClasses = {HoomanBot.class, Jsr310JpaConverters.class})
@EnableCaching
@SpringBootApplication
public class HoomanBot {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(HoomanBot.class);

    public static void main(String[] args) throws TwitterException {
        logger.info("Starting up Twitter Hue Controller...");

        new SpringApplicationBuilder()
            .logStartupInfo(true)
            .sources(HoomanBot.class)
            .web(true)
            .build()
            .run(args);

//        SpringApplication.run(TwitterHueApplication.class);
    }


}
