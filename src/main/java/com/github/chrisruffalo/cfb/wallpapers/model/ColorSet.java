package com.github.chrisruffalo.cfb.wallpapers.model;

import com.github.chrisruffalo.cfb.wallpapers.util.ColorUtil;

/**
 * <p></p>
 *
 */
public class ColorSet {

    private String id;

    private String primaryColor;
    private String secondaryColor;
    private String accentColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public String getPrimaryTextColor() {
        return ColorUtil.getTextColor(this.getPrimaryColor());
    }

    public String getSecondaryTextColor() {
        return ColorUtil.getTextColor(this.getSecondaryColor());
    }

    public String getAccentTextColor() {
        return ColorUtil.getTextColor(this.getAccentColor());
    }
 }
