package com.teamhooman.hoomanbot.twitch.domain;

import com.beust.jcommander.internal.Sets;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSortedSet;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "twitch_channels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class        TwitchChannel implements Comparable<TwitchChannel>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, mappedBy = "channel", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<TwitchChatMessage> chats = Sets.newHashSet();

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

    public Set<TwitchChatMessage> getChats() {
        return ImmutableSortedSet.copyOf(chats);
    }

    public TwitchChannel addChats( Collection<TwitchChatMessage> chats) {
        if( chats != null ) {
            chats.forEach(this::addChat);
        }
        return this;
    }

    public TwitchChannel addChat( TwitchChatMessage chat ) {
        if( chat != null && !chats.contains( chat )) {
            chats.add( chat );
            chat.setChannel( this );
        }
        return this;
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
    public String toString() {
        return MoreObjects.toStringHelper(TwitchChannel.class)
            .add("id", id)
            .add("version", version)
            .add("name", name)
            .toString();
    }

    @Override
    public int compareTo(TwitchChannel o) {
        return this.name.compareTo( o.name);
    }
}
