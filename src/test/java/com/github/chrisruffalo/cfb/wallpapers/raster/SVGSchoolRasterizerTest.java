package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p></p>
 *
 */
public class SVGSchoolRasterizerTest {

    @Test
    public void testRaster() {
        // load school
        final SchoolYamlLoader loader = new SchoolYamlLoader();
        final School school = loader.load("schools/fcs/socon/thecitadel.yml", ResourceLoader.loadResource("schools/fcs/socon/thecitadel.yml"));

        // create rasterizer for school with base output path in test area
        final SVGSchoolRasterizer rasterizer = new SVGSchoolRasterizer(school, Paths.get("target/test/output"));

        // rasterize
        rasterizer.raster();

        // assert files exist
        Assert.assertTrue(Files.exists(Paths.get("target/test/output/socon/thecitadel/thecitadel-inset_stripe-basic_1920_1080.png")));
        Assert.assertTrue(Files.exists(Paths.get("target/test/output/socon/thecitadel/thecitadel-two_stripes-basic.svg")));
    }

}
