package com.ryonday.automation.twitch.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.awt.*;

import static com.google.common.base.Strings.isNullOrEmpty;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database column
     */
    @Override
    public String convertToDatabaseColumn(Color attribute) {
        if( attribute == null ) {
            return null;
        }
        return "#" + Integer.toHexString( attribute.getRGB()).toUpperCase();
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct dbData type for the corresponding column
     * for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param dbData the data from the database column to be converted
     * @return the converted value to be stored in the entity attribute
     */
    @Override
    public Color convertToEntityAttribute(String dbData) {
        if( isNullOrEmpty( dbData )) {
            return null;
        }
        return Color.decode( dbData );
    }
}
