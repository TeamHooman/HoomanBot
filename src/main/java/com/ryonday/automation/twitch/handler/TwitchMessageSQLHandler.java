package com.ryonday.automation.twitch.handler;

import com.google.common.base.Preconditions;
import com.ryonday.automation.twitch.EmoteTag;
import com.ryonday.automation.twitch.domain.Nickname;
import com.ryonday.automation.twitch.domain.TwitchChannel;
import com.ryonday.automation.twitch.domain.TwitchChatMessage;
import com.ryonday.automation.twitch.repo.*;
import com.ryonday.automation.twitch.util.EmoteTagParser;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class TwitchMessageSQLHandler extends ListenerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TwitchMessageSQLHandler.class);

    private final NicknameRepository nickRepo;
    private final TwitchChatMessageRepository chatRepo;
    private final EmoteRepository emoteRepo;
    private final ChatEmoteRepository chatEmoteRepo;
    private final TwitchChannelRepository chanRepo;

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
    public void onMessage(MessageEvent messageEvent) throws Exception {
        logger.debug("Received Channel Message: {}", messageEvent);
        String chanName = messageEvent.getChannel().getName();
        Long twitchUserId = Long.parseLong(messageEvent.getTags().get("user-id"));
        String emoteString = messageEvent.getTags().get("emotes");
        String nickColor = messageEvent.getTags().get("color");
        String text = messageEvent.getMessage();

        Optional<TwitchChannel> maybeChannel = chanRepo.findByName(chanName);
        Optional<Nickname> maybeNick = nickRepo.findOne(twitchUserId);

        TwitchChannel channel = chanRepo
            .findByName(chanName)
            .orElse(
                chanRepo.save(
                    new TwitchChannel()
                        .setName(chanName)
                ));

        Nickname nickname = nickRepo
            .findOne(twitchUserId)
            .orElse(
                nickRepo.save(
                    new Nickname()
                        .setId( twitchUserId )
                        .setNickname(messageEvent.getTags().get("display-name").toLowerCase())
                ));

        Set<EmoteTag> emoteTags = EmoteTagParser.parseEmoteTag( emoteString );

        TwitchChatMessage message = new TwitchChatMessage()
            .setChannel( channel )
            .setText( text )
            .setNickname( nickname )
            .setNickColor(Color.decode(nickColor.substring(1)))
            .setTimestamp( LocalDateTime.now() );

        TwitchC

        if (messageEvent.getMessage().startsWith("HoomanBot")) {
            messageEvent.respond("HELLO HOOMAN");
        }

    }
}
