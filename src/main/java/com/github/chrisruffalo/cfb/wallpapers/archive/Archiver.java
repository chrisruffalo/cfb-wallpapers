package com.github.chrisruffalo.cfb.wallpapers.archive;

import com.github.chrisruffalo.cfb.wallpapers.model.Division;
import com.github.chrisruffalo.cfb.wallpapers.model.School;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p></p>
 *
 */
public class Archiver {

    public static void archiveAll(final Path outputPath) {
        final Path zipFilePath = outputPath.resolve("minimal_spirit.zip");

        // pack
        Archiver.pack(outputPath, zipFilePath);
    }

    public static void archiveDivision(final Path outputPath, final Division division) {
        // go to school
        final Path divisionPath = outputPath.toAbsolutePath().normalize()
                .resolve(division.getId());

        if (!Files.isDirectory(divisionPath)) {
            try {
                Files.createDirectories(divisionPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        final Path zipFilePath = divisionPath.resolve(division.getId() + ".zip");

        // pack
        Archiver.pack(divisionPath, zipFilePath);
    }

    public static void archiveConference(final Path outputPath, final Division division, final String conference) {
        // go to school
        final Path pathToConference = outputPath.toAbsolutePath().normalize()
                .resolve(division.getId())
                .resolve(conference);

        if (!Files.isDirectory(pathToConference)) {
            try {
                Files.createDirectories(pathToConference);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        final Path zipFilePath = pathToConference.resolve(conference + ".zip");

        // pack
        Archiver.pack(pathToConference, zipFilePath);
    }

    public static void archiveSchool(final Path outputPath, final School schoolToArchive) {
        // go to school
        final Path pathToSchool = outputPath.toAbsolutePath().normalize()
                .resolve(schoolToArchive.getDivision().getId())
                .resolve(schoolToArchive.getConference())
                .resolve(schoolToArchive.getId());

        if (!Files.isDirectory(pathToSchool)) {
            try {
                Files.createDirectories(pathToSchool);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        final Path zipFilePath = pathToSchool.resolve(schoolToArchive.getId() + ".zip");

        // pack
        Archiver.pack(pathToSchool, zipFilePath);
    }

    private static Path pack(final Path folder, final Path zipFilePath) {
        // archive picture bits
        try(
                final OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(zipFilePath));
                final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        ) {
            Files.walkFileTree(folder, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if(folder.getFileName().equals("resources")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    zipOutputStream.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(file.getFileName().toString().toLowerCase().endsWith(".png") || file.getFileName().toString().toLowerCase().endsWith(".svg") ) {
                        zipOutputStream.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
                        Files.copy(file, zipOutputStream);
                        zipOutputStream.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return zipFilePath;
    }
}
