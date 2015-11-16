package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
public class Chat implements Comparable<Chat> {

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
    private Channel channel;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChatEmote> chatEmotes;

    @Column(name = "text", nullable = false)
    private String text;

    public Long getId() {
        return id;
    }

    public Chat setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected Chat setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getText() {
        return text;
    }

    public Chat setText(String text) {
        this.text = text;
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

    public Chat setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public Color getNickColor() {
        return nickColor;
    }

    public Chat setNickColor(Color nickColor) {
        this.nickColor = nickColor;
        return this;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Chat setNickname(Nickname nickname) {
        this.nickname = nickname;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Chat setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
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
    public int compareTo(Chat o) {
        return channelAndTimestamp.compare( this, o );
    }

    public final static Comparator<Chat> channelAndTimestamp = Comparator.comparing(Chat::getChannel).thenComparing(Chat::getTimestamp);
    public final static Comparator<Chat> nicknameAndTimestamp = Comparator.comparing( Chat::getNickname ).thenComparing( Chat::getTimestamp );
}
