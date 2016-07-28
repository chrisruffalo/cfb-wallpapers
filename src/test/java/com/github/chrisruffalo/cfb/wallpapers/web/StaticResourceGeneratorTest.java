package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.load.DivisionYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.Divisions;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p></p>
 *
 */
public class StaticResourceGeneratorTest {

    private static final String OUTPUT_PATH = "target/test/output";

    @Test
    public void testResourceGeneration() {
        final Divisions divisions = DivisionYamlLoader.loadDivisions("schools/divisions.yml");

        // create school(s)
        final School school1 = new SchoolYamlLoader().load(ResourceLoader.loadResource("schools/fcs/socon/thecitadel.yml"));
        final School school2 = new SchoolYamlLoader().load(ResourceLoader.loadResource("schools/fcs/socon/vmi.yml"));

        divisions.getDivisions().get("fcs").add("socon", school1);
        divisions.getDivisions().get("fcs").add("socon", school2);

        // path
        final Path path = Paths.get(OUTPUT_PATH).toAbsolutePath().normalize();

        // options
        final GeneratorOptions options = new GeneratorOptions();
        options.setOutputPath(path.toString());

        // generate static resources
        StaticResourceGenerator.generateStaticResources(path, divisions, options);

        // assert file is there
        Assert.assertTrue(Files.isRegularFile(path.resolve("resources").resolve("css").resolve("cfb.css")));
    }


}
