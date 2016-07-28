package com.github.chrisruffalo.cfb.wallpapers.web;

import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.model.Divisions;
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
import java.util.Base64;
import java.util.HashMap;
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
    private static final String JS_RESOURCE_PATH = RESOURCE_PATH + "/js";

    // input/resource locations
    private static final String CSS_TEMPLATE_PATH = "web/cfb.css.template";
    private static final String JS_TEMPLATES_PATH = "web/js/";
    private static final String[] JS_RESOURCES = {};
    private static final String HTML_INDEX_TEMPLATE_PATH = "web/index.html.template";
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

    public static void generateStaticResources(final Path outputDir, final Divisions divisions, final GeneratorOptions options) {

        // create data model
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("schools", divisions.allSchools());
        dataModel.put("divisions", divisions);
        dataModel.put("options", options);

        // generate the static CSS resource
        generateCss(dataModel, outputDir);

        // write font awesome elements
        writeFontAwesome(dataModel, outputDir);

        // generate javascript resources
        generateJSResources(dataModel, outputDir);

        // generate the index resource
        generateIndexFile(dataModel, outputDir);
    }

    // write font awesome elements
    private static void writeFontAwesome(final Map<String, Object> dataModel, final Path outputDir) {
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
                final Path outputCssResource = cssOutputPath.resolve(cssResource);
                Files.copy(ResourceLoader.loadResource(CSS_STATIC_PATH + cssResource), outputCssResource);
                dataModel.put("sha512_" + conditionName(cssResource), hashResource(outputCssResource));
            }

            for(final String fontResource : FONT_RESOURCES) {
                final Path outputFontResource = fontOutputPath.resolve(fontResource);
                Files.copy(ResourceLoader.loadResource(FONT_STATIC_PATH + fontResource), outputFontResource);
                dataModel.put("sha512_" + conditionName(fontResource), hashResource(outputFontResource));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // generate css output for given school, given the root output directory
    private static void generateCss(final Map<String, Object> dataModel, final Path outputDir) {
        // set up expected output path
        final Path outputCssPath = outputDir.resolve(CSS_RESOURCE_PATH).resolve("cfb.css").toAbsolutePath().normalize();
        if (!Files.isDirectory(outputCssPath.getParent())) {
            try {
                Files.createDirectories(outputCssPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // generate resource
        generateResource(dataModel, CSS_TEMPLATE_PATH, outputCssPath);

        // add output to hash
        dataModel.put("sha512_cfb_css", hashResource(outputCssPath));
    }


    private static void generateIndexFile(Map<String, Object> dataModel, final Path outputDir) {
        // set up expected output path
        final Path outputHtmlPath = outputDir.resolve("index.html");

        // generate resource
        generateResource(dataModel, HTML_INDEX_TEMPLATE_PATH, outputHtmlPath);
    }

    private static void generateJSResources(Map<String, Object> dataModel, final Path outputDir) {
        try {
            // output path
            final Path jsOutputPath = outputDir.resolve(JS_RESOURCE_PATH);
            if (!Files.isDirectory(jsOutputPath)) {
                Files.createDirectories(jsOutputPath);
            }

            for(final String jsFile : JS_RESOURCES) {
                final Path jsResource = jsOutputPath.resolve(jsFile).normalize().toAbsolutePath();
                generateResource(dataModel, JS_TEMPLATES_PATH + jsFile + ".template", jsResource);

                // add output to hash
                dataModel.put("sha512_" + conditionName(jsFile), hashResource(jsResource));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void generateResource(final Map<String, Object> dataModel, final String resourceToLoad, final Path outputPath) {
        // write file
        try (final Writer htmlFile = new OutputStreamWriter(Files.newOutputStream(outputPath))){
            // get template
            final Template template = TemplateCreator.getTemplate(resourceToLoad);
            // execute on data model
            template.process(dataModel, htmlFile);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hashResource(final Path resource) {
        // calculate sha512 hash in base64 encoding for ensuring the resource is right
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] contents = Files.readAllBytes(resource);
            md.update(contents);
            byte[] digestData = md.digest();
            return Base64.getEncoder().encodeToString(digestData);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String conditionName(final String name) {
        return name.replaceAll("\\.", "_").replaceAll("-","_");
    }
}
