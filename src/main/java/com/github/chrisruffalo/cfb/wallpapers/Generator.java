package com.github.chrisruffalo.cfb.wallpapers;

import com.beust.jcommander.JCommander;
import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.load.DivisionYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
import com.github.chrisruffalo.cfb.wallpapers.model.Division;
import com.github.chrisruffalo.cfb.wallpapers.model.Divisions;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.raster.SVGSchoolRasterizer;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import com.github.chrisruffalo.cfb.wallpapers.web.SchoolPageGenerator;
import com.github.chrisruffalo.cfb.wallpapers.web.StaticResourceGenerator;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

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

        // initial log message
        System.out.println("===== Loading =====");

        // first load sorted and then load schools by division
        final Divisions divisions = DivisionYamlLoader.loadDivisions("schools/divisions.yml");

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

        // load all sorted
        System.out.print("Loading schools... ");
        for(final Division division : divisions.getSortedDivisions()) {
            loadDivisionSchools(division, options);
        }
        System.out.printf("%d [DONE]\n", divisions.allSchools().size());

        // handle output for each division that has content
        for(final Division division : divisions.getSortedDivisions()) {
            System.out.printf("\n===== %s =====\n", division.getName());
            handleDivisionOutput(division, options, outputPath);
        }

        // if web, tie together with static web content
        if(options.isGenerateWeb()) {
            System.out.println("\n===== Web =====");
            System.out.printf("Generating web resources... ");
            StaticResourceGenerator.generateStaticResources(outputPath, divisions, options);
            System.out.printf("[DONE]\n");
        }
    }

    private static void loadDivisionSchools(final Division division, GeneratorOptions options) {
        // don't do anything, makes an empty division
        if(!options.getDivisions().isEmpty() && !options.getDivisions().contains(division.getId())) {
            return;
        }

        // if there are no conferences for this division there is no point in going on
        if(division.getConferenceMap().isEmpty()) {
            return;
        }

        // load each conference
        for(final String conference : division.conferences()) {
            // skip conference if it is not in the specified set of conferences
            if(!options.getConferences().isEmpty() && !options.getConferences().contains(conference)) {
                continue;
            }

            // load schools for conference
            loadConferenceSchools(division, conference, options);
        }
    }

    private static void loadConferenceSchools(final Division division, final String conference, final GeneratorOptions options) {
        // loads all of the resources from the given path
        final List<String> schoolResources = ResourceLoader.loadResourceLocations("schools/" + division.getId() + "/" + conference);

        // yaml loader
        final SchoolYamlLoader loader = new SchoolYamlLoader();

        // load each resource
        for(final String resource : schoolResources) {
            // get resource
            final InputStream stream = ResourceLoader.loadResource(resource);
            if(stream == null) {
                continue;
            }

            // load yaml
            final School school = loader.load(stream);
            if(school == null || school.getId() == null || school.getId().isEmpty()) {
                continue;
            }

            // if schools were given and school is not in the list of given schools then skip it
            if(!options.getSchools().isEmpty() && !options.getSchools().contains(school.getId())) {
                continue;
            }

            // add to division / conference
            division.add(conference, school);
        }
    }

    private static void handleDivisionOutput(Division division, GeneratorOptions options, Path outputPath) {

        // for each conference get the list of schools and then handle them
        for(final String conference : division.conferences()) {

            // conference names to readable names
            System.out.printf("::: %s (id='%s')\n", division.getConferenceName(conference), conference);

            // get schools
            final List<School> schools = division.schools(conference);
            if(schools == null || schools.isEmpty()) {
                continue;
            }

            // sort list of schools by name
            Collections.sort(schools);

            // handle each school
            for(final School school : schools) {

                if(options.isList()) {
                    System.out.printf("\t%s (id='%s')\n", school.getName(), school.getId());
                    continue;
                }

                // rasterize the school
                System.out.printf("\t%s... ", school.display());
                if(school.getColors() == null || school.getColors().isEmpty()) {
                    System.out.printf("[ERROR] (School has 0 color combinations)\n");
                } else {
                    // don't generate images if requested
                    if(!options.isNoImages()) {
                        final SVGSchoolRasterizer rasterizer = new SVGSchoolRasterizer(division.getId(), conference, school, outputPath);
                        rasterizer.raster();
                    }

                    // generate web resources if required
                    if(options.isGenerateWeb()) {
                        final SchoolPageGenerator schoolPageGenerator = new SchoolPageGenerator(division, conference, school);
                        schoolPageGenerator.generate(outputPath);
                    }

                    System.out.printf("[DONE]\n");
                }
            }
        }
    }
}
