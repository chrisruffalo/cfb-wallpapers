package com.github.chrisruffalo.cfb.wallpapers;

import com.beust.jcommander.JCommander;
import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.load.SchoolYamlLoader;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p></p>
 *
 */
public class Generator {

    public static final Map<String, String> CONFERENCE_MAP = new HashMap<>();

    // just init the map
    private static void loadConferenceMap() {
        // info here: https://en.wikipedia.org/wiki/List_of_NCAA_conferences

        //fbs
        CONFERENCE_MAP.put("aac", "American Athletic Conference");
        CONFERENCE_MAP.put("acc", "Atlantic Coast Conference");
        CONFERENCE_MAP.put("b1g", "Big Ten Conference");
        CONFERENCE_MAP.put("big12", "Big 12 Conference");
        CONFERENCE_MAP.put("cusa", "Conference USA");
        CONFERENCE_MAP.put("independent", "Independent");
        CONFERENCE_MAP.put("mac", "Mid-American Conference");
        CONFERENCE_MAP.put("mw", "Mountain West Conference");
        CONFERENCE_MAP.put("pac", "Pac-12 Conference");
        CONFERENCE_MAP.put("sec", "Southeastern Conference");
        CONFERENCE_MAP.put("sunbelt", "Sun Belt Conference");

        //fcs
        CONFERENCE_MAP.put("sky", "Big Sky Conference");
        CONFERENCE_MAP.put("south", "Big South Conference");
        CONFERENCE_MAP.put("caa", "Colonial Athletic Association");
        // todo: fcs independents goes here
        CONFERENCE_MAP.put("ivy", "Ivy League");
        CONFERENCE_MAP.put("meac", "Mid-Eastern Athletic Conference");
        CONFERENCE_MAP.put("mvfc", "Missouri Valley Football Conference");
        CONFERENCE_MAP.put("nec", "Northeast Conference");
        CONFERENCE_MAP.put("ovc", "Ohio Valley Conference");
        CONFERENCE_MAP.put("patriot", "Patriot League");
        CONFERENCE_MAP.put("pfl", "Pioneer Football League");
        CONFERENCE_MAP.put("socon", "Southern Conference");
        CONFERENCE_MAP.put("southland", "Southland Conference");
        CONFERENCE_MAP.put("swac", "Southwestern Athletic Conference");
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
        final LinkedList<School> allSchools = new LinkedList<>();
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

            // skip non-fbs schools if fbs only
            if(options.isFbsOnly() && !school.isFbs()) {
                continue;
            }

            // skip conference if it is not in the specified set of conferences
            if(!options.getConferences().isEmpty() && !options.getConferences().contains(school.getConference())) {
                continue;
            }

            // if schools were given and school is not in the list of given schools then skip it
            if(!options.getSchools().isEmpty() && !options.getSchools().contains(school.getId())) {
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
            allSchools.add(school);
        }

        // fbs schools
        if(!options.isFbsOnly()) {
            System.out.println("===== FBS =====");
        }
        handleSchoolMap(fbsSchoolsByConference, options, outputPath);
        if(!fcsSchoolsByConference.isEmpty()) {
            System.out.println("===== FCS =====");
            handleSchoolMap(fcsSchoolsByConference, options, outputPath);
        }

        // if web, tie together with static web content
        if(options.isGenerateWeb()) {
            System.out.println("===== Web =====");
            System.out.printf("Generating web resources... ");
            StaticResourceGenerator.generateStaticResources(outputPath, allSchools, fbsSchoolsByConference, fcsSchoolsByConference, options);
            System.out.printf("[DONE]");
        }
    }

    private static void handleSchoolMap(Map<String, List<School>> conferenceMap, GeneratorOptions options, Path outputPath) {
        if(conferenceMap.isEmpty()) {
            return;
        }

        // get list of conferences and sort them
        final List<String> conferences = new ArrayList<>(conferenceMap.keySet());
        Collections.sort(conferences);

        // for each conference get the list of schools and then handle them
        for(final String conference : conferences) {

            // conference names to readable names
            System.out.printf("::: %s (id='%s')\n", CONFERENCE_MAP.get(conference), conference);

            // get schools
            final List<School> schools = conferenceMap.get(conference);
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
                    final SVGSchoolRasterizer rasterizer = new SVGSchoolRasterizer(school, outputPath);
                    rasterizer.raster();

                    // generate web resources if required
                    if(options.isGenerateWeb()) {
                        final SchoolPageGenerator schoolPageGenerator = new SchoolPageGenerator(school);
                        schoolPageGenerator.generate();
                    }

                    System.out.printf("[DONE]\n");
                }
            }
        }
    }
}
