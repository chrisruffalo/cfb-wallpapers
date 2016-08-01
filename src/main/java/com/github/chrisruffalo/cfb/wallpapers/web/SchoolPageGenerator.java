package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.model.Division;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.TemplateCreator;
import freemarker.template.Template;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 */
public class SchoolPageGenerator {

    private final School school;
    private final Division division;
    private final String conference;

    public SchoolPageGenerator(final Division division, final String conference, final School school) {
        this.division = division;
        this.conference = conference;
        this.school = school;
    }

    public void generate(final List<OutputTarget> outputTargets, final Path outputPath) {
        final Path schoolPath = outputPath.resolve(this.division.getId()).resolve(this.conference).resolve(this.school.getId()).normalize().toAbsolutePath();
        if(!Files.isDirectory(schoolPath)) {
            try {
                Files.createDirectories(schoolPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        final Path htmlPath = schoolPath.resolve(this.school.getId() + ".html");

        // create datamodel
        final Map<String, Object> model = new HashMap<>();
        model.put("division", this.division);
        model.put("conference", this.conference);
        model.put("school", this.school);
        model.put("targets", outputTargets);
        model.put("outputPath", outputPath);

        // write template out
        try (final Writer writer = new OutputStreamWriter(Files.newOutputStream(htmlPath))){
            final Template template = TemplateCreator.getTemplate("web/school.html.template");
            template.process(model, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
