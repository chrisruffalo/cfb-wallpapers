package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.model.BuiltInOutputSize;
import com.github.chrisruffalo.cfb.wallpapers.model.ColorSet;
import com.github.chrisruffalo.cfb.wallpapers.model.OutputSize;
import com.github.chrisruffalo.cfb.wallpapers.model.School;
import com.github.chrisruffalo.cfb.wallpapers.transform.SVGColorChange;
import com.github.chrisruffalo.cfb.wallpapers.util.ResourceLoader;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public SVGSchoolRasterizer(final School school, final Path pathToOutput) {
        this.school = school;
        this.pathToOutput = pathToOutput.resolve(school.getConference()).resolve(school.getId());
        if(!Files.isDirectory(this.pathToOutput)) {
            try {
                Files.createDirectories(this.pathToOutput);
            } catch (IOException e) {
                throw new RuntimeException("Could not create path for school output with id: " + this.school.getId(), e);
            }
        }
    }

    public void raster() {
        // load templates
        final List<String> templates = ResourceLoader.loadAllSVGTemplates();
        if(templates.isEmpty()) {
            return;
        }

        // raster each template
        templates.forEach(this::rasterTemplate);
    }

    private void rasterTemplate(final String templateResource) {
        // load template once
        final String template = ResourceLoader.loadResourceAsString(templateResource);

        // get template name itself
        final String templateName = templateResource.substring(templateResource.lastIndexOf("/") + 1, templateResource.lastIndexOf("."));

        // rasterize in each color set
        for(final ColorSet colorSet : this.school.getColors()) {
            // create file name
            final String fileNameBase = this.school.getId() + "-" + templateName + "-" + colorSet.getId();

            final String output = CHANGER.transform(template, colorSet);
            try {
                Files.write(this.pathToOutput.resolve(fileNameBase + ".svg"), output.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // only one output size for now
            this.writeSvgToPng(fileNameBase, BuiltInOutputSize.O_1920_1080, output);
        }
    }

    private void writeSvgToPng(final String fileNameBase, final OutputSize size, final String svgContents) {
        // create universe
        final SVGUniverse universe = SVGCache.getSVGUniverse();

        final Path svgPath = this.pathToOutput.resolve(fileNameBase + ".svg");

        try (final InputStream svgInputStream = new ByteArrayInputStream(svgContents.getBytes())) {
            final SVGDiagram diagram = universe.getDiagram(universe.loadSVG(svgPath.toUri().toURL()));

            // transform!
            final AffineTransform transform = new AffineTransform();
            transform.setToScale(size.w() / diagram.getWidth(), size.h() / diagram.getHeight());

            BufferedImage bi = new BufferedImage(size.w_int(), size.h_int(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig2 = bi.createGraphics();
            ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // do transform
            ig2.transform(transform);

            // render diagram
            diagram.render(ig2);

            // output
            final Path outputFile = this.pathToOutput.resolve(fileNameBase + "_" + size.w_int() + "_" + size.h_int() + ".png");

            // write
            ImageIO.write(bi, "PNG", Files.newOutputStream(outputFile));

            ig2.dispose();
        } catch (IOException | SVGException ex) {
            ex.printStackTrace();
        } finally {
            universe.clear();
        }
    }


}
