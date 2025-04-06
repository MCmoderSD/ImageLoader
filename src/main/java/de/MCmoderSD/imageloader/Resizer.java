package de.MCmoderSD.imageloader;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Utility class for resizing {@link BufferedImage} objects.
 * Provides methods to resize images to specific dimensions or by scale factor.
 * Uses bilinear interpolation and anti-aliasing for high-quality resizing.
 */
@SuppressWarnings("ALL")
public class Resizer {

    /**
     * Resizes an image to the specified width and height.
     *
     * @param image  the source image to resize
     * @param width  the target width in pixels
     * @param height the target height in pixels
     * @return a new BufferedImage with the specified dimensions
     * @throws IllegalArgumentException if width or height is less than or equal to zero
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {

        // Validate input parameters
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive values");

        // Create a new BufferedImage with the desired dimensions
        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());

        // Create graphics object and configure rendering quality
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the original image scaled to the new dimensions
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        // Return the resized image
        return resizedImage;
    }

    /**
     * Resizes an image by a scale factor.
     * For example, a scale of 0.5 will reduce the image to half its original size,
     * while a scale of 2.0 will double the image size.
     *
     * @param image the source image to resize
     * @param scale the scale factor to apply (positive float value)
     * @return a new BufferedImage with dimensions adjusted by the scale factor
     */
    public static BufferedImage resize(BufferedImage image, float scale) {
        var newWidth = (int) (image.getWidth() * scale);
        var newHeight = (int) (image.getHeight() * scale);
        return resize(image, newWidth, newHeight);
    }

    /**
     * Resizes an image to a square with the specified size.
     * Both width and height will be set to the provided size.
     *
     * @param image the source image to resize
     * @param size  the size in pixels for both width and height
     * @return a new square BufferedImage with the specified size
     * @throws IllegalArgumentException if size is less than or equal to zero
     */
    public static BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }
}