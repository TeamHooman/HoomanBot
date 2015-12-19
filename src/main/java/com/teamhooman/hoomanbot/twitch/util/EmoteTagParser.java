package com.teamhooman.hoomanbot.twitch.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.teamhooman.hoomanbot.twitch.EmoteTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * Various parsing methods for the Twitch IRC 'emotes' tag. For example:
 * <p>
 * Raw Message: "ryonEat ryonEat ryonGood ryonGood ryonGucci ryonSail ryonScotch ryonScotch ryonBye ryonRealTalk ryonRealTalk"
 * Emotes tag content: "47816:0-6,8-14/44751:16-23,25-32/44757:34-42/44579:44-51/37365:53-62,64-73/44577:75-81/38117:83-94,96-107"
 * <p>
 * This means that the Twitch emote with ID 47816 takes up indeces with ranges 0-6 and 8-14 (inclusive) in the message String.
 * The emote with ID 44751 takes up indeces sets 16-23 and 25-32, etc.
 */
public class EmoteTagParser {

    private static final Logger logger = LoggerFactory.getLogger(EmoteTagParser.class);

    private final static String EMOTE_INDEX = "\\d+-\\d+";
    private final static String ADDTL_EMOTE_INDECES = "(," + EMOTE_INDEX + ")*";
    private final static String EMOTE_GROUP = "\\d+:" + EMOTE_INDEX + ADDTL_EMOTE_INDECES;
    private final static String ADDTL_EMOTE_GROUPS = "(/" + EMOTE_GROUP + ")*";
    private final static String EMOTE_CLUSTER = EMOTE_GROUP + ADDTL_EMOTE_GROUPS;

    public final static Pattern EMOTE_TAG_PATTERN = Pattern.compile(EMOTE_CLUSTER);

    private final static Splitter SECTION = Splitter.on('/').omitEmptyStrings().trimResults();
    private final static Splitter EMOTE = Splitter.on(':').omitEmptyStrings().limit(2).trimResults();
    private final static Splitter INSTANCES = Splitter.on(',').omitEmptyStrings().trimResults();
    private final static Splitter RANGE = Splitter.on('-').limit(2).trimResults();

    public static Set<EmoteTag> parseEmoteTag(String emoteTag) {
        if (Strings.isNullOrEmpty(emoteTag)) {
            return ImmutableSet.of();
        } else if( !EMOTE_TAG_PATTERN.matcher(emoteTag).matches()) {
            logger.warn("Empty/Null/noncompliant emote tag.\n\t" +
                "Pattern: '{}'\n\t" +
                "Tag:     '{}'", EMOTE_TAG_PATTERN, emoteTag);
            return ImmutableSet.of();
        }

        // ex: 100:10-20,21-31/200:41-51,52-62/400:70-80
        Set<EmoteTag> tags = SECTION.splitToList(emoteTag)      // ["100:10-20,21-31", "200:41-51,52-62", "400:70-80"]
            .stream()                                       // "100:10-20,21-31"
            .map(EMOTE::splitToList)                              // ["100", "10-20,21-31"]
            .map(l -> ImmutableList.<String>builder()
                .add(l.get(0))
                .addAll(INSTANCES.split(l.get(1)))
                .build())                           // ["100", "10-20", "21-31"]
            .flatMap(l ->
                l.subList(1, l.size())
                    .stream()
                    .map(s ->
                        ImmutableList.<String>builder()
                            .add(l.get(0))
                            .addAll(RANGE.split(s))
                            .build()) // ["100, "10", "20"], ["100", "21", "31"]
            )
            .map(l -> {
                    try {
                        EmoteTag t = new EmoteTag(
                            Integer.parseInt(l.get(0)),
                            Integer.parseInt(l.get(1)),
                            Integer.parseInt(l.get(2)));
                        logger.debug("Created EmoteTag: {}", t);
                        return t;
                    } catch (IllegalArgumentException ex) {
                        logger.warn("Encountered invalid EmoteTag definition: {}", ex.getMessage());
                        return null;
                    }
                }
            )
            .filter(e -> e != null)
            .collect(collectingAndThen(
                toSet(),
                ImmutableSortedSet::copyOf
            ));
        logger.debug("Converted emote tag to List:\n\t" +
            "Tag:  {}\n\t" +
            "List: {}", emoteTag, tags);

        return tags;

    }
}
