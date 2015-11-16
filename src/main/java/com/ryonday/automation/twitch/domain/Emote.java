package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "emotes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"emote"})})
public class Emote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "emote", nullable = false)
    private String emote;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emote")
    private List<ChatEmote> chatEmotes;

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    protected Emote setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getEmote() {
        return emote;
    }

    public Emote setEmote(String emote) {
        this.emote = emote;
        return this;
    }

    public List<ChatEmote> getChatEmotes() {
        return chatEmotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emote)) return false;
        Emote emote1 = (Emote) o;
        return Objects.equals(id, emote1.id) &&
                Objects.equals(emote, emote1.emote) &&
                Objects.equals(chatEmotes, emote1.chatEmotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, emote, chatEmotes);
    }
}
