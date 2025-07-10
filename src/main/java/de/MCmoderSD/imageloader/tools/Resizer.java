package de.MCmoderSD.imageloader.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Utility class for resizing and scaling {@link BufferedImage} instances with high quality.
 */
@SuppressWarnings("ALL")
public class Resizer {

    /**
     * Resizes a {@link BufferedImage} to a square of the given size.
     *
     * @param image the image to resize
     * @param size the width and height to resize to
     * @return the resized image
     * @throws IllegalArgumentException if size is non-positive
     */
    public static BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }

    /**
     * Resizes a {@link BufferedImage} to the given width and height using high-quality rendering.
     *
     * @param image the image to resize
     * @param width the desired width
     * @param height the desired height
     * @return the resized image
     * @throws IllegalArgumentException if width or height is non-positive
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {

        // Validate input parameters
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive values");

        // Create a new BufferedImage with the desired dimensions
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());

        // Create graphics object and configure rendering quality
        Graphics2D g = resizedImage.createGraphics();

        // Set rendering hints for better quality
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Draw the original image scaled to the new dimensions
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        // Return the resized image
        return resizedImage;
    }

    /**
     * Scales a {@link BufferedImage} by a given scale factor (float).
     *
     * @param image the image to scale
     * @param scale the scale factor
     * @return the scaled image
     */
    public static BufferedImage scale(BufferedImage image, float scale) {
        var newWidth = (int) (image.getWidth() * scale);
        var newHeight = (int) (image.getHeight() * scale);
        return resize(image, newWidth, newHeight);
    }

    /**
     * Scales a {@link BufferedImage} by a given scale factor (double).
     *
     * @param image the image to scale
     * @param scale the scale factor
     * @return the scaled image
     */
    public static BufferedImage scale(BufferedImage image, double scale) {
        var newWidth = (int) (image.getWidth() * scale);
        var newHeight = (int) (image.getHeight() * scale);
        return resize(image, newWidth, newHeight);
    }
}