package com.ryonday.automation.hue.runner;

import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

@Controller
public class HueRunner implements ApplicationRunner {


    private static final Logger logger = LoggerFactory.getLogger(HueRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PHHueSDK hueSDK = PHHueSDK.getInstance();

        logger.info("Beginning Hue Bridge Search...");
        PHBridgeSearchManager sm = (PHBridgeSearchManager) hueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
    }
}
