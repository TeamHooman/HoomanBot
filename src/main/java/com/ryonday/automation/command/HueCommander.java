package com.ryonday.automation.command;


import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.ryonday.automation.hue.utils.HueUtils;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Service
public class HueCommander {

    private final static Logger logger = LoggerFactory.getLogger(HueCommander.class);
    private final PHHueSDK hueSdk;

    private final static CharMatcher wsMatcher = CharMatcher.WHITESPACE;

    @Autowired
    public HueCommander(PHHueSDK hueSdk) {
        Preconditions.checkNotNull(this.hueSdk = hueSdk);
    }

    private Map<String, PHLight> getLights() {
        Map<String, PHLight> lights =  hueSdk
            .getSelectedBridge()
            .getResourceCache()
            .getAllLights()
            .stream()
//            .peek(e -> logger.debug("Light: {}/{}", e.getKey(), e.getValue()))
            .collect(
                collectingAndThen(
                    toMap(light -> wsMatcher.removeFrom(light.getName()).toLowerCase(),
                        light -> light),
                    ImmutableMap::copyOf)
            );
        logger.debug("Available Lights: {}", lights);
        return lights;
    }

    private Optional<PHLight> lightForName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        Map<String, PHLight> lights = getLights();

        return Optional.ofNullable(lights.get(name));
    }

    private void updateLight(PHLight light, PHLightState state) {
        if (state == null) {
            logger.warn("Attempt to set null status for light '{}'", HueUtils.toString(light));
            return;
        }

        logger.info("WTFFFFF");

        hueSdk.getSelectedBridge().updateLightState(light, state);
    }

    private void updateLight(String name, PHLightState state) {
        Optional<PHLight> lightOpt = lightForName(name);

        if (!lightOpt.isPresent()) {
            logger.warn("Attempt to set status for unrecognized light:\n\t" +
                "Light name:    {}\n\t" +
                "Desired state: {} ", name, state);
            return;
        }

        PHLight light = lightOpt.get();

        updateLight(light, state);
    }

    private void updateLight(String name, Function<PHLight, PHLightState> mutator) {
        Optional<PHLight> lightOpt = lightForName(name);

        if (!lightOpt.isPresent()) {
            logger.warn("Attempt to update unrecognized light '{}'.", name);
            return;
        }

        PHLight light = lightOpt.get();

        updateLight(light, mutator.apply(light));
    }

    public void color(String name, Color color) {

        if (color == null) {
            logger.warn("requested null color change for bulb '{}'.", name);
            return;
        }

        logger.info("Color change requested:\n\t" +
            "Light name:    {}\n\t" +
            "Desired color: {}", name, color);

        updateLight(name, l -> {
            String modelNumber = l.getModelNumber();

            int red = (int) (color.getRed() * 255.0);
            int green = (int) (color.getGreen() * 255.0);
            int blue = (int) (color.getBlue() * 255.0);

            float[] xy = PHUtilities.calculateXYFromRGB(red, green, blue, modelNumber);

            PHLightState state = new PHLightState();
            state.setX(xy[0]);
            state.setY(xy[1]);
            state.setEffectMode(PHLight.PHLightEffectMode.EFFECT_NONE);
            return state;
        });
    }

    public void off(String name) {

        PHLightState state = new PHLightState();
        state.setOn(false);
        updateLight(name, state);

    }

    public void on(String name) {

        PHLightState state = new PHLightState();
        state.setOn(true);
        updateLight(name, state);

    }

    public void brightness( String name, int brightness ) {
        logger.info("Brightness change requested:\n\t" +
            "Light:      {}\n\t" +
            "Brightness: {}", name, brightness);
        updateLight(name, l -> {
            PHLightState currentState = l.getLastKnownLightState();
            currentState.setBrightness( brightness, true);
            return currentState;
        });
    }

    private void incrementBrightness( String name, int increment ) {

        updateLight(name, l -> {
            PHLightState currentState = l.getLastKnownLightState();
            int currentBrightness = currentState.getBrightness();
            int newBrightness = currentBrightness + increment;
            logger.info("Brightness increment requested:\n\t" +
                "Light:              {}\n\t" +
                "Increment:          {}\n\t" +
                "Current Brightness: {}\n\t" +
                "Desired Brightness: {}", name, increment, currentBrightness, newBrightness);
            currentState.setBrightness( (currentState.getBrightness() + increment), true);
            return currentState;
        });
    }
    public void brighter(String name) {
        incrementBrightness( name, 50);
    }

    public void darker(String name) {
        incrementBrightness( name, -50);
    }

    public void cycle(String name) {
        logger.info("Color Loop requested for light '{}'.", name);
        updateLight(name, l -> {
            String modelNumber = l.getModelNumber();
            PHLightState currentState = l.getLastKnownLightState();
            currentState.setEffectMode(PHLight.PHLightEffectMode.EFFECT_COLORLOOP);
            return currentState;
        });
    }

}
