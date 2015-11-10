package com.ryonday.automation.hue.utils;

import com.google.common.collect.ImmutableMap;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;

public class HueUtils {

    private static final Logger logger = LoggerFactory.getLogger(HueUtils.class);

    private static final Map<Integer, String> intToMessageType;

    static {
        ImmutableMap.Builder<Integer, String> b = ImmutableMap.builder();

        for( Field f : PHMessageType.class.getDeclaredFields() ) {
            if( Modifier.isStatic( f.getModifiers() ) && int.class.equals( f.getType()) || Integer.class.equals( f.getType()) ) {
                try {
                    b.put( (Integer) f.get( null), f.getName() );
                } catch (IllegalAccessException e) {
                   logger.error( "Could not add Field definition to message ID map: {}", f );
                }
            }
        }

        intToMessageType = b.build();
    }

    public static String apToString(PHAccessPoint accessPoint) {
        if (accessPoint == null) {
            return null;
        }
        return toStringHelper(PHAccessPoint.class)
                .add("id", accessPoint.getBridgeId())
                .add("ip", accessPoint.getIpAddress())
                .add("mac", accessPoint.getMacAddress())
                .add("username", accessPoint.getUsername())
                .toString();
    }

    public static String translateMessageCode( int i ) {
        String type = intToMessageType.get( i );
        return type != null ? type : "<UNKNOWN>";
    }
}
