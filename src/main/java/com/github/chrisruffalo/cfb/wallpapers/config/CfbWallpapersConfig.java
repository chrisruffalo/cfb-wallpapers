package com.github.chrisruffalo.cfb.wallpapers.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public enum CfbWallpapersConfig {
    // singleton
    INSTANCE;

    private static final String TEMPLATE_PRIMARY = "template.primary";
    private static final String TEMPLATE_SECONDARY = "template.secondary";
    private static final String TEMPLATE_ACCENT="template.accent";

    private static final String CONFIG_FILE_NAME = "config.properties";

    private final Properties properties;

    private CfbWallpapersConfig() {
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            this.properties = new Properties();
            this.properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(this.properties.isEmpty()) {
            throw new RuntimeException("Could not load default properties from classpath resource '" + CONFIG_FILE_NAME + "'");
        }
    }

    private String get(String key) {
        return this.properties.getProperty(key);
    }

    public String templatePrimary() {
        return this.get(TEMPLATE_PRIMARY);
    }

    public String templateSecondary() {
        return this.get(TEMPLATE_SECONDARY);
    }

    public String templateAccent() {
        return this.get(TEMPLATE_ACCENT);
    }
}
