package com.teamhooman.hoomanbot.antlr;

import com.teamhooman.hoomanbot.twitch.antlr.TwitchEmoteLexer;
import com.teamhooman.hoomanbot.twitch.antlr.TwitchEmoteParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ryon on 11/23/15.
 */
public class AntlrShitsTest {

    private final static Logger logger = LoggerFactory.getLogger( AntlrShitsTest.class );
    String dope = "47816:0-6,8-14/" +
        "44751:16-23,25-32/" +
        "44757:34-42/" +
        "44579:44-51/" +
        "37365:53-62,64-73/" +
        "44577:75-81/" +
        "38117:83-94,96-107";

    String dope2 = "47816:0-6";
    String dope3 = "47816:0-6,8-14";

    @Test
    public void testThisShit() throws Exception {

        ANTLRInputStream anis = new ANTLRInputStream(dope2);
        TwitchEmoteLexer lx = new TwitchEmoteLexer(anis);
        CommonTokenStream str = new CommonTokenStream( lx );
        TwitchEmoteParser prs = new TwitchEmoteParser( str );
        ParseTree tree = prs.emoteString();

        logger.info( tree.toStringTree( prs ) );

    }


}
