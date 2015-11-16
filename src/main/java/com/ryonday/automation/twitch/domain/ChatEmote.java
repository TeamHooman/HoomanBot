package com.ryonday.automation.twitch.domain;

import javax.persistence.*;

@Entity
@Table(name = "chatEmotes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"chat_id", "startIndex"}),
                @UniqueConstraint(columnNames = {"chat_id", "endIndex"})})
public class ChatEmote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", nullable = false, updatable = false)
    private Chat chat;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "emote_id")
    private Emote emote;

    @Column(name = "startIndex", nullable = false)
    private Long startIndex;

    @Column(name = "endIndex", nullable = false)
    private Long endIndex;

    public Long getId() {
        return id;
    }

    public ChatEmote setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected ChatEmote setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Chat getChat() {
        return chat;
    }

    public Emote getEmote() {
        return emote;
    }

    public ChatEmote setEmote(Emote emote) {
        this.emote = emote;
        return this;
    }

    public Long getEndIndex() {
        return endIndex;
    }

    public ChatEmote setEndIndex(Long endIndex) {
        this.endIndex = endIndex;
        return this;
    }


    public Long getStartIndex() {
        return startIndex;
    }

    public ChatEmote setStartIndex(Long startIndex) {
        this.startIndex = startIndex;
        return this;
    }
}