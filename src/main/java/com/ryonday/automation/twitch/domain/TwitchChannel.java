package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "twitch_channels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class TwitchChannel implements Comparable<TwitchChannel>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "name", fetch = FetchType.LAZY)
    private List<TwitchChatMessage> chats;

    public Long getId() {
        return id;
    }

    public TwitchChannel setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected TwitchChannel setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getName() {
        return name;
    }

    public TwitchChannel setName(String name) {
        this.name = name;
        return this;
    }

    public List<TwitchChatMessage> getChats() {
        return chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwitchChannel)) return false;
        TwitchChannel channel1 = (TwitchChannel) o;
        return Objects.equals(name, channel1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(TwitchChannel o) {
        return this.name.compareTo( o.name);
    }
}
