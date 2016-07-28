package com.github.chrisruffalo.cfb.wallpapers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 */
public class Divisions {

    private Map<String, Division> divisions = new HashMap<>();

    public List<School> allSchools() {
        final List<School> allSchools = new LinkedList<>();
        for(final Division division : this.divisions.values()) {
            allSchools.addAll(division.schools());
        }
        Collections.sort(allSchools);
        return Collections.unmodifiableList(allSchools);
    }

    public String getConferenceName(final String conferenceId) {
        for(final Division division : this.divisions.values()) {
            final String name = division.getConferenceName(conferenceId);
            if(name != null && !name.isEmpty()) {
                return name;
            }
        }
        return conferenceId;
    }

    public String getDivisionName(final String divId) {
        final Division division = this.divisions.get(divId);
        if(division == null) {
            return null;
        }
        return division.getName();
    }

    public Division getDivision(final String divId) {
        return this.divisions.get(divId);
    }

    public Map<String, Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(Map<String, Division> divisions) {
        this.divisions = divisions;
    }

    public List<Division> getSortedDivisions() {
        final List<Division> divisions = new ArrayList<>(this.divisions.size());
        divisions.addAll(this.divisions.values());
        Collections.sort(divisions);
        return Collections.unmodifiableList(divisions);
    }
}
