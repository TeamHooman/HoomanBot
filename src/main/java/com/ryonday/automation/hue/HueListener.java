package com.ryonday.automation.hue;

import com.google.common.base.Joiner;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;
import com.ryonday.automation.hue.utils.HueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ryonday.automation.hue.utils.HueUtils.apToString;

@Service
public class HueListener implements PHSDKListener {

    private final static Logger logger = LoggerFactory.getLogger(HueListener.class);

    private final PHHueSDK hueSdk;

    private final static Joiner JOINER = Joiner.on(", ").useForNull("<NULL>");

    public HueListener() {
        checkNotNull(this.hueSdk = PHHueSDK.getInstance());
    }

    @Override
    public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
//        logger.info("onCacheUpdated(list = {}, bridgeId = {})", JOINER.join(list), phBridge.getResourceCache().getBridgeConfiguration().getBridgeID());
    }

    @Override
    public void onBridgeConnected(PHBridge bridge, String username) {
        logger.info("onBridgeConnected(bridgeId = {}, username = {}) at IP address {}",
                bridge.getResourceCache().getBridgeConfiguration().getBridgeID(), username, bridge.getResourceCache().getBridgeConfiguration().getIpAddress());

        hueSdk.setSelectedBridge(bridge);
        hueSdk.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);

        // Here it is recommended to set your connected bridge in your sdk object (as above) and start the heartbeat.
        // At this point you are connected to a bridge so you should pass control to your main program/activity.
        // The username is generated randomly by the bridge.
        // Also it is recommended you store the connected IP Address/ Username in your app here.  This will allow easy automatic connection on subsequent use.
    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        logger.info("onAuthenticationRequired({})", apToString(accessPoint));
        hueSdk.startPushlinkAuthentication(accessPoint);
    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
        logger.info("onAccessPointsFound({})", JOINER.join(accessPoints.stream().map(HueUtils::apToString).iterator()));

        // Choosing first :/

        PHAccessPoint accessPoint = accessPoints.get(0);
        hueSdk.connect(accessPoint);

    }

    @Override
    public void onError(int i, String s) {
        logger.info("onError({}, {})", i, s);

    }

    @Override
    public void onConnectionResumed(PHBridge bridge) {
//        logger.info("onConnectionResumed({})", bridge.getResourceCache().getBridgeConfiguration().getBridgeID());

    }

    @Override
    public void onConnectionLost(PHAccessPoint accessPoint) {
        logger.info("onConnectionLost({})", apToString( accessPoint));

    }

    @Override
    public void onParsingErrors(List<PHHueParsingError> parsingErrors) {
        logger.info("onParsingErrors({})", JOINER.join(parsingErrors));

    }
}
