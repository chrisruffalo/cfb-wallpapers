package com.github.chrisruffalo.cfb.wallpapers;

import com.beust.jcommander.JCommander;
import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.raster.SVGSchoolRasterizer;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p></p>
 *
 */
public class Generator {

    public static void main(String[] args) {

        // parse arguments
        final GeneratorOptions options = new GeneratorOptions();
        final JCommander commander = new JCommander(options, args);

        // use arguments
        if(options.isHelp()) {
            commander.usage();
            return;
        }

        // convert schools to hash set for easy lookup
        final Set<String> schoolIdsFromArguments = new HashSet<>();
        if(!options.getSchools().isEmpty()) {
            schoolIdsFromArguments.addAll(options.getSchools());
        }

        // continue, load all schools
        final List<String> schoolResources = ResourceLoader.loadAllSchoolResources();

        // get output dir
        final Path outputPath = Paths.get(options.getOutputPath()).toAbsolutePath().normalize();
        if(Files.isDirectory(outputPath) && options.isClean()) {
            try {
                System.out.printf("Deleting wallpaper directory `%s`... ", outputPath.toString());
                FileUtils.deleteDirectory(outputPath.toFile());
                System.out.printf("[DONE]\n");
            } catch (IOException e) {
                throw new RuntimeException("Could not delete output directory but `--clean` was specified.", e);
            }
        }
        if(!Files.isDirectory(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // yaml loader
        final SchoolYamlLoader loader = new SchoolYamlLoader();

        // do loop
        for(final String resource : schoolResources) {
            // get resource
            final InputStream stream = ResourceLoader.loadResource(resource);
            if(stream == null) {
                continue;
            }

            // load yaml
            final School school = loader.load(stream);

            if(options.isList()) {
                System.out.printf("- %s (id='%s', conference='%s')\n", school.getName(), school.getId(), school.getConference());
                continue;
            }

            // if schools were given and school is not in the list of given schools then skip it
            if(!schoolIdsFromArguments.isEmpty() && !schoolIdsFromArguments.contains(school.getId())) {
                continue;
            }

            // rasterize the school
            System.out.printf("Generating wallpapers for %s... ", school.display());
            final SVGSchoolRasterizer rasterizer = new SVGSchoolRasterizer(school, outputPath);
            rasterizer.raster();
            System.out.printf("[DONE]\n");
        }
    }
}
