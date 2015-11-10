package com.ryonday.automation.twitch.util;

import com.google.common.collect.ImmutableSortedSet;
import com.ryonday.automation.twitch.EmoteTag;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Stream;

import static com.ryonday.automation.twitch.util.EmoteTagParser.EMOTE_TAG_PATTERN;
import static org.junit.Assert.*;

public class EmoteTagParserTest {

    static final String single_emote_single_instance = "47816:0-6";
    static final String single_emote_two_instance = "47816:0-6,8-14";
    static final String single_emote_multiple_instance = "47816:0-6,8-14,16-22,24-30,32-38";

    static final String two_emotes_single_instance = "47816:0-6/44751:16-23";
    static final String two_emotes_two_instance = "47816:0-6,8-14/44751:16-23,25-33";
    static final String two_emotes_multiple_instance = "47816:0-6,8-14,16-22,24-30,32-38/44751:40-47,49-56,58-65,67-74,76-83";

    static final String multiple_emotes_single_instance = "44751:8-15/44757:17-25/47816:0-6";
    static final String multiple_emotes_two_instance = "44757:34-42,44-52/47816:0-6,8-14/44751:16-23,25-32";
    static final String multiple_emotes_multiple_instance = "47816:0-6,8-14,16-22,24-30,32-38/44751:40-47,49-56,58-65,67-74,76-83/44757:85-93,95-103,105-113,115-123,125-133";

    static final String tons_of_mixed_index_emotes =
            "47816:0-6,8-14,153-159/" +
            "44751:16-23,144-151,199-206/" +
            "44757:25-33,99-107,134-142,189-197/" +
            "37620:35-45,122-132,208-218/" +
            "38117:47-58,109-120,220-231/" +
            "44577:60-66,172-178,233-239/" +
            "37365:68-77,79-88,161-170,241-250/" +
            "44579:90-97,180-187";


    @Test
    public void testTheEmoteTagRegex() throws Exception {


        Stream.of(
                single_emote_single_instance,
                single_emote_two_instance,
                single_emote_multiple_instance,
                two_emotes_single_instance,
                two_emotes_two_instance,
                two_emotes_multiple_instance,
                multiple_emotes_single_instance,
                multiple_emotes_two_instance,
                multiple_emotes_multiple_instance,
                tons_of_mixed_index_emotes
        ).forEach(
                s -> assertTrue(EMOTE_TAG_PATTERN.matcher(s).matches())
        );

        Stream.of("47816:0-a",
                "ryonScotch:0-5",
                " 47816:0-5",
                "47816:0-20 ",
                "44751:8-15/ 44757:17-25/47816:0-6",
                "44751:8-15/44757:17-25/47816:0-6/",
                "44757:34-42,44-52,/47816:0-6,8-14/44751:16-23,25-32").forEach(s -> assertFalse(EMOTE_TAG_PATTERN.matcher(s).matches()));
    }

    @Test
    public void testItReturnsAnEmptyListOnNullInput() throws Exception {
        Set<EmoteTag> l = EmoteTagParser.parseEmoteTag(null);
        assertTrue(l.isEmpty());
    }

    @Test
    public void testItReturnsAnEmptyListOnEmptyInput() throws Exception {
        Set<EmoteTag> l = EmoteTagParser.parseEmoteTag("");
        assertTrue(l.isEmpty());
    }

    @Test
    public void testItReturnsAnEmptyListOnInvalidInput() throws Exception {
        Set<EmoteTag> l = EmoteTagParser.parseEmoteTag("HELLO WURLD");
        assertTrue(l.isEmpty());
    }

