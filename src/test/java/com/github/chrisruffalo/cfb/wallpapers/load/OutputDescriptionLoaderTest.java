package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * <p></p>
 *
 */
public class OutputDescriptionLoaderTest {

    @Test
    public void testOutputDescriptions() {
        // load list
        final List<OutputTarget> targetList = OutputDescriptionLoader.load(ResourceLoader.loadResource("outputs.yml"));

        // assert not empty
        Assert.assertFalse(targetList.isEmpty());
    }

}
