package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputFormat;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputTarget;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.model.Template;
import com.github.chrisruffalo.cfb.wallpapers.transform.SVGColorChange;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * <p></p>
 *
 */
public class SVGSchoolRasterizer {

    private static final SVGColorChange CHANGER = new SVGColorChange();

    private final School school;
    private final Path pathToOutput;

    public SVGSchoolRasterizer(final String divisionId, final String conferenceId, final School school, final Path pathToOutput) {
        this.school = school;

        this.pathToOutput = pathToOutput.resolve(divisionId).resolve(conferenceId).resolve(school.getId());
        if(!Files.isDirectory(this.pathToOutput)) {
            try {
                Files.createDirectories(this.pathToOutput);
            } catch (IOException e) {
                throw new RuntimeException("Could not create path for school output with id: " + this.school.getId(), e);
            }
        }
    }

    public void raster(final List<OutputTarget> targets) {
        // raster for each target
        targets.forEach(this::rasterForTarget);
    }

    private void rasterForTarget(final OutputTarget target) {
        for(final Template template : target.getTemplates()) {
            if(target.getFormats().isEmpty()) {
                continue;
            }
            this.rasterTemplate(target, template, target.getFormats());
        }
    }

    private void rasterTemplate(final OutputTarget target, final Template template, final List<OutputFormat> formats) {
        // get template name itself
        final String templateId = template.getId();

        final String templateContents = template.contents();

        // rasterize in each color set
        for(final ColorSet colorSet : this.school.getColors()) {
            // create file name
            final String fileNameBase = this.school.getId() + "-" + target.getId() + "-" + templateId + "-" + colorSet.getId();

            // path to svg
            final Path pathToSvg = this.pathToOutput.resolve("svg").resolve(fileNameBase + ".svg");
            try {
                if (!Files.isDirectory(pathToSvg.getParent())) {
                    Files.createDirectories(pathToSvg.getParent());
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            final String output = CHANGER.transform(templateContents, colorSet);

            // if null or empty output is produced then skip this color set because
            // the accent color was missing (color sets for schools with no accent)
            if(output == null || output.isEmpty()) {
                continue;
            }

            try {
                Files.write(pathToSvg, output.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // create universe
            final SVGUniverse universe = SVGCache.getSVGUniverse();

            // load svg diagram for later use
            final SVGDiagram diagram;
            try {
                diagram = universe.getDiagram(universe.loadSVG(pathToSvg.toUri().toURL()));

                // only one output size for now
                for(final OutputFormat format : formats) {
                    this.writeSvgToPng(diagram, template.getTarget().getId(), fileNameBase, format);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            universe.clear();
        }
    }

    private void writeSvgToPng(final SVGDiagram diagram, final String targetId, final String fileNameBase, final OutputFormat size) {

        // output
        final Path outputFile = this.pathToOutput.resolve(targetId).resolve(fileNameBase + "_" + size.getId() + ".png");
        if(!Files.isDirectory(outputFile.getParent())) {
            try {
                Files.createDirectories(outputFile.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (final OutputStream imageOutput = Files.newOutputStream(outputFile)) {

            // transform!
            final AffineTransform transform = new AffineTransform();
            transform.setToScale(size.w_double() / diagram.getWidth(), size.h_double() / diagram.getHeight());

            BufferedImage bi = new BufferedImage(size.getW(), size.getH(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig2 = bi.createGraphics();
            ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // do transform
            ig2.transform(transform);

            // render diagram
            diagram.render(ig2);

            // write
            ImageIO.write(bi, "PNG", imageOutput);

            ig2.dispose();
        } catch (IOException | SVGException ex) {
            throw new RuntimeException(ex);
        }
    }


}
