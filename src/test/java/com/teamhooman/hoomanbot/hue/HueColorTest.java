package com.teamhooman.hoomanbot.hue;

import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;

public class HueColorTest {
    Logger logger = LoggerFactory.getLogger(HueColorTest.class);

    @Test
    public void testIt() throws Exception {
//
//        Color color = Color.rgb(255, 0, 0 );
//        int r = (int) (color.getRed() * 255.0);
//        int g = (int) (color.getGreen() * 255.0);
//        int b = (int) (color.getBlue() * 255.0);

//        logger.info("Color: {}", color );
        int r = 255, b = 0, g = 0;

        String model = "LCT001";
        logger.info("r/g/b: {}/{}/{}", r, g, b);
        float[] xy = PHUtilities.calculateXYFromRGB(r, g, b, model);
        logger.info("xy: {}", Arrays.toString(xy));

        int intColor = PHUtilities.colorFromXY(xy, model);
        logger.info("intColor from xy: {}", intColor);

        xy = PHUtilities.calculateXY(intColor, model);
        logger.info("xy from intColor: {}", xy);

        r = intColor >> 16 & 255;
        g = intColor >> 8 & 255;
        b = intColor & 255;

        logger.info("rgb from intColor: {}/{}/{}", r, g, b);


    }

    @Test
    public void testIt2() throws Exception {
        SecureRandom sr = new SecureRandom();

        int x = Math.abs(sr.nextInt()) & 0x0000FFFF;
        int y = Math.abs(sr.nextInt()) >> 16;
        int z = Math.abs( sr.nextInt(65535));

        logger.info("x={} y={} z={}", x,y,z);

    }
}
