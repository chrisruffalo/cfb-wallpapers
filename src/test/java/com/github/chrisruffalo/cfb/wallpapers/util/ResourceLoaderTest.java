package com.github.chrisruffalo.cfb.wallpapers.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public class ResourceLoaderTest {

    @Test
    public void testLoadAllSchools() {
        // load
        final List<String> locations = ResourceLoader.loadAllSchoolResources();

        // asser that schools were loaded
        Assert.assertFalse(locations.isEmpty());

        // test that schools were loaded
        Assert.assertTrue(locations.contains("schools/fcs/socon/thecitadel.yml"));
        Assert.assertTrue(locations.contains("schools/fcs/socon/vmi.yml"));
    }

}
