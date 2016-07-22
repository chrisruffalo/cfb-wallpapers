package com.github.chrisruffalo.cfb.wallpapers.transform;

import com.github.chrisruffalo.cfb.wallpapers.config.CfbWallpapersConfig;
import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public class SVGColorChange {

    public String transform(final String xmlSvgInput, final ColorSet transformTo) {
        // transform colors
        String output = xmlSvgInput.replaceAll(CfbWallpapersConfig.INSTANCE.templatePrimary(), transformTo.getPrimaryColor());
        output = output.replaceAll(CfbWallpapersConfig.INSTANCE.templateSecondary(), transformTo.getSecondaryColor());
        output = output.replaceAll(CfbWallpapersConfig.INSTANCE.templateAccent(), transformTo.getAccentColor());

        // return
        return output;
    }

}
