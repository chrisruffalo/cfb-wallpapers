package com.github.chrisruffalo.cfb.wallpapers.raster;

import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;
import com.github.chrisruffalo.cfb.wallpapers.raster.png.PNG;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p></p>
 *
 */
public class BufferedImagePNGWriter {

    public static void write(final Path outputPath, final BufferedImage bi, GeneratorOptions options) {
        // BI must be ARGB!

        // get raster data
        final int[] data = ((DataBufferInt) bi.getAlphaRaster().getDataBuffer()).getData();

        // do png
        try {
            final byte[] png = PNG.toPNG(bi.getWidth(), bi.getHeight(), data, options);
            // write
            Files.write(outputPath, png);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
