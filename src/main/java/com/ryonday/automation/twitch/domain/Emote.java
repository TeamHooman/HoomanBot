package com.ryonday.automation.twitch.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "emotes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"emote"})})
public class Emote {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "emote", nullable = false, updatable = false)
    private String emote;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emote")
    private Set<EmoteInChat> uses = Sets.newHashSet();

    public Long getId() {
        return id;
    }

    public Emote setId( Long id ) {
        this.id = id;
        return this;
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

    public Set<EmoteInChat> getUses() {
        return ImmutableSortedSet.copyOf(uses);
    }

    public Emote addUses( Collection<EmoteInChat> uses) {
        if( uses != null ) {
            uses.forEach(this::addUse);
        }
        return this;
    }

    public Emote addUse( EmoteInChat use ) {
        if( use != null && !uses.contains( use )) {
            uses.add( use );
            use.setEmote( this );
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Emote)) return false;
        Emote emote1 = (Emote) o;
        return Objects.equals(emote, emote1.emote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emote);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Emote.class)
            .add("id", id)
            .add("version", version)
            .add("emote", emote)
//            .add("uses", uses)
            .toString();
    }
}
