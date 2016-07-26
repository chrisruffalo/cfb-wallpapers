package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 */
public class StaticResourceGeneratorTest {

    private static final String OUTPUT_PATH = "target/test/output";

    @Test
    public void testResourceGeneration() {
        // create school(s)
        final School school1 = new SchoolYamlLoader().load("schools/fcs/socon/thecitadel.yml", ResourceLoader.loadResource("schools/fcs/socon/thecitadel.yml"));
        final School school2 = new SchoolYamlLoader().load("schools/fcs/socon/vmi.yml", ResourceLoader.loadResource("schools/fcs/socon/vmi.yml"));

        // add to list
        final List<School> schools = new ArrayList<>(2);
        schools.add(school1);
        schools.add(school2);

        // path
        final Path path = Paths.get(OUTPUT_PATH).toAbsolutePath().normalize();

        // generate static resources
        StaticResourceGenerator.generateCss(path, schools);

        // assert file is there
        Assert.assertTrue(Files.isRegularFile(path.resolve("resources").resolve("css").resolve("cfb.css")));
    }


}
