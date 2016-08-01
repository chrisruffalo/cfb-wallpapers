package com.github.chrisruffalo.cfb.wallpapers.model;

import java.util.List;

/**
 * <p></p>
 *
 */
public class School implements Comparable<School> {

    private String id;
    private String name;
    private String conference;

    private List<ColorSet> colors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public ColorSet getPrimaryColorSet() {
        if(this.colors.isEmpty()) {
            return null;
        }
        // return first color
        return this.colors.get(0);
    }

    public List<ColorSet> getColors() {
        return colors;
    }

    public void setColors(List<ColorSet> colors) {
        this.colors = colors;
    }

    public String display() {
        return this.name == null || this.name.trim().isEmpty() ? this.id : this.name;
    }

    @Override
    public int compareTo(School school) {
        return this.getName().compareTo(school.getName());
    }
}
