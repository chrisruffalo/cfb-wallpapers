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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p></p>
 *
 */
public class Generator {

    private static final Map<String, String> CONFERENCE_MAP = new HashMap<>();

    // just init the map
    private static void loadConferenceMap() {
        // info here: https://en.wikipedia.org/wiki/List_of_NCAA_conferences

        //fbs
        CONFERENCE_MAP.put("aac", "American Athletic");
        CONFERENCE_MAP.put("acc", "Atlantic Coast");
        CONFERENCE_MAP.put("b1g", "Big Ten");
        CONFERENCE_MAP.put("big12", "Big 12");
        CONFERENCE_MAP.put("independent", "Independent");
        CONFERENCE_MAP.put("sec", "Southeastern");
        CONFERENCE_MAP.put("sunbelt", "Sun Belt");

        //fcs
        CONFERENCE_MAP.put("socon", "Southern Conference");
    }

    public static void main(String[] args) {
        // load conference map
        loadConferenceMap();

        // parse arguments
        final GeneratorOptions options = new GeneratorOptions();
        final JCommander commander = new JCommander(options, args);

        // use arguments
        if(options.isHelp()) {
            commander.usage();
            return;
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

        // save schools into map by conference / bowl status
        final Map<String, List<School>> fbsSchoolsByConference = new HashMap<>();
        final Map<String, List<School>> fcsSchoolsByConference = new HashMap<>();

        // do loop
        for(final String resource : schoolResources) {
            // get resource
            final InputStream stream = ResourceLoader.loadResource(resource);
            if(stream == null) {
                continue;
            }

            // load yaml
            final School school = loader.load(resource, stream);
            if(school == null || school.getId() == null || school.getId().isEmpty()) {
                continue;
            }

            // sort into bowl status -> conference -> school
            Map<String, List<School>> byConf = school.isFbs() ? fbsSchoolsByConference : fcsSchoolsByConference;
            final String conference = school.getConference();
            List<School> schools = byConf.get(conference);
            if(schools == null) {
                schools = new ArrayList<>(10);
                byConf.put(conference, schools);
            }
            schools.add(school);
        }

        // fbs schools
        handleSchoolMap(fbsSchoolsByConference, options, outputPath);
        if(!options.isFbsOnly()) {
            handleSchoolMap(fcsSchoolsByConference, options, outputPath);
        }
    }

    private static void handleSchoolMap(Map<String, List<School>> conferenceMap, GeneratorOptions options, Path outputPath) {
        if(conferenceMap.isEmpty()) {
            return;
        }

        final String prefix = options.isList() ? "" : "Conference :: ";

        for(final Map.Entry<String, List<School>> conferenceEntry : conferenceMap.entrySet()) {
            final String conference = conferenceEntry.getKey();

            // skip conference if it is not in the specified set of conferences
            if(!options.getConferences().isEmpty() && !options.getConferences().contains(conference)) {
                continue;
            }

            // todo: map to map conference names to big names
            System.out.printf("%s%s (id='%s')\n", prefix, CONFERENCE_MAP.get(conference), conference);

            // get schools
            final List<School> schools = conferenceEntry.getValue();

            // sort list of schools by name
            Collections.sort(schools);

            // handle each school
            for(final School school : schools) {

                // if schools were given and school is not in the list of given schools then skip it
                if(!options.getSchools().isEmpty() && !options.getSchools().contains(school.getId())) {
                    continue;
                }

                if(options.isList()) {
                    System.out.printf("- %s (id='%s')\n", school.getName(), school.getId(), school.getConference());
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
}
