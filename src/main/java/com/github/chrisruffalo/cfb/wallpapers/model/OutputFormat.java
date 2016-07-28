package com.github.chrisruffalo.cfb.wallpapers.model;

/**
 * <p></p>
 *
 */
public class OutputFormat {

    private final String id;

    private final int h;
    private final int w;

    public OutputFormat(final String id, final int h, final int w) {
        this.id = id;
        this.h = h;
        this.w = w;
    }

    public String getId() {
        return id;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public double w_double() {
        return (double)this.w;
    }

    public double h_double() {
        return (double)this.h;
    }

}
