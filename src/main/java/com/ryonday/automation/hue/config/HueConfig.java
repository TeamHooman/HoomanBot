package com.ryonday.automation.hue.config;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHNotificationManager;
import com.philips.lighting.hue.sdk.PHSDKListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@Configuration
public class HueConfig {

    private static final Logger logger = LoggerFactory.getLogger( HueConfig.class );

    @Bean(name="hueSDK")
    public PHHueSDK configureHueSdk( @Value("${hue.appName}") String hueAppName,
                            @Value("${hue.deviceName}") String hueDeviceName,
                                     @Value("${hue.bridgeIp:null}") String bridgeIp,
                                     @Value("${hue.username:null}") String username,
                                     List<PHSDKListener> listeners) {
        checkArgument(!isNullOrEmpty(hueAppName));
        checkArgument(!isNullOrEmpty(hueDeviceName));

        PHHueSDK hueSDK = PHHueSDK.getInstance();
        hueSDK.setAppName(hueAppName);
        hueSDK.setDeviceName(hueDeviceName);

        PHNotificationManager notificationManager = hueSDK.getNotificationManager();
        listeners.forEach(notificationManager::registerSDKListener);
        logger.info("Configured Hue Event Listeners: {}", listeners);

        logger.info( "Configured Phillips Hue SDK:\n\t" +
                "SDK Version:     {}\n\t" +
                "AppName:         {}\n\t" +
                "DeviceName:      {}\n\t",
                hueSDK.getSDKVersion(), hueAppName, hueDeviceName );

        return hueSDK;
    }
}
