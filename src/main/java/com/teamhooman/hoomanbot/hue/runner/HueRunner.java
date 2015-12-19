package com.teamhooman.hoomanbot.hue.runner;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.teamhooman.hoomanbot.hue.utils.HueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class HueRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(HueRunner.class);

    private final PHAccessPoint accessPoint;

    @Autowired
    public HueRunner(Optional<PHAccessPoint> hueAccessPoint) {
        logger.info("what is it?: {}", hueAccessPoint);
        this.accessPoint = hueAccessPoint.orElse(null);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PHHueSDK hueSDK = PHHueSDK.getInstance();

        if( accessPoint != null ) {
            logger.info("Attempting connect to access point: {}", HueUtils.toString(accessPoint) );
            hueSDK.connect( accessPoint );
        } else {
            logger.info("Beginning Hue Bridge Search...");
            PHBridgeSearchManager sm = (PHBridgeSearchManager) hueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
            sm.search(true, true);
        }

    }
}
