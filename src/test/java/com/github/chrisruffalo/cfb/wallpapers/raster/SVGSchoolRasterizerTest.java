package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.Division;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputFormat;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.model.Template;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * <p></p>
 *
 */
public class SVGSchoolRasterizerTest {

    @Test
    public void testRaster() {
        // load school
        final SchoolYamlLoader loader = new SchoolYamlLoader();
        final Division division = new Division();
        division.setId("fcs");
        division.setName("FCS");

        // school
        final School school = loader.load(division, "socon", ResourceLoader.loadResource("schools/fcs/socon/thecitadel.yml"));

        // create rasterizer for school with base output path in test area
        final SVGSchoolRasterizer rasterizer = new SVGSchoolRasterizer("fcs", "socon", school, Paths.get("target/test/output"), new GeneratorOptions());

        // output targets
        final OutputTarget target = new OutputTarget("desktop", "templates");
        target.getTemplates().add(new Template(target, "slant", "slant test"));
        target.getFormats().add(new OutputFormat("small", 200, 200));

        // rasterize
        rasterizer.raster(Collections.singletonList(target));

        // assert files exist
        Assert.assertTrue(Files.exists(Paths.get("target/test/output/fcs/socon/thecitadel/desktop/thecitadel-desktop-slant-basic_small.png")));
        Assert.assertTrue(Files.exists(Paths.get("target/test/output/fcs/socon/thecitadel/svg/thecitadel-desktop-slant-basic.svg")));
    }

}
