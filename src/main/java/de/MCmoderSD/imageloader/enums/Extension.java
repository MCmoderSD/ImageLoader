package de.MCmoderSD.imageloader.enums;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public enum Extension {

    // Supported image formats with their properties
    JPEG(false, false),
    JPG(false, false),
    PNG(true, true),
    BMP(false, true),
    TIFF(true, true),
    GIF(true, true),
    WEBP(true, true);

    // Attributes
    private final String extension;
    private final boolean transparent;
    private final boolean lossless;

    // Constructor
    Extension(boolean transparent, boolean lossless) {
        this.extension = this.name().toLowerCase();
        this.transparent = transparent;
        this.lossless = lossless;
    }

    // Static method to get Extension from string
    public static Extension fromString(String extension) throws IllegalArgumentException {

        // Validate input
        if (extension == null) throw new IllegalArgumentException("Extension cannot be null");

        // Process input
        extension = extension.trim().toLowerCase();
        while (extension.startsWith(".")) extension = extension.substring(1); // Remove leading dots

        // Validate Input Format
        if (extension.isBlank()) throw new IllegalArgumentException("Extension cannot be blank");

        // Find matching extension
        for (var ext : Extension.values()) if (ext.extension.equalsIgnoreCase(extension)) return ext;
        throw new IllegalArgumentException("Unknown image extension: " + extension);
    }

    public String getExtension() {
        return extension;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isLossless() {
        return lossless;
    }
}