    @Test
    public void testItCanParseASingleBuddy() throws Exception {

        Set expected = ImmutableSortedSet.of(new EmoteTag(47816, 0, 6));
        Set actual = EmoteTagParser.parseEmoteTag(single_emote_single_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseTwoInstancesOfTheSameBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(new EmoteTag(47816, 0, 6), new EmoteTag(47816, 8, 14));
        Set actual = EmoteTagParser.parseEmoteTag(single_emote_two_instance);
        assertEquals(expected, actual);

    }

    @Test
    public void testItCanParseLotsOfInstancesOfTheSameBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(47816, 16, 22),
                new EmoteTag(47816, 24, 30),
                new EmoteTag(47816, 32, 38)
        );
        Set actual = EmoteTagParser.parseEmoteTag(single_emote_multiple_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseADoubleBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(new EmoteTag(47816, 0, 6),
                new EmoteTag(44751, 16, 23)
        );
        Set actual = EmoteTagParser.parseEmoteTag(two_emotes_single_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseADoubleDoubleBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(44751, 16, 23),
                new EmoteTag(44751, 25, 33)
        );
        Set actual = EmoteTagParser.parseEmoteTag(two_emotes_two_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseADoubleMultiBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(47816, 16, 22),
                new EmoteTag(47816, 24, 30),
                new EmoteTag(47816, 32, 38),
                new EmoteTag(44751, 40, 47),
                new EmoteTag(44751, 49, 56),
                new EmoteTag(44751, 58, 65),
                new EmoteTag(44751, 67, 74),
                new EmoteTag(44751, 76, 83)
        );
        Set actual = EmoteTagParser.parseEmoteTag(two_emotes_multiple_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseAMultiSingleBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(44751, 8, 15),
                new EmoteTag(44757, 17, 25),
                new EmoteTag(47816, 0, 6)
        );
        Set actual = EmoteTagParser.parseEmoteTag(multiple_emotes_single_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseAMultiDoubleBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(44757, 34, 42),
                new EmoteTag(44757, 44, 52),
                new EmoteTag(44751, 16, 23),
                new EmoteTag(44751, 25, 32)
        );
        Set actual = EmoteTagParser.parseEmoteTag(multiple_emotes_two_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseAMultiMultiBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(47816, 16, 22),
                new EmoteTag(47816, 24, 30),
                new EmoteTag(47816, 32, 38),

                new EmoteTag(44757, 85, 93),
                new EmoteTag(44757, 95, 103),
                new EmoteTag(44757, 105, 113),
                new EmoteTag(44757, 115, 123),
                new EmoteTag(44757, 125, 133),

                new EmoteTag(44751, 40, 47),
                new EmoteTag(44751, 49, 56),
                new EmoteTag(44751, 58, 65),
                new EmoteTag(44751, 67, 74),
                new EmoteTag(44751, 76, 83)
        );
        Set actual = EmoteTagParser.parseEmoteTag(multiple_emotes_multiple_instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testItCanParseAMegaBuddy() throws Exception {
        Set expected = ImmutableSortedSet.of(
                new EmoteTag(47816, 0, 6),
                new EmoteTag(47816, 8, 14),
                new EmoteTag(47816, 153, 159),

                new EmoteTag(44751, 16, 23),
                new EmoteTag(44751, 144, 151),
                new EmoteTag(44751, 199, 206),

                new EmoteTag(44757, 25, 33),
                new EmoteTag(44757, 99, 107),
                new EmoteTag(44757, 134, 142),
                new EmoteTag(44757, 189, 197),

                new EmoteTag(37620, 35, 45),
                new EmoteTag(37620, 122, 132),
                new EmoteTag(37620, 208, 218),

                new EmoteTag(38117, 47, 58),
                new EmoteTag(38117, 109, 120),
                new EmoteTag(38117, 220, 231),

                new EmoteTag(44577, 60, 66),
                new EmoteTag(44577, 172, 178),
                new EmoteTag(44577, 233, 239),

                new EmoteTag(37365, 68, 77),
                new EmoteTag(37365, 79, 88),
                new EmoteTag(37365, 161, 170),
                new EmoteTag(37365, 241, 250),

                new EmoteTag(44579, 90, 97),
                new EmoteTag(44579, 180, 187)
        );
        Set actual = EmoteTagParser.parseEmoteTag(tons_of_mixed_index_emotes);
        assertEquals(expected, actual);
    }

    @Test
    public void testItIgnoresDuplicateBuddies() throws Exception {
        Set<EmoteTag> expected = ImmutableSortedSet.of( new EmoteTag( 100, 10, 20 ) );

        Set<EmoteTag> actual = EmoteTagParser.parseEmoteTag("100:10-20,10-20,10-20");

        assertEquals( expected, actual );
    }

    @Test
    public void testItIgnoresInvalidBuddies() throws Exception {
        Set<EmoteTag> expected = ImmutableSortedSet.of( new EmoteTag( 100, 10, 20 ) );
        Set<EmoteTag> actual = EmoteTagParser.parseEmoteTag("100:10-20,10-5");
        assertEquals( expected, actual );
    }

    @Test
    public void testItIgnoresInvalidCompanionBuddies() throws Exception {
        Set<EmoteTag> expected = ImmutableSortedSet.of(
                new EmoteTag( 100, 10, 20 ),
                new EmoteTag( 100, 21, 31 ),
                new EmoteTag( 200, 42, 52 )
                );
        Set<EmoteTag> actual = EmoteTagParser.parseEmoteTag("100:10-20,21-31/200:42-52,60-55");
        assertEquals( expected, actual );
    }
}
