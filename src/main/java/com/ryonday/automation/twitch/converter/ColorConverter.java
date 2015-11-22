package com.ryonday.automation.twitch.converter;

import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.google.common.base.Strings.isNullOrEmpty;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    private final static Logger logger = LoggerFactory.getLogger(ColorConverter.class);

    @Override
    public String convertToDatabaseColumn(Color color) {
        if (color == null) {
            logger.warn("Cannot convert null color.");
            return null;
        }

        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }

    @Override
    public Color convertToEntityAttribute(String colorString) {
        if (isNullOrEmpty(colorString)) {
            return null;
        }

        try {
            return Color.web(colorString);
        } catch( Exception ex ) {
            logger.error("Could not parse color String from database: {}", colorString );
            return Color.BLACK;
        }
    }
}
