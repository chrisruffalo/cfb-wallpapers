package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.TemplateCreator;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
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
public class StaticResourceGenerator {

    // values for output locations
    private static final String RESOURCE_PATH = "resources";
    private static final String CSS_RESOURCE_PATH = RESOURCE_PATH + "/css";

    // input locations
    private static final String CSS_TEMPLATE_PATH = "web/cfb.css.template";

    // generate css output for given school, given the root output directory
    public static void generateCss(final Path outputDir, final List<School> schools) {

        // set up expected output path
        final Path outputCssPath = outputDir.resolve(CSS_RESOURCE_PATH).resolve("cfb.css");
        if(!Files.isDirectory(outputCssPath.getParent())) {
            try {
                Files.createDirectories(outputCssPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // create data model
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("schools", schools);

        // write file
        try (final Writer cssFile = new OutputStreamWriter(Files.newOutputStream(outputCssPath))){
            // get template
            final Template template = TemplateCreator.getTemplate(CSS_TEMPLATE_PATH);
            // execute on data model
            template.process(dataModel, cssFile);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
