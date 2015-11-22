package com.ryonday.automation.hue.utils;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.model.*;
import com.philips.lighting.model.rule.PHRule;
import com.philips.lighting.model.rule.PHRuleAction;
import com.philips.lighting.model.rule.PHRuleCondition;
import com.philips.lighting.model.sensor.PHSensor;
import com.philips.lighting.model.sensor.PHSensorConfiguration;
import com.philips.lighting.model.sensor.PHSensorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Strings.emptyToNull;
import static java.util.stream.Collectors.toList;

public class HueUtils {

    private static final Logger logger = LoggerFactory.getLogger(HueUtils.class);

    private static final Map<Integer, String> intToMessageType;

    static {
        ImmutableMap.Builder<Integer, String> b = ImmutableMap.builder();

        for (Field f : PHMessageType.class.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers()) && int.class.equals(f.getType()) || Integer.class.equals(f.getType())) {
                try {
                    b.put((Integer) f.get(null), f.getName());
                } catch (IllegalAccessException e) {
                    logger.error("Could not add Field definition to message ID map: {}", f);
                }
            }
        }

        intToMessageType = b.build();
    }

    public static String toString(PHAccessPoint accessPoint) {

        ToStringHelper helper = toStringHelper(PHAccessPoint.class);

        if (accessPoint == null) {
            return helper.addValue(null).toString();
        }

        return helper.omitNullValues()
            .add("id", accessPoint.getBridgeId())
            .add("ip", accessPoint.getIpAddress())
            .add("mac", accessPoint.getMacAddress())
            .add("username", accessPoint.getUsername())

            .toString();
    }

    public static String toString(PHBridge bridge) {
        ToStringHelper helper = toStringHelper(PHBridge.class);

        if (bridge == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("resourceCache", toString(bridge.getResourceCache()))
            .toString();
    }

    public static String toString(PHBridgeResourcesCache cache) {

        ToStringHelper helper = toStringHelper(PHBridgeResourcesCache.class);

        if (cache == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("config", toString(cache.getBridgeConfiguration()))
            .add("lights", firstNonNull(cache.getAllLights(), ImmutableList.<PHLight>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("groups", firstNonNull(cache.getAllGroups(), ImmutableList.<PHGroup>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("rules", firstNonNull(cache.getAllRules(), ImmutableList.<PHRule>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("scenes", firstNonNull(cache.getAllScenes(), ImmutableList.<PHScene>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("schedules", Stream.concat(
                firstNonNull(cache.getAllSchedules(true), ImmutableList.<PHSchedule>of()).stream(),
                firstNonNull(cache.getAllSchedules(false), ImmutableList.<PHSchedule>of()).stream())
                .distinct()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("sensors", firstNonNull(cache.getAllSensors(), ImmutableList.<PHSensor>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("timers", Stream.concat(
                firstNonNull(cache.getAllTimers(true), ImmutableList.<PHSchedule>of()).stream(),
                firstNonNull(cache.getAllTimers(false), ImmutableList.<PHSchedule>of()).stream())
                .distinct()
                .map(HueUtils::toString)
                .collect(toList()))
            .toString();
    }

    public static String toString(PHSensor sensor) {
        ToStringHelper helper = toStringHelper(PHSensor.class);

        if (sensor == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("uniqueId", sensor.getUniqueId())
            .add("identifier", sensor.getIdentifier())
            .add("name", sensor.getName())
            .add("manufacturerName", sensor.getManufacturerName())
            .add("modelId", sensor.getModelId())
            .add("swVersion", sensor.getSwVersion())
            .add("typeAsString", sensor.getTypeAsString())
            .add("baseConfiguration", toString(sensor.getBaseConfiguration()))
            .add("baseState", toString(sensor.getBaseState()))
            .toString();
    }

    public static final String toString(PHSensorState state) {
        ToStringHelper helper = toStringHelper(PHSensorState.class);

        if (state == null) {
            return helper.addValue(null).toString();
        }
        return helper
            .omitNullValues()
            .add("lastUpdated", state.getLastUpdated())
            .toString();
    }

    public static final String toString(PHSensorConfiguration config) {
        ToStringHelper helper = toStringHelper(PHSensorConfiguration.class);

        if (config == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("on", config.getOn())
            .add("reachable", config.getReachable())
            .add("url", config.getUrl())
            .add("alertMode", config.getAlertMode())
            .add("battery", config.getBattery())
            .add("userTest", config.getUserTest())
            .toString();
    }

    public static String toString(PHSchedule schedule) {
        ToStringHelper helper = toStringHelper(PHSchedule.class);

        if (schedule == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("identifier", schedule.getIdentifier())
            .add("name", schedule.getName())
            .add("description", schedule.getDescription())
            .add("status", schedule.getStatus())
            .add("owner", schedule.getOwner())
            .add("sceneIdentifier", schedule.getSceneIdentifier())
            .add("groupIdentifier", schedule.getGroupIdentifier())
            .add("lightIdentifier", schedule.getLightIdentifier())
            .add("lightState", toString(schedule.getLightState()))
            .add("date", schedule.getDate())
            .add("localtime", schedule.getLocalTime())
            .add("created", schedule.getCreated())
            .add("startTime", schedule.getStartTime())
            .add("timer", schedule.getTimer())
            .add("randomTime", schedule.getRandomTime())
            .add("recurringDays", schedule.getRecurringDays())
            .add("recurringTimerInterval", schedule.getRecurringTimerInterval())
            .add("autoDelete", schedule.getAutoDelete())
            .toString();
    }

    public static String toString(PHScene scene) {
        ToStringHelper helper = toStringHelper(PHScene.class);

        if (scene == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .add("name", scene.getName())
            .add("sceneIdentifier", scene.getSceneIdentifier())
            .add("transitionTime", scene.getTransitionTime())
            .add("activeState", scene.getActiveState())
            .add("lightIdentifiers", scene.getLightIdentifiers())
            .omitNullValues()
            .toString();
    }

    public static String toString(PHRule rule) {
        ToStringHelper helper = toStringHelper(PHRule.class);

        if (rule == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("identifier", rule.getIdentifier())
            .add("name", rule.getName())
            .add("owner", rule.getOwner())
            .add("status", rule.getStatus())
            .add("timesTriggered", rule.getTimesTriggered())
            .add("creationTime", rule.getCreationTime())
            .add("lastTriggered", rule.getLastTriggered())
            .add("conditions", firstNonNull(rule.getConditions(), ImmutableList.<PHRuleCondition>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .add("actions", firstNonNull(rule.getActions(), ImmutableList.<PHRuleAction>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .toString();
    }

    public static String toString(PHRuleAction action) {
        ToStringHelper helper = toStringHelper(PHRuleAction.class);

        if (action == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .add("address", action.getAddress())
            .add("method", action.getMethod())
            .add("body", action.getBody())
            .omitNullValues()
            .toString();
    }

    public static String toString(PHRuleCondition condition) {
        ToStringHelper helper = toStringHelper(PHRuleCondition.class);

        if (condition == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("address", condition.getAddress())
            .add("operator", condition.getOperator())
            .add("value", condition.getValue())
            .toString();
    }

    public static String toString(PHGroup group) {
        ToStringHelper helper = toStringHelper(PHGroup.class);

        if (group == null) {
            return helper.addValue(null).toString();
        }

        return helper.omitNullValues()
            .add("name", group.getName())
            .add("uniqueId", group.getUniqueId())
            .add("identifier", group.getIdentifier())
            .add("type", group.getType())
            .add("modelId", group.getModelId())
            .add("lightIdentifiers", group.getLightIdentifiers())
            .toString();
    }

    public static String toString(PHLight light) {

        ToStringHelper helper = toStringHelper(PHLight.class);

        if (light == null) {
            return helper.addValue(null).toString();
        }

        return helper.omitNullValues().
            add("uniqueId", light.getUniqueId())
            .add("LuminaireUniqueid", emptyToNull(light.getLuminaireUniqueId()))
            .add("identifier", light.getIdentifier())
            .add("name", light.getName())
            .add("manufacturer", light.getManufacturerName())
            .add("model", light.getModelNumber())
            .add("version", light.getVersionNumber())
            .add("type", light.getLightType())
            .add("supportsBrightness", light.supportsBrightness())
            .add("supportsColor", light.supportsColor())
            .add("supportsCt", light.supportsCT())
            .add("state", toString(light.getLastKnownLightState()))
            .toString();
    }

    public static String toString(PHLightState state) {

        ToStringHelper helper = toStringHelper(PHLightState.class);

        if (state == null) {
            return helper.addValue(null).toString();
        }

        helper.omitNullValues()
            .add("reachable", state.isReachable())
            .add("on", state.isOn())
            .add("brightness", state.getBrightness())
            .add("brightnessIncrement", state.getIncrementBri())
            .add("transitionTime", state.getTransitionTime())
            .add("effectMode", state.getEffectMode())
            .add("alertMode", state.getAlertMode())
            .add("colorMode", state.getColorMode());

        switch (state.getColorMode()) {
            case COLORMODE_XY:
                helper.add("x", state.getX())
                    .add("y", state.getY())
                    .add("xIncrement", state.getIncrementX())
                    .add("yIncrement", state.getIncrementY());
                break;
            case COLORMODE_CT:
                helper.add("ct", state.getCt())
                    .add("ctIncrement", state.getIncrementCt());
                break;
            case COLORMODE_HUE_SATURATION:
                helper.add("hue", state.getHue())
                    .add("saturation", state.getSaturation())
                    .add("hueIncrement", state.getIncrementHue())
                    .add("saturationIncrement", state.getIncrementSat());
                break;
            case COLORMODE_NONE:
            case COLORMODE_UNKNOWN:
                break;
        }

        return helper.toString();
    }

    public static String toString(PHBridgeConfiguration config) {
        ToStringHelper helper = toStringHelper(PHBridgeConfiguration.class);

        if (config == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("userName", config.getUsername())
            .add("name", config.getName())
            .add("identifier", config.getIdentifier())
            .add("bridgeId", config.getBridgeID())
            .add("macAddress", config.getMacAddress())
            .add("ipAddress", config.getIpAddress())
            .add("dhcpEnabled", config.getDhcpEnabled())
            .add("netMask", config.getNetmask())
            .add("gateway", config.getGateway())
            .add("proxy", config.getProxy())
            .add("proxyPort", config.getProxyPort())
            .add("replacesBridgeId", config.getReplacesBridgeId())
            .add("factoryNew", config.getFactoryNew())
            .add("touchLink", config.getTouchlink())
            .add("zigbeeChannel", config.getZigbeeChannel())
            .add("modelId", config.getModelId())
            .add("isReboot", config.isReboot())
            .add("apiVersion", config.getAPIVersion())
            .add("softwareVersion", config.getSoftwareVersion())
            .add("softwareStatus", toString(config.getSoftwareStatus()))
            .add("checkForUpdate", config.getCheckForUpdate())
            .add("time", config.getTime())
            .add("timeZone", config.getTimeZone())
            .add("localTime", config.getLocalTime())
            .add("portalServicesEnabled", config.getPortalServicesEnabled())
            .add("portalState", toString(config.getPortalState()))
            .add("backups", toString(config.getBackup())) // *
            .add("whiteListEntries", firstNonNull(config.getWhiteListEntries(), ImmutableList.<PHWhiteListEntry>of())
                .stream()
                .map(HueUtils::toString)
                .collect(toList()))
            .toString();
    }

    private static String toString(PHSoftwareUpdateStatus status ) {
        ToStringHelper helper = toStringHelper(PHSoftwareUpdateStatus.class);

        if (status == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("state", status.getState())
            .add("releaseNotesUrl", status.getReleaseNotesUrl())
            .add("updateText", status.getUpdateText())
            .add("isNotify", status.isNotify())
            .add("isUpdateAvailable", status.isSoftwareUpdateAvailable())
            .add("deviceTypes", toString(status.getDeviceTypes()))
            .toString();

    }

    private static String toString(PHSoftwareUpdateDeviceTypes types) {
        ToStringHelper helper = toStringHelper(PHSoftwareUpdateDeviceTypes.class);

        if (types == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("batteryLights", types.getBatteryLights())
            .add("batterySensors", types.getBatterySensors())
            .add("mainsLights", types.getMainsLights())
            .add("mainsSensors", types.getMainsSensors())
            .add("slowSensors", types.getSlowSensors())
            .toString();

    }

    public static String toString(PHWhiteListEntry entry) {
        ToStringHelper helper = toStringHelper(PHWhiteListEntry.class);

        if (entry == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("appName", entry.getAppName())
            .add("deviceName", entry.getDeviceName())
            .add("userName", entry.getUserName())
            .toString();
    }

    public static String toString(PHPortalState state) {
        ToStringHelper helper = toStringHelper(PHPortalState.class);

        if (state == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("incoming", state.getIncoming())
            .add("outgoing", state.getOutgoing())
            .add("signedOn", state.getSignedOn())
            .add("communicationState", state.getCommunication())
            .toString();
    }

    public static String toString(PHBackup backup) {
        ToStringHelper helper = toStringHelper(PHBackup.class);

        if (backup == null) {
            return helper.addValue(null).toString();
        }

        return helper
            .omitNullValues()
            .add("status", backup.getStatus())
            .add("errorCode", backup.getErrorCode())
            .toString();
    }

    public static String translateMessageCode(int i) {
        String type = intToMessageType.get(i);
        return type != null ? type : "<UNKNOWN>";
    }
}
