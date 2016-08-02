package com.github.chrisruffalo.cfb.wallpapers.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p></p>
 *
 */
public class School implements Comparable<School> {

    private String id;
    private String name;

    private Set<String> tags;

    private String conference;
    private Division division;

    private String wikiUrl;
    private String colorUrl;

    private List<ColorSet> colors;

    public School() {
        this.tags = new TreeSet<>();
        this.colors = new ArrayList<>(0);
    }

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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getColorUrl() {
        return colorUrl;
    }

    public void setColorUrl(String colorUrl) {
        this.colorUrl = colorUrl;
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
