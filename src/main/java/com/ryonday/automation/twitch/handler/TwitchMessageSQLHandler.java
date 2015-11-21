package com.ryonday.automation.twitch.handler;

import com.google.common.base.Preconditions;
import com.ryonday.automation.twitch.domain.Nickname;
import com.ryonday.automation.twitch.domain.TwitchChannel;
import com.ryonday.automation.twitch.domain.TwitchChatMessage;
import com.ryonday.automation.twitch.repo.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDateTime;

@Component
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

        try {

            TwitchChannel channel = chanRepo
                .findByNameIgnoreCase(chanName)
                .orElseGet(
                    () -> chanRepo.save(
                        new TwitchChannel()
                            .setName(chanName))
                );

            logger.info("channel: {}", channel );
            Nickname nickname = nickRepo
                .findOne(twitchUserId)
                .orElseGet(
                    () -> nickRepo.save(
                        new Nickname()
                            .setId(twitchUserId)
                            .setNickname(messageEvent.getTags().get("display-name").toLowerCase()))
                );

            logger.info("nickname: {}", nickname );
//        Set<EmoteInChat> emotesInChat = EmoteTagParser
//            .parseEmoteTag(emoteString)
//            .stream()
//            .map(tag -> new EmoteInChat()
//                .setStartIndex(tag.startIndex)
//                .setEndIndex(tag.endIndex)
//                .setEmote(emoteRepo
//                    .findOne(tag.id)
//                    .orElseGet(() ->
//                        emoteRepo.save(
//                            new Emote()
//                                .setId(tag.id)
//                                .setEmote(
//                                    text.substring(
//                                        tag.startIndex,
//                                        tag.endIndex))))))
//            .collect(Collectors.toSet());


            chatRepo.save(
                new TwitchChatMessage()
                    .setChannel(channel)
                    .setNickname(nickname)
                    .setText(text)
                    .setTimestamp(LocalDateTime.now())
                    .setNickColor(Color.decode(nickColor)));

        } catch (Exception ex) {
            logger.error("Received exception.", ex);
        }
    }
}
