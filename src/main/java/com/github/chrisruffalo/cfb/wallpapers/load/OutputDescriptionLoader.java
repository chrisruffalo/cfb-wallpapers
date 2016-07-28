package com.github.chrisruffalo.cfb.wallpapers.load;

import com.github.chrisruffalo.cfb.wallpapers.model.OutputFormat;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.Template;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 */
public class OutputDescriptionLoader {

    @SuppressWarnings("unchecked")
    public static List<OutputTarget> load(final InputStream yamlStream) {
        if(yamlStream == null) {
            return Collections.emptyList();
        }

        final Yaml yaml = new Yaml();
        Map<String, Object> document = (Map<String, Object>)yaml.load(yamlStream);

        final List<OutputTarget> targets = new ArrayList<>();

        for(Map.Entry<String, Object> target : document.entrySet()) {
            final OutputTarget parsed = parseTarget(target.getKey(), target.getValue());
            if(parsed == null || parsed.getId() == null || parsed.getId().isEmpty()) {
                continue;
            }
            targets.add(parsed);
        }

        return targets;
    }

    @SuppressWarnings("unchecked")
    private static OutputTarget parseTarget(Object key, Object target) {
        if(!(target instanceof Map)) {
            return null;
        }
        final Map<String, Object> targetMap = (Map<String, Object>) target;

        final OutputTarget outputTarget = new OutputTarget((String)key, "templates");
        outputTarget.setName((String)targetMap.get("name"));

        if(targetMap.containsKey("formats")) {
            final List<OutputFormat> formats = parseFormats(targetMap);
            if(formats != null) {
                outputTarget.setFormats(formats);
            }
        }

        if(targetMap.containsKey("templates")) {
            final List<Template> templates = parseTemplates(outputTarget, targetMap);
            if(templates != null) {
                outputTarget.setTemplates(templates);
            }
        }

        return outputTarget;
    }

    @SuppressWarnings("unchecked")
    private static List<OutputFormat> parseFormats(Map<String, Object> targetMap) {
        final List<OutputFormat> formats = new LinkedList<>();

        final List<Object> formatList = (List<Object>)targetMap.get("formats");

        for(Object entry : formatList) {
            if(!(entry instanceof Map)) {
                continue;
            }
            final Map<String, Object> formatEntry = (Map<String, Object>)entry;
            if(formatEntry.isEmpty()) {
               continue;
            }
            final OutputFormat format = new OutputFormat(
                (String)formatEntry.get("id"),
                Integer.valueOf((String)formatEntry.get("height")),
                Integer.valueOf((String)formatEntry.get("width"))
            );
            formats.add(format);
        }

        return formats;
    }

    @SuppressWarnings("unchecked")
    private static List<Template> parseTemplates(final OutputTarget target, Map<String, Object> targetMap) {
        final List<Template> templates = new LinkedList<>();

        final List<Object> templateList = (List<Object>)targetMap.get("templates");

        for(Object entry : templateList) {
            if(!(entry instanceof Map)) {
                continue;
            }
            final Map<String, Object> templateEntry = (Map<String, Object>)entry;

            final Template template = new Template(
                target,
                (String)templateEntry.get("id"),
                (String)templateEntry.get("name")
            );
            templates.add(template);
        }

        return templates;
    }

}
