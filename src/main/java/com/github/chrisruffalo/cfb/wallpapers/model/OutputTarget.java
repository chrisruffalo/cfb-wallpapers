package com.github.chrisruffalo.cfb.wallpapers.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 */
public class OutputTarget {

    private final String id;

    private String name;

    private final String resourceBase;

    private List<Template> templates;

    private List<OutputFormat> formats;

    public OutputTarget(final String id, final String resourceBase) {
        this.id = id;
        this.name = id;
        this.resourceBase = resourceBase;
        this.templates = new ArrayList<>(0);
        this.formats = new ArrayList<>(0);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<OutputFormat> getFormats() {
        return formats;
    }

    public void setFormats(List<OutputFormat> formats) {
        this.formats = formats;
    }

    public String getResourceBase() {
        return this.resourceBase;
    }

    public boolean outputExists(final Path outputPath, final String resourcePath) {
        return Files.exists(outputPath.resolve(resourcePath));
    }
}
