package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.Divisions;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p></p>
 *
 */
public class DivisionYamlLoaderTest {

    @Test
    public void testDivisionLoading() {
        // load sorted
        final Divisions divisions = DivisionYamlLoader.loadDivisions("schools/divisions.yml");

        // no empty division structure
        Assert.assertFalse(divisions.getDivisions().isEmpty());

        // fcs and fbs are there
        Assert.assertTrue(divisions.getDivisions().containsKey("fbs"));
        Assert.assertTrue(divisions.getDivisions().containsKey("fcs"));

        // and have values as expected
        Assert.assertTrue(divisions.getDivisions().get("fbs").getConferenceMap().containsKey("sec"));
        Assert.assertTrue(divisions.getDivisions().get("fcs").getConferenceMap().containsKey("socon"));
    }

}
