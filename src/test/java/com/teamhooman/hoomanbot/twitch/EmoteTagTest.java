package com.teamhooman.hoomanbot.twitch;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmoteTagTest {

    @Test( expected = IllegalArgumentException.class )
    public void itWontAcceptNegativeStartIndex() throws Exception {
        EmoteTag t = new EmoteTag( 0, -1, 5 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void itWontAcceptNegativeEmoteLength() throws Exception {
        EmoteTag t = new EmoteTag( 0, 10, 5 );
    }

    @Test( expected = IllegalArgumentException.class )
    public void itWontAcceptZeroEmoteLength() throws Exception {
        EmoteTag t = new EmoteTag( 0, 10, 10 );

    }

    @Test
    public void testCompareTo() throws Exception {
        EmoteTag a = new EmoteTag( 0, 1, 5 );
        EmoteTag b = new EmoteTag( 0, 2, 6 );
        EmoteTag c = new EmoteTag( 1, 2, 6 );
        EmoteTag d = new EmoteTag( 1, 2, 7 );
        EmoteTag e = new EmoteTag( 1, 2, 7 );

        assertEquals( a.compareTo( a ), 0  );
        assertEquals( a.compareTo( b ), -1 );
        assertEquals( a.compareTo( c ), -1 );
        assertEquals( c.compareTo( a ), 1 );
        assertEquals( e.compareTo( e ), 0 );


    }
}
