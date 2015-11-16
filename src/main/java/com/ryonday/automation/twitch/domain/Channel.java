package com.ryonday.automation.twitch.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "channels", uniqueConstraints = {@UniqueConstraint(columnNames = {"channel"})})
public class Channel implements Comparable<Channel>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "channel", nullable = false)
    private String channel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", fetch = FetchType.LAZY)
    private List<Chat> chats;

    public Long getId() {
        return id;
    }

    public Channel setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    protected Channel setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public Channel setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public List<Chat> getChats() {
        return chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Channel)) return false;
        Channel channel1 = (Channel) o;
        return Objects.equals(channel, channel1.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel);
    }

    @Override
    public int compareTo(Channel o) {
        return this.channel.compareTo( o.channel );
    }
}
