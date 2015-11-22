package com.ryonday.automation.twitch.converter;

import javafx.scene.paint.Color;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;

public class ColorConverterTest {

    Logger logger = LoggerFactory.getLogger( ColorConverterTest.class);

    ColorConverter cc = new ColorConverter();

    @Test
    public void testItReturnsBlackOnNullInput() throws Exception {
        String s = cc.convertToDatabaseColumn( null );
        assertEquals( s, "#000000" );

        Color c = cc.convertToEntityAttribute( null );
        assertEquals( c, Color.BLACK );
    }

    @Test
    public void testItReturnsBlackOnInvalidInput() throws Exception {

        Color c = cc.convertToEntityAttribute( "GIBBERISH" );
        assertEquals( c, Color.BLACK );
    }

    @Test
    public void testConvertToString() throws Exception {
        Color c = Color.rgb(255, 0, 0 );
        String cs = cc.convertToDatabaseColumn( c );
        assertEquals( "#FF0000", cs );

        c = Color.rgb( 0, 255, 0 );
        cs = cc.convertToDatabaseColumn( c );
        assertEquals( "#00FF00", cs );

        c = Color.rgb( 0, 1, 255 );
        cs = cc.convertToDatabaseColumn( c );
        assertEquals( "#0001FF", cs );
    }

    @Test
    public void testConvertToColor() throws Exception {
        String cs = "#FF0000";
        Color c = cc.convertToEntityAttribute( cs );
        assertEquals( 1.0, c.getRed() );
        assertEquals( 0.0, c.getGreen() );
        assertEquals( 0.0, c.getBlue() );

        cs = "#00FF00";
        c = cc.convertToEntityAttribute( cs );
        assertEquals( 0.0, c.getRed() );
        assertEquals( 1.0, c.getGreen() );
        assertEquals( 0.0, c.getBlue() );

        cs = "#0101FF";
        c = cc.convertToEntityAttribute( cs );
        assertEquals( 1, c.getRed() * 255, .0000000591389835 ); // :|
        assertEquals( 1, c.getGreen() * 255, .0000000591389835 );
        assertEquals( 1.0, c.getBlue() );
    }
}
