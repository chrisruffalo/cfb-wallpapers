package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.archive.Archiver;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.School;

import java.nio.file.Path;
import java.util.List;

/**
 * <p></p>
 *
 */
public class RasterRunner implements Runnable {

    private final Path outputPath;
    private final School school;
    private final SVGSchoolRasterizer rasterizer;
    private final List<OutputTarget> targets;

    public RasterRunner(final Path outputPath, final School school, final SVGSchoolRasterizer rasterizer, final List<OutputTarget> targetList) {
        this.outputPath = outputPath;
        this.school = school;
        this.rasterizer = rasterizer;
        this.targets = targetList;
    }

    @Override
    public void run() {
        // raster
        this.rasterizer.raster(this.targets);
        // archive
        Archiver.archiveSchool(this.outputPath, this.school);
    }
}
