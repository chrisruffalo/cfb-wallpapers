package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 */
public class SchoolYamlLoader {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COLORS = "colors";
    private static final String KEY_CONFERENCE = "conference";
    private static final String KEY_BOWL_STATUS = "fbs";

    private static final String KEY_PRIMARY = "primary";
    private static final String KEY_SECONDARY = "secondary";
    private static final String KEY_ACCENT = "accent";

    @SuppressWarnings({"unchecked"})
    public School load(final InputStream yamlStream) {
        // create empty school object
        final School school = new School();

        // load yaml document
        final Yaml yaml = new Yaml();
        final Map<String, Object> document = (Map<String, Object>) yaml.load(yamlStream);

        // parse basic school details
        school.setId((String)document.get(KEY_ID));
        school.setName((String)document.get(KEY_NAME));
        school.setConference((String)document.get(KEY_CONFERENCE));

        // look for colors
        final Object colorCandidate = document.get(KEY_COLORS);
        if(colorCandidate instanceof List) {
            final List<Map<String, ?>> colorDocument = (List<Map<String, ?>>) colorCandidate;
            school.setColors(this.parseColors(colorDocument));
        } // if it wasn't a list there are no colors defined for the school

        // return parsed school
        return school;
    }

    private List<ColorSet> parseColors(final List<Map<String, ?>> colorDocument) {
        final List<ColorSet> colorSets = new ArrayList<>(colorDocument.size());

        for(Map<String, ?> singleColor : colorDocument) {
            if(singleColor.isEmpty()) {
                continue;
            }

            final ColorSet colorSet = new ColorSet();
            colorSet.setId((String)singleColor.get(KEY_ID));
            colorSet.setPrimaryColor(this.parseColorString((String)singleColor.get(KEY_PRIMARY)));
            colorSet.setSecondaryColor(this.parseColorString((String)singleColor.get(KEY_SECONDARY)));

            if(singleColor.containsKey(KEY_ACCENT)) {
                colorSet.setAccentColor(this.parseColorString((String)singleColor.get(KEY_ACCENT)));
            } else {
                colorSet.setAccentColor(null);
            }

            colorSets.add(colorSet);
        }

        return colorSets;
    }

    private String parseColorString(final String colorStringInput) {
        // don't do anything with null colors.
        if(colorStringInput == null) {
            return null;
        }

        // if hex, leave as hex
        if(colorStringInput.startsWith("#")) {
            return colorStringInput;
        }

        if(colorStringInput.startsWith("rgb(") && colorStringInput.endsWith(")")) {
            // convert rgb to integers and make awt color do the work
            final String[] rgbSplit = colorStringInput.substring(4, colorStringInput.length() - 2).split(",");
            final Color color = new Color(Integer.parseInt(rgbSplit[0]), Integer.parseInt(rgbSplit[1]), Integer.parseInt(rgbSplit[2]));
            // get hex color
            return "#" + Integer.toHexString(color.getRGB()).substring(2);
        }

        // don't do anything but it will probably fail
        return colorStringInput;
    }

}

