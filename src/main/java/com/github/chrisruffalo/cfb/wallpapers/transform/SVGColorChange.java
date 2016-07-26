package com.github.chrisruffalo.cfb.wallpapers.transform;

import com.github.chrisruffalo.cfb.wallpapers.config.CfbWallpapersConfig;
import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;

/**
 * <p></p>
 *
 */
public class SVGColorChange {

    public String transform(final String xmlSvgInput, final ColorSet transformTo) {
        // transform colors
        String output = xmlSvgInput.replaceAll(CfbWallpapersConfig.INSTANCE.templatePrimary(), transformTo.getPrimaryColor());
        output = output.replaceAll(CfbWallpapersConfig.INSTANCE.templateSecondary(), transformTo.getSecondaryColor());

        // if the text to output contains the accent template but it isn't provided then the school doesn't have enough colors to do this template
        // and we should move on.
        if(output.contains(CfbWallpapersConfig.INSTANCE.templateAccent()) && transformTo.getAccentColor() == null) {
            return null;
        }

        output = output.replaceAll(CfbWallpapersConfig.INSTANCE.templateAccent(), transformTo.getAccentColor());

        // return
        return output;
    }

}
