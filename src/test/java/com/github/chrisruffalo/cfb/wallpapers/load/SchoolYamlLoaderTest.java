package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * <p></p>
 *
 */
public class SchoolYamlLoaderTest {

    @Test
    public void testLoadCitadel() {

        final SchoolYamlLoader loader = new SchoolYamlLoader();
        final School cid = loader.load(ResourceLoader.loadResource("schools/fcs/socon/thecitadel.yml"));

        // basic test
        Assert.assertEquals("Loaded correct id", "thecitadel", cid.getId());

        // color test
        final List<ColorSet> colorSetList = cid.getColors();
        Assert.assertFalse(colorSetList.isEmpty());
        final ColorSet primary = colorSetList.get(0);
        Assert.assertEquals("#4d90cd", primary.getPrimaryColor());
        Assert.assertEquals("#003263", primary.getSecondaryColor());
        Assert.assertEquals("#ffffff", primary.getAccentColor());

    }

}
