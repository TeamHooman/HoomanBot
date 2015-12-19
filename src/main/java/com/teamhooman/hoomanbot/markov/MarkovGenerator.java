/*
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of JMegaHal.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: JMegaHal.java,v 1.4 2004/02/01 13:24:06 pjm2 Exp $

*/

package com.teamhooman.hoomanbot.markov;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.chars.CharSet;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;

public class MarkovGenerator implements Serializable {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(MarkovGenerator.class);

    private final static Random rand = new SecureRandom();

    private final Map<String, Set<ListQuad>> wordToQuads = new HashMap<>();

    private final Map<ListQuad, ListQuad> listQuads = new HashMap<>();

    private final Map<ListQuad, Set<String>> quadToPreviousWord = new HashMap<>();
    private final Map<ListQuad, Set<String>> quadToNextWord = new HashMap<>();

    // These are valid chars for words. Anything else is treated as punctuation.
    private static final CharSet END_CHARS = new CharOpenHashSet(".!?".toCharArray());
    private final static CharMatcher whitespaceMatcher = CharMatcher.INVISIBLE.or(CharMatcher.WHITESPACE);
    private final static CharMatcher wordMatcher = CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf("-_/")).precomputed();
    private final static CharMatcher nonWordMatcher = wordMatcher.negate().precomputed();

    /**
     * Construct an instance of JMegaHal with an empty brain.
     */
    public MarkovGenerator() {

    }

    /**
     * Adds an entire documents to the 'brain'.  Useful for feeding in
     * stray theses, but be careful not to put too much in, or you may
     * run out of memory!
     */
    public void addDocument(String uri) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(uri).openStream()));
        StringBuilder buffer = new StringBuilder();

        int ch = 0;
        while ((ch = reader.read()) != -1) {
            buffer.append((char) ch);
            if (END_CHARS.contains((char) ch)) {
                String sentence = whitespaceMatcher.collapseFrom(buffer, ' ');
                add(sentence);
                buffer = new StringBuilder();
            }
        }
        add(whitespaceMatcher.collapseFrom(buffer, ' '));
        reader.close();
    }

    /**
     * Adds a new sentence to the 'brain'
     */
    public void add(CharSequence input) {

        Map<CharMatcher, CharMatcher> swap = ImmutableMap.of( wordMatcher, nonWordMatcher, nonWordMatcher, wordMatcher );
        CharMatcher matcher = wordMatcher;
        List<String> parts = Lists.newArrayList();

//        input.chars().spliterator().

        Splitter sentenceSplitter = Splitter.on( wordMatcher.negate() ).trimResults(whitespaceMatcher).omitEmptyStrings();

        List<String> split = sentenceSplitter.splitToList(input);

        logger.info( "Processed:\n\t" +
                "Input: {}\n\t" +
                "Words: {}", input, split );

        if( split.size() < 4 ) {
            logger.warn( "Sentence was not long enough to learn from.");
            return;
        }

        for( int n = 0 ; n < split.size() - 3 ; ++n ) {
            List<String> subList = split.subList( n, n + 4 );
            ListQuad lq = new ListQuad( subList );

            if( listQuads.containsKey(lq)) {
                lq = listQuads.get( lq );
            } else {
                listQuads.put( lq, lq );
            }

            if( n == 0 ) {
                lq.setCanStart( true );
            }

            if( n == split.size() - 4 ) {
                lq.setCanEnd( true );
            }

            for( int q = 0 ; q < 4 ; ++q ) {
                String token = split.get( n + q );
                addAssociation(wordToQuads, token, lq);
                logger.info("Added word-to-containing-quad association: {} / {}", token, lq);
            }

            if( n > 0 ) {
                String previousToken = split.get( n - 1 );
                addAssociation(quadToPreviousWord, lq, previousToken);
                logger.info("Added quad-to-possible-predecessor association: {} / {}", previousToken, lq);
            }

            if( n < split.size() - 4 ) {
                String nextToken = split.get( n + 4 );
                addAssociation(quadToNextWord, lq, nextToken);
                logger.info("Added quad-to-possible-successor association: {} / {}", nextToken, lq);
            }
        }
    }

    private <T, U> void addAssociation(Map<T, Set<U>> source, T key, U value) {
        Set<U> set = source.get( key );
        if( set == null ) {
            set = new HashSet<>(1);
            source.put( key, set);
        }
        set.add( value );
    }

    /**
     * Generate a random sentence from the brain.
     */
    public String getSentence() {
        return getSentence(null);
    }

    /**
     * Generate a sentence that includes (if possible) the specified word.
     */
    public String getSentence(String word) {

        Set<ListQuad> quads = MoreObjects.firstNonNull(wordToQuads.get(word), this.listQuads.keySet());

        if( quads.size() == 0 ) {
            logger.warn("Not enough information to construct sentence; returning the empty String.");
            return "";
        }

        LinkedList<String> parts = new LinkedList<>();
        ListQuad mid = quads.stream().skip(rand.nextInt( quads.size())).findFirst().get();
        ListQuad quad = mid;
        parts.addAll( quad.getTokens() );

        while( !quad.canEnd() ) {
            Set<String> nextTokens = quadToNextWord.get( quad );
            String nextToken = nextTokens.stream().skip( rand.nextInt(nextTokens.size())).findFirst().get();
            List<String> newList = Lists.newArrayList( quad.getTokens().subList(1, 4));
            newList.add( nextToken );
            quad = listQuads.get( new ListQuad( newList ) );
            parts.add( nextToken);
            logger.info("Added '{}' to end.", nextToken );
        }

        quad = mid;

        while( !quad.canStart() ) {
            Set<String> previousTokens = quadToPreviousWord.get( quad );
            String previousToken = previousTokens.stream().skip( rand.nextInt(previousTokens.size())).findFirst().get();
            List<String> newList = new ArrayList<>();
            newList.add( previousToken);
            newList.addAll(quad.getTokens().subList(0, 3));
            parts.addFirst( previousToken );
            logger.info("Added '{}' to beginning.", previousToken );
        }

        Joiner j = Joiner.on( ' ' );
        return j.join( parts );
    }
}
