package com.github.chrisruffalo.cfb.wallpapers.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p></p>
 *
 */
public class ColorTest {

    @Test
    public void testHex() {
        Color hex = Color.parse("#ffffff");
        Assert.assertEquals(255, hex.getRed());
        Assert.assertEquals(255, hex.getBlue());
        Assert.assertEquals(255, hex.getGreen());

        Assert.assertEquals("#000000", hex.getTextColor());

        // clemson, where this bug was first visible
        hex = Color.parse("#f66733");
        Assert.assertEquals("#f66733", hex.toString());
    }

    @Test
    public void testRgb() {
        Color rgb = Color.parse("rgb(12,34,19)");
        Assert.assertEquals(12, rgb.getRed());
        Assert.assertEquals(34, rgb.getGreen());
        Assert.assertEquals(19, rgb.getBlue());
        Assert.assertEquals("#ffffff", rgb.getTextColor());

        rgb = Color.parse("rgb(129,0,42)");
        Assert.assertEquals(129, rgb.getRed());
        Assert.assertEquals(0, rgb.getGreen());
        Assert.assertEquals(42, rgb.getBlue());
    }

    @Test
    public void testRgbAOutput() {
        final Color rgb = Color.fromRgb(175, 200, 100);
        Assert.assertEquals(rgb.toRgbA("1.0"), "rgba(175,200,100,1.0)");
    }

    @Test
    public void testCmyk() {
        Color cmyk = Color.parse("cmyk(0, 0, 0, 100)");
        Assert.assertEquals(cmyk.toHex(), "#000000");

        cmyk = Color.parse("cmyk(0, 0, 0, 0)");
        Assert.assertEquals(cmyk.toHex(), "#ffffff");

        cmyk = Color.parse("cmyk(0, 100, 100, 0)");
        Assert.assertEquals(cmyk.toHex(), "#ff0000");

        cmyk = Color.parse("cmyk(100, 0, 100, 0)");
        Assert.assertEquals(cmyk.toHex(), "#00ff00");

        cmyk = Color.parse("cmyk(100, 100, 0, 0)");
        Assert.assertEquals(cmyk.toHex(), "#0000ff");

        cmyk = Color.parse("cmyk(0, 0, 100, 0)");
        Assert.assertEquals(cmyk.toHex(), "#ffff00");

        cmyk = Color.parse("cmyk(100, 0, 0, 0)");
        Assert.assertEquals(cmyk.toHex(), "#00ffff");

        cmyk = Color.parse("cmyk(0, 100, 0, 0)");
        Assert.assertEquals(cmyk.toHex(), "#ff00ff");
    }

}
