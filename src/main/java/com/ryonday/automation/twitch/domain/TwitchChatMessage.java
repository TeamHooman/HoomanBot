package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "twitch_chat_messages")
public class TwitchChatMessage implements Comparable<TwitchChatMessage> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "nickColor", nullable = false)
    private Color nickColor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "nickname_id", nullable = false)
    private Nickname nickname;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id", nullable = false)
    private TwitchChannel channel;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<EmoteInChat> chatEmotes;

    @Column(name = "text", nullable = false)
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

    public Color getNickColor() {
        return nickColor;
    }

    public TwitchChatMessage setNickColor(Color nickColor) {
        this.nickColor = nickColor;
        return this;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public TwitchChatMessage setNickname(Nickname nickname) {
        this.nickname = nickname;
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
        return Objects.equals(id, chat.id) &&
                Objects.equals(timestamp, chat.timestamp) &&
                Objects.equals(nickColor, chat.nickColor) &&
                Objects.equals(nickname, chat.nickname) &&
                Objects.equals(channel, chat.channel) &&
                Objects.equals(text, chat.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, nickColor, nickname, channel, text);
    }

    @Override
    public int compareTo(TwitchChatMessage o) {
        return channelAndTimestamp.compare( this, o );
    }

    public final static Comparator<TwitchChatMessage> channelAndTimestamp = Comparator.comparing(TwitchChatMessage::getChannel).thenComparing(TwitchChatMessage::getTimestamp);
    public final static Comparator<TwitchChatMessage> nicknameAndTimestamp = Comparator.comparing( TwitchChatMessage::getNickname ).thenComparing( TwitchChatMessage::getTimestamp );
}
