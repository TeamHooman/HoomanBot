package com.teamhooman.hoomanbot.twitch.domain;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "emotes_in_chat",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"chat_id", "startIndex"}),
        @UniqueConstraint(columnNames = {"chat_id", "endIndex"})})
public class EmoteInChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", nullable = false, updatable = false)
    private TwitchChatMessage chat;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "emote_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Emote emote;

    @Column(name = "startIndex", nullable = false, updatable = false)
    private Integer startIndex;

    @Column(name = "endIndex", nullable = false, updatable = false)
    private Integer endIndex;

    public Long getId() {
        return id;
    }

    public EmoteInChat setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected EmoteInChat setVersion(Long version) {
        this.version = version;
        return this;
    }

    public TwitchChatMessage getChat() {
        return chat;
    }

    public EmoteInChat setChat(TwitchChatMessage chat) {
        if (chat != null) {
            this.chat = chat;
            chat.addChatEmote( this );
        }
        return this;
    }

    public Emote getEmote() {
        return emote;
    }

    public EmoteInChat setEmote(Emote emote) {
        this.emote = emote;
        return this;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public EmoteInChat setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
        return this;
    }


    public Integer getStartIndex() {
        return startIndex;
    }

    public EmoteInChat setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmoteInChat)) return false;
        EmoteInChat that = (EmoteInChat) o;
        return Objects.equals(emote, that.emote) &&
            Objects.equals(startIndex, that.startIndex) &&
            Objects.equals(endIndex, that.endIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emote, startIndex, endIndex);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(EmoteInChat.class)
            .add("id", id)
            .add("version", version)
            .add("chat", chat)
            .add("emote", emote)
            .add("startIndex", startIndex)
            .add("endIndex", endIndex)
            .toString();
    }
}
