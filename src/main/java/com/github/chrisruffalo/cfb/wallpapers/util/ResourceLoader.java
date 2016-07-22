package com.github.chrisruffalo.cfb.wallpapers.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p></p>
 *
 */
public class ResourceLoader {

    public static InputStream loadResource(final String resource) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resource);
    }

    public static String loadResourceAsString(final String resource) {
        try (
            final InputStream stream = ResourceLoader.loadResource(resource);
            final OutputStream target = new ByteArrayOutputStream();
        ) {
            // copy
            IOUtils.copy(stream, target);
            // return
            return target.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> loadAllSchoolResources() {
       return loadResourceLocations("schools");
    }

    public static List<String> loadAllSVGTemplates() {
        return loadResourceLocations("templates");
    }

    private static List<String> loadResourceLocations(final String startPath) {
        // locations of resources
        final List<String> resourceLocations = new LinkedList<>();

        // open 'file' location that could be the jar file
        try {
            final URI localUri = ResourceLoader.class.getResource("").toURI();
            String localPath = localUri.toString();
            if(localPath.startsWith("jar")) {
                localPath = localPath.substring("jar".length() + 1);
                localPath = localPath.substring(0, localPath.indexOf("!"));
            }
            if(localPath.startsWith("file")) {
                localPath = localPath.substring("file".length() + 1);
            }
            final File jarFile = new File(localPath);

            if (jarFile.isFile()) {  // run from packaged jar file
                try (final JarFile jar = new JarFile(jarFile)) {
                    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                    while (entries.hasMoreElements()) {
                        final JarEntry entry = entries.nextElement();
                        final String name = entry.getName();
                        if (name.startsWith(startPath + "/")) {
                            if (!entry.isDirectory()) {
                                resourceLocations.add(name);
                            }
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else { // run from outside of packaged mechanism
                final URL url = ResourceLoader.class.getResource("/" + startPath);
                final Path directoryPath = Paths.get(url.getPath());
                if (Files.isDirectory(directoryPath)) {
                    try {
                        Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                                final String pathString = path.toString();
                                resourceLocations.add(pathString.substring(pathString.lastIndexOf(startPath + "/")));
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    final String pathString = directoryPath.toString();
                    resourceLocations.add(pathString.substring(pathString.lastIndexOf(startPath + "/")));
                }
            }
        } catch (URISyntaxException uri) {
            throw new RuntimeException(uri);
        }

        return resourceLocations;
    }
}
