package com.github.chrisruffalo.cfb.wallpapers.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p></p>
 *
 */
public class ColorUtilTest {

    @Test
    public void testTextColor() {
        Assert.assertEquals("#ffffff", ColorUtil.getTextColor("#000000"));
        Assert.assertEquals("#000000", ColorUtil.getTextColor("#ffffff"));
    }

}
