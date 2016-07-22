package com.github.chrisruffalo.cfb.wallpapers.transform;

import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public class SVGColorChangeTest {

    @Test
    public void testChangeSimpleSVGFile() {
        // input
        final String svgInput = ResourceLoader.loadResourceAsString("templates/inset_stripe.svg");
        // colorset
        final ColorSet set = new ColorSet();
        set.setPrimaryColor("#eeeeee");
        set.setSecondaryColor("#bbbbbb");
        set.setAccentColor("#dddddd");
        // convert
        final SVGColorChange changer = new SVGColorChange();
        final String svgOutput = changer.transform(svgInput, set);
        // test
        Assert.assertTrue("Primary color converted", svgOutput.contains("#eeeeee"));
        Assert.assertTrue("Secondary color converted", svgOutput.contains("#bbbbbb"));
        Assert.assertTrue("Accent color converted", svgOutput.contains("#dddddd"));
    }

}
