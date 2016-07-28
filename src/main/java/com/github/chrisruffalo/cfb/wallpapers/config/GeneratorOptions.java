package com.github.chrisruffalo.cfb.wallpapers.config;

import com.beust.jcommander.Parameter;

import java.util.HashSet;
import java.util.Set;

/**
 * <p></p>
 *
 */
public class GeneratorOptions {

    @Parameter(names = {"--output", "-o"}, description = "The path to the output folder where wallpapers should be saved.")
    private String outputPath = "wallpaper";

    @Parameter(names = {"--school", "-s"}, description = "A school id to be included in the output. By default all schools are executed.")
    private Set<String> schools = new HashSet<>(0);

    @Parameter(names = {"--conference", "-c"}, description = "A conference id to be included in the output. Will output all schools in that conference (unless schools are specified otherwise).")
    private Set<String> conferences = new HashSet<>(0);

    @Parameter(names = {"--division", "-d"}, description = "A division id to be included in the output. Will output all conferences and schools in that division (unless schools/conferences are specified).")
    private Set<String> divisions = new HashSet<>(0);

    @Parameter(names = {"--clean"}, description = "Deletes the contents of the output directory before starting to generate new wallpapers.")
    private boolean clean = false;

    @Parameter(names = {"--help", "-h", "-?"}, description = "Display the help message")
    private boolean help;

    @Parameter(names = {"--no-images"}, description = "Don't generate images. Useful for testing.")
    private boolean noImages = false;

    @Parameter(names = {"--web", "-w"}, description = "Generate web content (html, links, etc) to go with the generated wallpaper. Default: false")
    private boolean generateWeb = false;

    @Parameter(names = {"--list", "-l"}, description = "List the structure of sorted, conferences, and schools.")
    private boolean list;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Set<String> getSchools() {
        return schools;
    }

    public void setSchools(Set<String> schools) {
        this.schools = schools;
    }

    public Set<String> getDivisions() {
        return divisions;
    }

    public void setDivisions(Set<String> divisions) {
        this.divisions = divisions;
    }

    public Set<String> getConferences() {
        return conferences;
    }

    public void setConferences(Set<String> conferences) {
        this.conferences = conferences;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isGenerateWeb() {
        return generateWeb;
    }

    public void setGenerateWeb(boolean generateWeb) {
        this.generateWeb = generateWeb;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isNoImages() {
        return noImages;
    }

    public void setNoImages(boolean noImages) {
        this.noImages = noImages;
    }
}
