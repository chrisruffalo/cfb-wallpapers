package com.github.chrisruffalo.cfb.wallpapers.model;

import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;

import java.io.InputStream;

/**
 * <p></p>
 *
 */
public class Template {

    private final String id;
    private final String name;
    private final OutputTarget target;
    private final String resourcePath;

    public Template(final OutputTarget target, final String id, final String name) {
        this.id = id;
        this.name = name;
        this.target = target;
        this.resourcePath = target.getResourceBase() + "/" + target.getId() + "/" + id + ".svg";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public OutputTarget getTarget() {
        return target;
    }

    public InputStream load() {
        return ResourceLoader.loadResource(this.resourcePath);
    }

    public String contents() { return ResourceLoader.loadResourceAsString(this.resourcePath); }
}
