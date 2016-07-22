package com.github.chrisruffalo.cfb.wallpapers.config;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public class GeneratorOptions {

    @Parameter(names = {"--output", "-o"}, description = "The path to the output folder where wallpapers should be saved.")
    private String outputPath = "wallpaper";

    @Parameter(names = {"--school", "-s"}, description = "A school id to be included in the output. By default all schools are executed.")
    private List<String> schools = new ArrayList<>(0);

    @Parameter(names = {"--size", "-x"}, description = "An output format in the form WxH. Ex: `-x 1280x1024` adds an output mode that is 1280 pixels wide and 1024 pixels tall.")
    private List<String> outputSizes = new ArrayList<>(0);

    @Parameter(names = {"--clean"}, description = "Deletes the contents of the output directory before starting to generate new wallpapers.")
    private boolean clean = false;

    @Parameter(names = {"--no-modes", "-n"}, description = "Ignores any built-in modes in favor of only modes given by the command line")
    private boolean noBuiltInModes = false;

    @Parameter(names = {"--svg", "-i"}, description = "An input SVG file that will be used instead of the built-in templates")
    private String svgFile = null;

    @Parameter(names = {"--school-file", "-f"}, description = "An input school YAML that will be used instead of a built-in YAML file. If no other IDs will be provided this will be the only school that has wallpapers made.")
    private String schoolYamlFile = null;

    @Parameter(names = {"--help", "-h", "-?"}, description = "Display the help message")
    private boolean help;

    @Parameter(names = {"--list-schools"}, description = "Instead of doing anything else, list each school.")
    private boolean list;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public List<String> getOutputSizes() {
        return outputSizes;
    }

    public void setOutputSizes(List<String> outputSizes) {
        this.outputSizes = outputSizes;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public boolean isNoBuiltInModes() {
        return noBuiltInModes;
    }

    public void setNoBuiltInModes(boolean noBuiltInModes) {
        this.noBuiltInModes = noBuiltInModes;
    }

    public String getSvgFile() {
        return svgFile;
    }

    public void setSvgFile(String svgFile) {
        this.svgFile = svgFile;
    }

    public String getSchoolYamlFile() {
        return schoolYamlFile;
    }

    public void setSchoolYamlFile(String schoolYamlFile) {
        this.schoolYamlFile = schoolYamlFile;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }
}
