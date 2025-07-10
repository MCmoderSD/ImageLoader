package de.MCmoderSD.imageloader.enums;

/**
 * Enum representing supported image file extensions and their properties.
 */
@SuppressWarnings("ALL")
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

    /**
     * Constructor for each image extension.
     *
     * @param transparent whether the format supports transparency
     * @param lossless whether the format is lossless
     */
    Extension(boolean transparent, boolean lossless) {
        this.extension = this.name().toLowerCase();
        this.transparent = transparent;
        this.lossless = lossless;
    }

    /**
     * Parses a string extension into the corresponding enum.
     *
     * @param extension the extension string (e.g., "png")
     * @return the corresponding Extension enum
     * @throws IllegalArgumentException if no matching extension is found
     */
    public static Extension fromString(String extension) throws IllegalArgumentException {
        for (Extension ext : Extension.values()) if (ext.extension.equalsIgnoreCase(extension)) return ext;
        throw new IllegalArgumentException("Unknown image extension: " + extension);
    }

    /**
     * Gets the lowercase extension string.
     *
     * @return the file extension (e.g., "png")
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Checks if the image format supports transparency.
     *
     * @return true if transparent, false otherwise
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Checks if the image format is lossless.
     *
     * @return true if lossless, false otherwise
     */
    public boolean isLossless() {
        return lossless;
    }
}