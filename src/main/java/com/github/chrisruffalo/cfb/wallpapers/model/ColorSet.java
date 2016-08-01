package com.github.chrisruffalo.cfb.wallpapers.model;

/**
 * <p></p>
 *
 */
public class ColorSet {

    private String id;

    private Color primaryColor;
    private Color secondaryColor;
    private Color accentColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Color getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
    }

    public Color getBannerColor() {
        // both dark
        if (this.primaryColor.getLuminance() <= 0.5 && this.secondaryColor.getLuminance() <= 0.5) {
            return Color.lighter(this.primaryColor, this.secondaryColor);
        }
        // otherwise just choose the darker color
        return Color.darker(this.primaryColor, this.secondaryColor);
    }
}
