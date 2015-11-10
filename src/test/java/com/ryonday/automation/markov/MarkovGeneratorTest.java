package com.ryonday.automation.markov;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class MarkovGeneratorTest {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(MarkovGeneratorTest.class);

    MarkovGenerator testGen;
    JMegaHal testHAL;

    @Before
    public void setUp() throws Exception {
        testGen = new MarkovGenerator();
        testHAL = new JMegaHal();
    }

    @Test
    public void testIt() throws Exception {
        testGen.add( "The quickest of all brown foxes quickly jumped with great grace and poise over the slothful, slumbering hounds.");
        testHAL.add( "The quickest of all brown foxes quickly jumped with great grace and poise over the slothful, slumbering hounds." );

//        logger.info("Test sentence: {}", sample );


    }

}
