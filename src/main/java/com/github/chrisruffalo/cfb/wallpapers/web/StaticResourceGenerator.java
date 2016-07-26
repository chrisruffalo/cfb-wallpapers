package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.Generator;
import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import com.github.chrisruffalo.cfb.wallpapers.util.TemplateCreator;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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
    private static final String FONT_RESOURCE_PATH = RESOURCE_PATH + "/fonts";

    // input locations
    private static final String CSS_TEMPLATE_PATH = "web/cfb.css.template";
    private static final String HTML_TEMPLATE_PATH = "web/index.html.template";
    private static final String CSS_STATIC_PATH = "web/static/css/";
    private static final String[] CSS_RESOURCES = new String[] {
        "font-awesome.min.css",
    };
    private static final String FONT_STATIC_PATH = "web/static/fonts/";
    private static final String[] FONT_RESOURCES = new String[] {
        "FontAwesome.otf",
        "fontawesome-webfont.eot",
        "fontawesome-webfont.svg",
        "fontawesome-webfont.ttf",
        "fontawesome-webfont.woff",
        "fontawesome-webfont.woff2"
    };

    public static void generateStaticResources(
        final Path outputDir,
        final List<School> allSchools,
        final Map<String, List<School>> fbsSchools,
        final Map<String, List<School>> fcsSchools,
        final GeneratorOptions options
    ) {
        // generate the static CSS resource
        final Path outputCss = generateCss(outputDir, allSchools);

        // write font awesome elements
        writeFontAwesome(outputDir);

        // generate the index resource
        generateIndexFile(outputDir, outputCss, allSchools, fbsSchools, fcsSchools, options);
    }

    // write font awesome elements
    public static void writeFontAwesome(final Path outputDir) {
        try {
            final Path cssOutputPath = outputDir.resolve(CSS_RESOURCE_PATH);
            if(!Files.isDirectory(cssOutputPath)) {
                Files.createDirectories(cssOutputPath);
            }

            final Path fontOutputPath = outputDir.resolve(FONT_RESOURCE_PATH);
            if(!Files.isDirectory(fontOutputPath)) {
                Files.createDirectories(fontOutputPath);
            }

            for(final String cssResource : CSS_RESOURCES) {
                Files.copy(ResourceLoader.loadResource(CSS_STATIC_PATH + cssResource), cssOutputPath.resolve(cssResource));
            }

            for(final String fontResource : FONT_RESOURCES) {
                Files.copy(ResourceLoader.loadResource(FONT_STATIC_PATH + fontResource), fontOutputPath.resolve(fontResource));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // generate css output for given school, given the root output directory
    public static Path generateCss(final Path outputDir, final List<School> schools) {
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
            // return path to file
            return outputCssPath;
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateIndexFile(
        final Path outputDir,
        final Path outputCss,
        final List<School> allSchools,
        final Map<String, List<School>> fbsSchools,
        final Map<String, List<School>> fcsSchools,
        final GeneratorOptions options
    ) {
        // set up expected output path
        final Path outputHtmlPath = outputDir.resolve("index.html");

        // calculate conference names in-order
        final List<String> fbsConferences = new ArrayList<>(fbsSchools.keySet());
        Collections.sort(fbsConferences);

        final List<String> fcsConferences = new ArrayList<>(fcsSchools.keySet());
        Collections.sort(fcsConferences);

        // create data model
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("schools", allSchools);
        dataModel.put("fbs", fbsSchools);
        dataModel.put("fbsConferences", fbsConferences);
        dataModel.put("fcs", fcsSchools);
        dataModel.put("fcsConferences", fcsConferences);
        dataModel.put("options", options);
        // also need names of conferences
        dataModel.put("CONFERENCE_MAP", Generator.CONFERENCE_MAP);

        // calculate sha512 hash in base64 encoding for ensuring the resource is right
        // and add it to the data model
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] contents = Files.readAllBytes(outputCss);
            md.update(contents);
            byte[] digestData = md.digest();
            final String base64 = Base64.getEncoder().encodeToString(digestData);
            dataModel.put("cfbCssHashString", base64);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // write file
        try (final Writer htmlFile = new OutputStreamWriter(Files.newOutputStream(outputHtmlPath))){
            // get template
            final Template template = TemplateCreator.getTemplate(HTML_TEMPLATE_PATH);
            // execute on data model
            template.process(dataModel, htmlFile);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
