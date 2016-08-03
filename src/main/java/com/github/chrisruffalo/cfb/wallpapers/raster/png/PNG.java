package com.github.chrisruffalo.cfb.wallpapers.raster.png;

import com.github.chrisruffalo.cfb.wallpapers.config.GeneratorOptions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * <p></p>
 * From: http://www.chrfr.de/software/PNG.java
 */
public class PNG {

    public static byte[] toPNG(int width, int height, int[] argbBytes, final GeneratorOptions options) throws IOException
    {
        byte[] signature = new byte[] {(byte) 137, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10};
        byte[] header = createHeaderChunk(width, height);
        byte[] data = createDataChunk(width, height, argbBytes, options);
        byte[] trailer = createTrailerChunk();

        ByteArrayOutputStream png = new ByteArrayOutputStream(signature.length + header.length + data.length + trailer.length);
        png.write(signature);
        png.write(header);
        png.write(data);
        png.write(trailer);
        return png.toByteArray();
    }

    private static byte[] createHeaderChunk(int width, int height) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(13);
        DataOutputStream chunk = new DataOutputStream(baos);
        chunk.writeInt(width);
        chunk.writeInt(height);
        chunk.writeByte(8); // Bitdepth
        chunk.writeByte(6); // Colortype ARGB
        chunk.writeByte(0); // Compression
        chunk.writeByte(0); // Filter
        chunk.writeByte(0); // Interlace
        return toChunk("IHDR", baos.toByteArray());
    }

    private static byte[] createDataChunk(int width, int height, int[] argbBytes, final GeneratorOptions options) throws IOException {
        int source = 0;
        int dest = 0;
        byte[] raw = new byte[4*(width*height) + height];
        for (int y = 0; y < height; y++)
        {
            raw[dest++] = 0; // No filter
            for (int x = 0; x < width; x++)
            {
                // order is RGBA swapped from ARGB input
                raw[dest++] = (byte)((argbBytes[source] & 0x00FF0000) >> 16);
                raw[dest++] = (byte)((argbBytes[source] & 0x0000FF00) >> 8);
                raw[dest++] = (byte)((argbBytes[source] & 0x000000FF));
                raw[dest++] = (byte)((argbBytes[source++] & 0xFF000000) >> 24);
            }
        }
        return toChunk("IDAT", toZLIB(raw,options));
    }

    private static byte[] createTrailerChunk() throws IOException {
        return toChunk("IEND", new byte[] {});
    }

    private static byte[] toChunk(String id, byte[] raw) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length + 12);
        DataOutputStream chunk = new DataOutputStream(baos);

        chunk.writeInt(raw.length);

        byte[] bid = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            bid[i] = (byte) id.charAt(i);
        }

        chunk.write(bid);

        chunk.write(raw);

        int crc = 0xFFFFFFFF;
        crc = updateCRC(crc, bid);
        crc = updateCRC(crc, raw);
        chunk.writeInt(~crc);

        return baos.toByteArray();
    }

    private static int[] crcTable = null;

    private static void createCRCTable() {
        crcTable = new int[256];

        for (int i = 0; i < 256; i++)
        {
            int c = i;
            for (int k = 0; k < 8; k++)
            {
                c = ((c & 1) > 0) ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
            }
            crcTable[i] = c;
        }
    }

    private static int updateCRC(int crc, byte[] raw) {
        if (crcTable == null)
        {
            createCRCTable();
        }

        for (int i = 0; i < raw.length; i++) {
            crc = crcTable[(crc ^ raw[i]) & 0xFF] ^ (crc >>> 8);
        }

        return crc;
    }

    /* This method is called to encode the image data as a zlib
       block as required by the PNG specification. This file comes
       with a minimal ZLIB encoder which uses uncompressed deflate
       blocks (fast, short, easy, but no compression). If you want
       compression, call another encoder (such as JZLib?) here. */
    private static byte[] toZLIB(byte[] raw, final GeneratorOptions options) throws IOException {

        // handle optimization of outputs based on the option set by the user
        final int compression = options.isOptimizePng() ? Deflater.BEST_COMPRESSION : Deflater.BEST_SPEED;

        try (
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final DeflaterOutputStream dout = new DeflaterOutputStream(out, new Deflater(compression));
        ) {
            dout.write(raw);

            dout.flush();
            dout.finish();

            out.flush();

            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
