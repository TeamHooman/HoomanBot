package com.teamhooman.hoomanbot.twitch.handler;

import com.google.common.base.Preconditions;
import com.teamhooman.hoomanbot.twitch.domain.*;
import com.teamhooman.hoomanbot.twitch.repo.*;
import com.teamhooman.hoomanbot.twitch.util.EmoteTagParser;
import javafx.scene.paint.Color;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Profile("sql,twitch")
public class TwitchMessageSQLHandler extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TwitchMessageSQLHandler.class);

    private final NicknameRepository nickRepo;
    private final TwitchChatMessageRepository chatRepo;
    private final EmoteRepository emoteRepo;
    private final ChatEmoteRepository chatEmoteRepo;
    private final TwitchChannelRepository chanRepo;

    @Autowired
    public TwitchMessageSQLHandler(TwitchChannelRepository chanRepo,
                                   NicknameRepository nickRepo,
                                   TwitchChatMessageRepository chatRepo,
                                   EmoteRepository emoteRepo,
                                   ChatEmoteRepository chatEmoteRepo) {
        Preconditions.checkNotNull(this.chanRepo = chanRepo);
        Preconditions.checkNotNull(this.nickRepo = nickRepo);
        Preconditions.checkNotNull(this.chatRepo = chatRepo);
        Preconditions.checkNotNull(this.emoteRepo = emoteRepo);
        Preconditions.checkNotNull(this.chatEmoteRepo = chatEmoteRepo);
    }

    @Override
    @Transactional
    public void onMessage(MessageEvent messageEvent) throws Exception {

        logger.debug("Received Channel Message: {}", messageEvent);

        final String chanName = messageEvent.getChannel().getName();
        final Long twitchUserId = Long.parseLong(messageEvent.getTags().get("user-id"));
        final String emoteString = messageEvent.getTags().get("emotes");
        final String nickColor = messageEvent.getTags().get("color");
        final String text = messageEvent.getMessage();
        final String from = messageEvent.getTags().get("display-name");

        if(Objects.equals( from, messageEvent.getBot().getNick())) {
            logger.debug("Will not store my own messages.");
            return;
        }

        try {
            chatRepo.save(
                new TwitchChatMessage()
                    .setTimestamp(LocalDateTime.now())
                    .setNickColor(Color.web(nickColor))
                    .setText(text)
                    .setChannel(chanRepo
                        .findByNameIgnoreCase(chanName)
                        .orElseGet(
                            () -> chanRepo.save(
                                new TwitchChannel()
                                    .setName(chanName)))
                    )
                    .setNickname(nickRepo
                        .findOne(twitchUserId)
                        .orElseGet(
                            () -> nickRepo.save(
                                new Nickname()
                                    .setId(twitchUserId)
                                    .setNickname(from.toLowerCase())))
                    )
                    .addChatEmotes(EmoteTagParser
                        .parseEmoteTag(emoteString)
                        .stream()
                        .map(tag -> new EmoteInChat()
                            .setStartIndex(tag.startIndex)
                            .setEndIndex(tag.endIndex)
                            .setEmote(emoteRepo
                                .findOne(tag.id)
                                .orElseGet(() ->
                                    emoteRepo.save(
                                        new Emote()
                                            .setId(tag.id)
                                            .setEmote(
                                                text.substring(
                                                    tag.startIndex,
                                                    tag.endIndex + 1 /* Bleh */)))))
                        )
                        .collect(Collectors.toSet())));

        } catch (Exception ex) {
            logger.error("Received exception.", ex);
        }
    }
}
