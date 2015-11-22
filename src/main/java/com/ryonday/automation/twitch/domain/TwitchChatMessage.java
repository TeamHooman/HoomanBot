package com.ryonday.automation.twitch.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "twitch_chat_messages")
public class TwitchChatMessage implements Comparable<TwitchChatMessage> {

    private final static Logger logger = LoggerFactory.getLogger( TwitchChatMessage.class );

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "nickColor", nullable = false, updatable = false)
    private Color nickColor;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "nickname_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Nickname nickname;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id", referencedColumnName = "id", nullable = false, updatable = false)
    private TwitchChannel channel;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<EmoteInChat> chatEmotes = Sets.newHashSet();

    @Column(name = "text", nullable = false, updatable = false)
    private String text;

    public Long getId() {
        return id;
    }

    public TwitchChatMessage setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected TwitchChatMessage setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getText() {
        return text;
    }

    public TwitchChatMessage setText(String text) {
        this.text = text;
        return this;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchChatMessage setChannel(TwitchChannel channel) {
        this.channel = channel;
        return this;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public TwitchChatMessage setNickname(Nickname nickname) {
        this.nickname = nickname;
        return this;
    }

    public Color getNickColor() {
        return nickColor;
    }

    public TwitchChatMessage setNickColor(Color nickColor) {
        this.nickColor = nickColor;
        return this;
    }

    public Set<EmoteInChat> getChatEmotes() {
        return ImmutableSortedSet.copyOf(chatEmotes);
    }

    public TwitchChatMessage setChatEmotes( Collection<EmoteInChat> chatEmotes ) {
        this.chatEmotes.clear();
        this.addChatEmotes( chatEmotes );
        return this;
    }


    public TwitchChatMessage addChatEmotes(Collection<EmoteInChat> chatEmotes) {
        if (chatEmotes != null) {
            chatEmotes.forEach(this::addChatEmote);
        }
        return this;
    }

    public TwitchChatMessage addChatEmote(EmoteInChat emoteInChat) {
        if (emoteInChat != null && !chatEmotes.contains(emoteInChat)) {
            this.chatEmotes.add(emoteInChat);
            emoteInChat.setChat(this);
        }
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TwitchChatMessage setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitchChatMessage)) return false;
        TwitchChatMessage chat = (TwitchChatMessage) o;
        return Objects.equals(timestamp, chat.timestamp) &&
            Objects.equals(nickColor, chat.nickColor) &&
            Objects.equals(nickname, chat.nickname) &&
            Objects.equals(channel, chat.channel) &&
            Objects.equals(text, chat.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, nickColor, nickname, channel, text);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(TwitchChatMessage.class)
            .add("channel", channel)
            .add("id", id)
            .add("version", version)
            .add("timestamp", timestamp)
            .add("nickColor", nickColor)
            .add("nickname", nickname)
            .add("chatEmotes", chatEmotes)
            .add("text", text)
            .toString();
    }

    @Override
    public int compareTo(TwitchChatMessage o) {
        return channelAndTimestamp.compare(this, o);
    }

    public final static Comparator<TwitchChatMessage> channelAndTimestamp = Comparator.comparing(TwitchChatMessage::getChannel).thenComparing(TwitchChatMessage::getTimestamp);
    public final static Comparator<TwitchChatMessage> nicknameAndTimestamp = Comparator.comparing(TwitchChatMessage::getNickname).thenComparing(TwitchChatMessage::getTimestamp);
}
