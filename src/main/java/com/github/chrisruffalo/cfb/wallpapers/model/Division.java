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
public class Division implements Comparable<Division> {

    private String id;
    private String name;

    private Map<String, String> conferenceMap = new HashMap<>();

    // conferences -> schools
    private Map<String, List<School>> schoolsPerConference = new HashMap<>();

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

    public Map<String, String> getConferenceMap() {
        return conferenceMap;
    }

    public void setConferenceMap(Map<String, String> conferenceMap) {
        this.conferenceMap = conferenceMap;
    }

    public String getConferenceName(final String conferenceId) {
        return this.conferenceMap.get(conferenceId);
    }

    public List<String> conferences() {
        if(this.conferenceMap.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> conferences = new ArrayList<>(this.conferenceMap.size());
        conferences.addAll(this.conferenceMap.keySet());
        Collections.sort(conferences);
        return Collections.unmodifiableList(conferences);
    }

    public void add(final String conference, final School school) {
        // get list of schools for conference
        List<School> schools = this.schoolsPerConference.get(conference);
        if(schools == null) {
            schools = new ArrayList<>(0);
            this.schoolsPerConference.put(conference, schools);
        }
        // add school
        schools.add(school);
    }

    public List<School> schools() {
        final List<School> all = new LinkedList<>();
        for(final List<School> conferenceSchools : this.schoolsPerConference.values()) {
            if(conferenceSchools == null) {
                continue;
            }
            all.addAll(conferenceSchools);
        }
        Collections.sort(all);
        return Collections.unmodifiableList(all);
    }

    public List<School> schools(final String conference) {
        final List<School> schools = this.schoolsPerConference.get(conference);
        if(schools == null || schools.isEmpty()) {
            return Collections.emptyList();
        }
        Collections.sort(schools);
        return schools;
    }

    // convert div to id so we don't have to do some sort of odd compare stuff
    private int idInt() {
        if("fbs".equals(this.getId())) {
            return 0;
        } else if("fcs".equals(this.getId())) {
            return 1;
        } else if("div2".equals(this.getId())) {
            return 2;
        } else if("div3".equals(this.getId())) {
            return 3;
        }
        return 4;
    }

    @Override
    public int compareTo(Division division) {
        return Integer.compare(this.idInt(), division.idInt());
    }

}
