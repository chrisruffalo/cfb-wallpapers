package com.github.chrisruffalo.cfb.wallpapers.model;

/**
 * <p></p>
 *
 */
public interface OutputSize {

    double w();
    double h();

    default int w_int() {
        return (int)this.w();
    }

    default int h_int() {
        return (int)this.h();
    }

}
