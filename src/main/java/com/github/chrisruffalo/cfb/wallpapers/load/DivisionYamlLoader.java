package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.Division;
import com.github.chrisruffalo.cfb.wallpapers.model.Divisions;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p></p>
 *
 */
public class DivisionYamlLoader {

    private static final String KEY_DIVISIONS = "divisions";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CONFERENCES = "conferences";

    @SuppressWarnings({"unchecked"})
    public static Divisions loadDivisions(final String resourceLocation) {
        // empty division map
        final Map<String, Division> divisionMap = new HashMap<>(4);

        // load yaml document
        final Yaml yaml = new Yaml();
        final Map<String, Object> document = (Map<String, Object>) yaml.load(ResourceLoader.loadResource(resourceLocation));

        // empty map
        if(!document.containsKey(KEY_DIVISIONS)) {
            return new Divisions();
        }

        // get division list
        final List<Map<String, Object>> divisionsList = (List<Map<String, Object>>)document.get(KEY_DIVISIONS);

        for(Map<String, Object> divisionDocument: divisionsList) {
            final Division div = parseDivision(divisionDocument);
            // must have id and conferences
            if(div != null && div.getId() != null && !div.getId().isEmpty()) {
                divisionMap.put(div.getId(), div);
            }
        }

        // output
        final Divisions divisions = new Divisions();
        divisions.setDivisions(divisionMap);
        return divisions;
    }

    @SuppressWarnings("unchecked")
    public static Division parseDivision(final Map<String, Object> divisionMap) {
        final Division div = new Division();

        div.setId((String)divisionMap.get(KEY_ID));
        div.setName((String)divisionMap.get(KEY_NAME));

        // get from document
        final Map<String, Object> documentConferences = (Map<String, Object>)divisionMap.get(KEY_CONFERENCES);
        if(documentConferences == null || documentConferences.isEmpty()) {
            return div;
        }

        // create holder
        final Map<String, String> conferences = new HashMap<>(0);

        // get all conferences
        for(Map.Entry<String, Object> docConf : documentConferences.entrySet()) {
            conferences.put(docConf.getKey(), Objects.toString(docConf.getValue()));
        }

        // add conferences to div
        div.setConferenceMap(conferences);

        // done parsing division
        return div;
    }

}
