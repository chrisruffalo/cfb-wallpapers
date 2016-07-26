package com.github.chrisruffalo.cfb.wallpapers.util;

import com.github.chrisruffalo.cfb.wallpapers.web.StaticResourceGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

/**
 * <p></p>
 *
 */
public class TemplateCreator {

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_25);

    static {
        CONFIGURATION.setDefaultEncoding("UTF-8");

        // set class loader
        CONFIGURATION.setClassLoaderForTemplateLoading(StaticResourceGenerator.class.getClassLoader(), "");
    }

    public static Template getTemplate(final String pathToTemplate) throws IOException {
        return CONFIGURATION.getTemplate(pathToTemplate);
    }

}
