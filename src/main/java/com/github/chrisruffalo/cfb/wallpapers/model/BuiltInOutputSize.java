package com.github.chrisruffalo.cfb.wallpapers.model;

/**
 * <p></p>
 *
 * @author Chris Ruffalo {@literal <cruffalo@redhat.com>}
 */
public enum BuiltInOutputSize implements OutputSize {
    // 1920x10810
    O_1920_1080(1920, 1080),
    // 4k
    O_4096_2160(4096, 2160)
    ;

    private final double width;
    private final double height;

    private BuiltInOutputSize(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    public double w() {
        return this.width;
    }

    public double h() {
        return this.height;
    }
}
