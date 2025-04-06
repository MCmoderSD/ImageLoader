package de.MCmoderSD.imageloader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Utility class for converting {@link BufferedImage} objects between different formats.
 * Supports common image formats including BMP, GIF, JPEG, JPG, PNG, and TIFF.
 */
@SuppressWarnings("ALL")
public class Converter {

    /**
     * Converts a BufferedImage to BMP format (RGB color model without alpha).
     *
     * @param image the source image to convert
     * @return a new BufferedImage in BMP format, or null if the input was null
     */
    public static BufferedImage convertToBMP(BufferedImage image) {
        if (image == null) return null;
        return convertTo(image, TYPE_INT_RGB);
    }

    /**
     * Converts a BufferedImage to GIF format (ARGB color model with alpha).
     *
     * @param image the source image to convert
     * @return a new BufferedImage in GIF format, or null if the input was null
     */
    public static BufferedImage convertToGIF(BufferedImage image) {
        return convertTo(image, TYPE_INT_ARGB);
    }

    /**
     * Converts a BufferedImage to JPEG format (RGB color model without alpha).
     *
     * @param image the source image to convert
     * @return a new BufferedImage in JPEG format, or null if the input was null
     */
    public static BufferedImage convertToJPEG(BufferedImage image) {
        if (image == null) return null;
        return convertTo(image, TYPE_INT_RGB);
    }

    /**
     * Converts a BufferedImage to JPG format (RGB color model without alpha).
     * This is an alias for convertToJPEG.
     *
     * @param image the source image to convert
     * @return a new BufferedImage in JPG format, or null if the input was null
     */
    public static BufferedImage convertToJPG(BufferedImage image) {
        if (image == null) return null;
        return convertTo(image, TYPE_INT_RGB);
    }

    /**
     * Converts a BufferedImage to PNG format (ARGB color model with alpha).
     *
     * @param image the source image to convert
     * @return a new BufferedImage in PNG format, or null if the input was null
     */
    public static BufferedImage convertToPNG(BufferedImage image) {
        if (image == null) return null;
        return convertTo(image, TYPE_INT_ARGB);
    }

    /**
     * Converts a BufferedImage to TIFF format (ARGB color model with alpha).
     *
     * @param image the source image to convert
     * @return a new BufferedImage in TIFF format, or null if the input was null
     */
    public static BufferedImage convertToTIFF(BufferedImage image) {
        if (image == null) return null;
        return convertTo(image, TYPE_INT_ARGB);
    }

    /**
     * Internal helper method that performs the actual image conversion.
     * Creates a new BufferedImage of the specified type and draws the original image onto it.
     *
     * @param image the source image to convert
     * @param type the BufferedImage type constant to convert to
     * @return a new BufferedImage of the specified type
     */
    private static BufferedImage convertTo(BufferedImage image, int type) {
        if (image.getType() == type) return image;
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), type);
        Graphics2D g2d = convertedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return convertedImage;
    }
}