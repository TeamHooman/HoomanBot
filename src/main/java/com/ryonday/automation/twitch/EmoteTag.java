package com.ryonday.automation.twitch;

import com.google.common.base.MoreObjects;

import java.util.Comparator;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Encapsulates the Emote ID and start/end indices for a Twitch emote tag.
 */
public class EmoteTag implements Comparable<EmoteTag> {
    public final long id;
    public final int startIndex;
    public final int endIndex;

    public EmoteTag(long id, int startIndex, int endIndex) {
        checkArgument(startIndex > -1, "Negative emote start index (%s) not allowed.", startIndex);
        checkArgument(endIndex > startIndex, "Nonpositive emote length ( %s - %s = %s) not allowed.", endIndex, startIndex, endIndex - startIndex);

        this.endIndex = endIndex;
        this.id = id;
        this.startIndex = startIndex;
    }

    public long getId() {
        return id;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmoteTag)) return false;
        EmoteTag emoteTag = (EmoteTag) o;
        return id == emoteTag.id &&
                startIndex == emoteTag.startIndex &&
                endIndex == emoteTag.endIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startIndex, endIndex);
    }


    @Override
    public int compareTo(EmoteTag o) {
        checkNotNull(o, "Will not compare to null object.");
        return naturalOrder.compare( this, o );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(EmoteTag.class)
                .add("id", id)
                .add("startIndex", startIndex)
                .add("endIndex", endIndex)
                .toString();
    }

    public final static Comparator<EmoteTag> byStartingIndex = Comparator.comparingLong( EmoteTag::getStartIndex );
    public final static Comparator<EmoteTag> byId = Comparator.comparingLong( EmoteTag::getId );
    public final static Comparator<EmoteTag> byEndIndex = Comparator.comparingLong(EmoteTag::getEndIndex);
    public final static Comparator<EmoteTag> naturalOrder = byId.thenComparing(byStartingIndex).thenComparing(byEndIndex);
}
