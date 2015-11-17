package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "nicknames",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname"})})
public class Nickname implements Comparable<Nickname> {

    @Id
//    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nickname", fetch = FetchType.LAZY)
    private List<TwitchChatMessage> chats;

    public Long getId() {
        return id;
    }

    public Nickname setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected Nickname setVersion( Long version ) {
        this.version = version;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public Nickname setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public List<TwitchChatMessage> getChats() {
        return chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nickname)) return false;
        Nickname nickname1 = (Nickname) o;
        return Objects.equals(nickname, nickname1.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    @Override
    public int compareTo(Nickname o) {
        return this.nickname.compareTo(o.nickname);
    }
}
