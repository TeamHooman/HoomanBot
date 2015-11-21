package com.ryonday.automation.twitch.converter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class ColorConverterTest {

    Logger logger = LoggerFactory.getLogger( ColorConverterTest.class);

    ColorConverter cc = new ColorConverter();

    @Test
    public void testWHISKYTANGFOXTROT() throws Exception {

        String color = "#9ACD32";
        Color c = Color.decode( color );
        logger.info( "Color: {}", c );


    }

    @Test
    public void testConvertToString() throws Exception {
        Color c = new Color(255, 0, 0 );
        String cs = cc.convertToDatabaseColumn( c );
        assertEquals( "FFFF0000", cs );

        c = new Color( 0, 255, 0 );
        cs = cc.convertToDatabaseColumn( c );
        assertEquals( "FF00FF00", cs );

        c = new Color( 0, 1, 255 );
        cs = cc.convertToDatabaseColumn( c );
        assertEquals( "FF0001FF", cs );
    }

    @Test
    public void testConvertToColor() throws Exception {
        String cs = "FFFF0000";
        Color c = cc.convertToEntityAttribute( cs );
        assertEquals( 255, c.getRed() );
        assertEquals( 0, c.getGreen() );
        assertEquals( 0, c.getBlue() );

        cs = "FF00FF00";
        c = cc.convertToEntityAttribute( cs );
        assertEquals( 0, c.getRed() );
        assertEquals( 255, c.getGreen() );
        assertEquals( 0, c.getBlue() );

        cs = "FF0101FF";
        c = cc.convertToEntityAttribute( cs );
        assertEquals( 1, c.getRed() );
        assertEquals( 1, c.getGreen() );
        assertEquals( 255, c.getBlue() );
    }
}
