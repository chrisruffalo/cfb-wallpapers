package com.github.chrisruffalo.cfb.wallpapers.transform;

import com.github.chrisruffalo.cfb.wallpapers.config.InternalConfig;
import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;

/**
 * <p></p>
 *
 */
public class SVGColorChange {

    public String transform(final String xmlSvgInput, final ColorSet transformTo) {
        // transform colors
        String output = xmlSvgInput.replaceAll(InternalConfig.INSTANCE.templatePrimary(), transformTo.getPrimaryColor().toHex());
        output = output.replaceAll(InternalConfig.INSTANCE.templateSecondary(), transformTo.getSecondaryColor().toHex());

        // if the text to output contains the accent template but it isn't provided then the school doesn't have enough colors to do this template
        // and we should move on.
        if(output.contains(InternalConfig.INSTANCE.templateAccent()) && transformTo.getAccentColor() == null) {
            return null;
        } else if(transformTo.getAccentColor() != null) {
            output = output.replaceAll(InternalConfig.INSTANCE.templateAccent(), transformTo.getAccentColor().toHex());
        }

        // return
        return output;
    }

}
