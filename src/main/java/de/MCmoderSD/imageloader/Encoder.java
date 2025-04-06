package de.MCmoderSD.imageloader;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Base64;
import java.util.stream.Stream;

/**
 * Utility class for encoding {@link BufferedImage} objects into different formats.
 * Supports encoding images to byte arrays, Base64 strings, and data URIs.
 * Supported formats include JPG, JPEG, PNG, BMP, GIF, and TIFF.
 */
@SuppressWarnings("ALL")
public class Encoder {

    /**
     * Encodes a BufferedImage to a byte array in the specified format.
     *
     * @param image  the image to encode
     * @param format the target format (jpg, jpeg, png, bmp, gif, or tiff)
     * @return byte array containing the encoded image data, or null if input parameters are invalid
     * @throws IOException if an I/O error occurs during encoding
     * @throws IllegalArgumentException if the specified format is not supported
     */
    public static byte[] encodeToBytes(BufferedImage image, String format) throws IOException {

        // Validate input parameters
        if (image == null) return null;
        if (format == null || format.isEmpty()) return null;
        if (Stream.of("jpg", "jpeg", "png", "bmp", "gif", "tiff").noneMatch(format::equalsIgnoreCase)) throw new IllegalArgumentException("Unsupported image format: " + format);

        // Use proper image conversion before encoding if necessary
        BufferedImage convertedImage = switch (format.toLowerCase()) {
            case "jpg", "jpeg" -> Converter.convertToJPEG(image);
            case "png" -> Converter.convertToPNG(image);
            case "bmp" -> Converter.convertToBMP(image);
            case "gif" -> Converter.convertToGIF(image);
            case "tiff" -> Converter.convertToTIFF(image);
            default -> image;
        };

        // Encode the image to bytes
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(convertedImage, format, outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Encodes a BufferedImage to a Base64 string in the specified format.
     *
     * @param image  the image to encode
     * @param format the target format (jpg, jpeg, png, bmp, gif, or tiff)
     * @return Base64 string representation of the image, or null if encoding failed
     * @throws IOException if an I/O error occurs during encoding
     * @throws IllegalArgumentException if the specified format is not supported
     */
    public static String encodeToBase64(BufferedImage image, String format) throws IOException {
        byte[] bytes = encodeToBytes(image, format);
        if (bytes == null) return null;
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Encodes a BufferedImage to a data URI string in the specified format.
     * The resulting string can be used directly in HTML img tags or CSS.
     *
     * @param image  the image to encode
     * @param format the target format (jpg, jpeg, png, bmp, gif, or tiff)
     * @return data URI string containing the encoded image data, or null if encoding failed
     * @throws IOException if an I/O error occurs during encoding
     * @throws IllegalArgumentException if the specified format is not supported
     */
    public static String encodeToDataURI(BufferedImage image, String format) throws IOException {
        String base64 = encodeToBase64(image, format);
        if (base64 == null) return null;
        return "data:image/" + format + ";base64," + base64;
    }
}