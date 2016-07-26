package com.github.chrisruffalo.cfb.wallpapers.util;

    /**
 * <p></p>
 *
 */
public class ColorUtil {

    public static String getTextColor(final String backgroundHexColor) {
        // get color strings
        final String red = backgroundHexColor.substring(1,3);
        final String grn = backgroundHexColor.substring(3,5);
        final String blu = backgroundHexColor.substring(4,6);

        // parse hex
        final int r = Integer.parseInt(red, 16);
        final int g = Integer.parseInt(grn, 16);
        final int b = Integer.parseInt(blu, 16);

        // luminance and stuff from https://stackoverflow.com/a/1855903
        double lum = 1 - ( 0.299 * r + 0.587 * g + 0.114 * b) / 255.0;

        // check luminance for dark or light and return appropriate font
        if(lum < 0.5) {
            return "#000000";
        }

        return "#ffffff";
    }
}
