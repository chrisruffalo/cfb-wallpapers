package com.github.chrisruffalo.cfb.wallpapers.model;

/**
 * <p></p>
 *
 */
public class Color {

    private int red;
    private int green;
    private int blue;

    public Color() {
        this(0,0,0);
    }

    private Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public double getLuminance() {
        // luminance and stuff from https://stackoverflow.com/a/1855903
        // this is basically a "read" on the ratio of colors to determine
        // what the human eye sees as 'bright' that takes green into account
        // as the 'brightest' color making it the most part of the ratio
        return (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue) / 255.0;
    }

    public String getTextColor() {
        // bright color
        if(this.getLuminance() > 0.5) {
            return "#000000";
        }
        // dark color
        return "#ffffff";
    }

    public String toHex() {
        return String.format("#%02x%02x%02x", this.red, this.green, this.blue);
    }

    public String toRgbA(final String alpha) {
        return String.format("rgba(%s,%s,%s,%s)", this.red, this.green, this.blue, alpha);
    }

    public String toRgb() {
        return String.format("rgb(%s,%s,%s)", this.red, this.green, this.blue);
    }

    @Override
    public String toString() {
        return this.toHex();
    }

    public static Color fromHex(final String hex) {
        // get color strings
        final String red = hex.substring(1,3);
        final String grn = hex.substring(3,5);
        final String blu = hex.substring(5,7);

        // parse hex
        final int r = Integer.parseInt(red, 16);
        final int g = Integer.parseInt(grn, 16);
        final int b = Integer.parseInt(blu, 16);

        // create color
        return new Color(r, g, b);
    }

    public static Color fromRgb(final int red, final int green, final int blue) {
        return new Color(red, green, blue);
    }

    public static Color fromRgb(final String rgbString) {
        // convert rgb to integers and make awt color do the work
        final String[] rgbSplit = rgbString.substring(4, rgbString.length() - 1).split(",");

        // get hex color from split
        return new Color(Integer.parseInt(rgbSplit[0]), Integer.parseInt(rgbSplit[1]), Integer.parseInt(rgbSplit[2]));
    }

    public static Color fromCmyk(final String cmykString) {
        // split, just like rgb
        final String[] cmykSplit = cmykString.substring(5, cmykString.length() - 1).split(",");
        // parse
        final double c = Double.parseDouble(cmykSplit[0]) / 100.0;
        final double m = Double.parseDouble(cmykSplit[1]) / 100.0;
        final double y = Double.parseDouble(cmykSplit[2]) / 100.0;
        final double k = Double.parseDouble(cmykSplit[3]) / 100.0;
        // maths
        final int r = (int)(255.0 * (1.0 - c) * (1.0 - k));
        final int g = (int)(255.0 * (1.0 - m) * (1.0 - k));
        final int b = (int)(255.0 * (1.0 - y) * (1.0 - k));
        // return
        return new Color(r, g, b);
    }

    public static Color parse(final String input) {
        if(input == null || input.isEmpty()) {
            return null;
        }

        if(input.startsWith("#")) {
            return Color.fromHex(input);
        } else if(input.startsWith("rgb")) {
            return Color.fromRgb(input);
        } else if(input.startsWith("cmyk")) {
            return Color.fromCmyk(input);
        } else {
            throw new RuntimeException("Could not parse color input: '" + input + "'");
        }
    }

    public static Color darker(final Color c1, final Color c2) {
        if(c1.getLuminance() > c2.getLuminance()) {
            return c2;
        }
        return c1;
    }

    public static Color lighter(final Color c1, final Color c2) {
        if(c1.getLuminance() < c2.getLuminance()) {
            return c2;
        }
        return c1;
    }
}
