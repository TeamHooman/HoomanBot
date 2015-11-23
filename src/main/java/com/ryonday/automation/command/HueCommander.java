package com.ryonday.automation.command;


import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
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
        return hueSdk
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
    }

    private Optional<PHLight> lightForName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        Map<String, PHLight> wat = getLights();
        logger.info("Lights: {}", wat);

        return Optional.ofNullable(wat.get(name));
    }

    private void updateLight(PHLight light, PHLightState state) {
        hueSdk.getSelectedBridge().updateLightState(light, state);
    }

    private void updateLight(String name, PHLightState state) {

        if (state == null) {
            logger.warn("Attempt to set null status for light:\n\t" +
                "Light name:    {}\n\t" +
                "Desired state: {}", name, state);
        }

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
            logger.warn("Attempt to update unrecognized light:\n\t" +
                "Light name:    {}", name);
            return;
        }

        PHLight light = lightOpt.get();

        updateLight(light, mutator.apply(light));
    }

    public void color(String name, Color color) {

        if (color == null) {
            logger.warn("requested color change for bulb '{}' with non-existent color.");
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

    public void brighter(String name) {

    }

    public void darker(String name) {
    }

    public void cycle(String name) {

    }

}
