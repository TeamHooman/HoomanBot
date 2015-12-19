package com.teamhooman.hoomanbot.hue.config;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHNotificationManager;
import com.philips.lighting.hue.sdk.PHSDKListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@ConfigurationProperties(prefix = "hue")
@Configuration
public class HueConfig {

    private static final Logger logger = LoggerFactory.getLogger( HueConfig.class );

    @NotNull
    private String appName;

    @NotNull
    private String deviceName;

    private String bridgeIp;

    private String username;

    public String getAppName() {
        return appName;
    }

    public HueConfig setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getBridgeIp() {
        return bridgeIp;
    }

    public HueConfig setBridgeIp(String bridgeIp) {
        this.bridgeIp = bridgeIp;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public HueConfig setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public HueConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    @Bean(name="hueSDK")
    public PHHueSDK configureHueSdk( List<PHSDKListener> listeners ) {
        checkArgument(!isNullOrEmpty(appName));
        checkArgument(!isNullOrEmpty(deviceName));

        PHHueSDK hueSDK = PHHueSDK.getInstance();
        hueSDK.setAppName(appName);
        hueSDK.setDeviceName(deviceName);

        PHNotificationManager notificationManager = hueSDK.getNotificationManager();
        listeners.forEach(notificationManager::registerSDKListener);
        logger.info("Configured Hue Event Listeners: {}", listeners);

        logger.info( "Configured Phillips Hue SDK:\n\t" +
                "SDK Version:     {}\n\t" +
                "AppName:         {}\n\t" +
                "DeviceName:      {}\n\t",
                hueSDK.getSDKVersion(), appName, deviceName );

        return hueSDK;
    }

    @Bean(name="hueAccessPoint")
    public PHAccessPoint configureAccessPoint() {
        if( isNullOrEmpty(bridgeIp) || isNullOrEmpty( username )) {
            logger.warn("Bridge IP and/or Username not available; cannot autoconnect.");
            return null;
        }

        PHAccessPoint accessPoint = new PHAccessPoint();
        accessPoint.setIpAddress( bridgeIp);
        accessPoint.setUsername( username );
        logger.info("Returning Hue Access Point: {}", accessPoint );
        return accessPoint;
    }
}
