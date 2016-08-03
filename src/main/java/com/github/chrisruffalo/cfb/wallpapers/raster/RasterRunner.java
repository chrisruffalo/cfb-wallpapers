package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.archive.Archiver;
import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.web.SchoolPageGenerator;

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
    private final GeneratorOptions options;

    public RasterRunner(final Path outputPath, final School school, final SVGSchoolRasterizer rasterizer, final List<OutputTarget> targetList, final GeneratorOptions options) {
        this.outputPath = outputPath;
        this.school = school;
        this.rasterizer = rasterizer;
        this.targets = targetList;
        this.options = options;
    }

    @Override
    public void run() {
        // raster
        this.rasterizer.raster(this.targets);

        // generate web resources if required
        if(this.options.isGenerateWeb()) {
            final SchoolPageGenerator schoolPageGenerator = new SchoolPageGenerator(this.school.getDivision(), this.school.getConference(), this.school);
            schoolPageGenerator.generate(this.targets, this.outputPath);
        }

        // archive
        Archiver.archiveSchool(this.outputPath, this.school);

        // output
        System.out.printf("%s... [DONE]\n", this.school.display());
    }
}
