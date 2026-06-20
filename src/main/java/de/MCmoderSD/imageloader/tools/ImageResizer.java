package de.MCmoderSD.imageloader.tools;

import java.awt.image.BufferedImage;

import static java.awt.RenderingHints.*;

@SuppressWarnings("unused")
public class ImageResizer {


    public static BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {

        // Validate input parameters
        if (image == null) throw new IllegalArgumentException("Image cannot be null");
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Width and height must be positive values");

        // Create a new BufferedImage with the desired dimensions
        var resizedImage = new BufferedImage(width, height, image.getType());

        // Create graphics object and configure rendering quality
        var g = resizedImage.createGraphics();

        // Set rendering hints for better quality
        g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(KEY_DITHERING, VALUE_DITHER_ENABLE);
        g.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE);

        // Draw the original image scaled to the new dimensions
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        // Return the resized image
        return resizedImage;
    }

    public static BufferedImage scale(BufferedImage image, float scale) {

        // Validate input parameters
        if (image == null) throw new IllegalArgumentException("Image cannot be null");
        if (scale <= 0f) throw new IllegalArgumentException("Scale must be a positive value");

        // Calculate new dimensions based on the scale factor
        var newWidth = (int) (image.getWidth() * scale);
        var newHeight = (int) (image.getHeight() * scale);

        // Return the resized image
        return resize(image, newWidth, newHeight);
    }

    public static BufferedImage scale(BufferedImage image, double scale) {

        // Validate input parameters
        if (image == null) throw new IllegalArgumentException("Image cannot be null");
        if (scale <= 0d) throw new IllegalArgumentException("Scale must be a positive value");

        // Calculate new dimensions based on the scale factor
        var newWidth = (int) (image.getWidth() * scale);
        var newHeight = (int) (image.getHeight() * scale);

        // Return the resized image
        return resize(image, newWidth, newHeight);
    }
}