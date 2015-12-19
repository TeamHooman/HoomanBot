package com.teamhooman.hoomanbot.hue;

import com.google.common.base.Joiner;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueParsingError;
import com.teamhooman.hoomanbot.hue.utils.HueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class HueListener implements PHSDKListener {

    private final static Logger logger = LoggerFactory.getLogger(HueListener.class);

    private final PHHueSDK hueSdk;

    private final static Joiner JOINER = Joiner.on(", ").skipNulls();

    public HueListener() {
        checkNotNull(this.hueSdk = PHHueSDK.getInstance());
    }

    @Override
    public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
//        logger.info("onCacheUpdated(list = {}, bridgeId = {})", JOINER.join(list), phBridge.getResourceCache().getBridgeConfiguration().getBridgeID());
    }

    @Override
    public void onBridgeConnected(PHBridge bridge, String username) {
        PHBridgeResourcesCache cache = bridge.getResourceCache();
        hueSdk.setSelectedBridge(bridge);
        hueSdk.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);

        logger.info("Connected to Hue Bridge:\n\t" +
                "Username: {}\n\t" +
                "Bridge:   {}",
            username,
            HueUtils.toString(bridge)
        );

    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        logger.info("onAuthenticationRequired({})", HueUtils.toString(accessPoint));
        hueSdk.startPushlinkAuthentication(accessPoint);
    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
        logger.info("onAccessPointsFound({})", JOINER.join(accessPoints.stream().map(HueUtils::toString).iterator()));

        // Choosing first :/

        PHAccessPoint accessPoint = accessPoints.get(0);
        hueSdk.connect(accessPoint);

    }

    @Override
    public void onError(int i, String s) {
        logger.info("onError({}, {})", i, s);
        if( i == 46 ) { // Bridge not responding
            PHBridgeSearchManager sm = (PHBridgeSearchManager) hueSdk.getSDKService(PHHueSDK.SEARCH_BRIDGE);
            sm.search(true, true);
        }
    }

    @Override
    public void onConnectionResumed(PHBridge bridge) {
//        logger.info("onConnectionResumed({})", bridge.getResourceCache().getBridgeConfiguration().getBridgeID());

    }

    @Override
    public void onConnectionLost(PHAccessPoint accessPoint) {
        logger.info("onConnectionLost({})", HueUtils.toString(accessPoint));

    }

    @Override
    public void onParsingErrors(List<PHHueParsingError> parsingErrors) {
        logger.info("onParsingErrors({})", JOINER.join(parsingErrors));

    }
}
