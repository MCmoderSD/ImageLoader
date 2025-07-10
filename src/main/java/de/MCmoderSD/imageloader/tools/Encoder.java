package de.MCmoderSD.imageloader.tools;

import de.MCmoderSD.imageloader.enums.Extension;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;

/**
 * Utility class for encoding BufferedImages to byte arrays or Base64 strings.
 */
@SuppressWarnings("ALL")
public class Encoder {

    /**
     * Encodes a BufferedImage into a byte array using the specified image format.
     *
     * @param image     the image to encode
     * @param extension the image format (e.g., PNG, JPEG)
     * @return the encoded byte array
     * @throws IOException if encoding fails
     */
    public static byte[] encode(BufferedImage image, Extension extension) throws IOException {
        return encode(image, extension, -1f);
    }

    /**
     * Encodes a BufferedImage into a byte array with optional quality setting.
     *
     * @param image     the image to encode
     * @param extension the image format (e.g., PNG, JPEG)
     * @param quality   the compression quality (0.0 to 1.0) or -1 for default
     * @return the encoded byte array
     * @throws IOException if encoding fails
     */
    public static byte[] encode(BufferedImage image, Extension extension, float quality) throws IOException {

        // Create ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Validate image and extension
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension.getExtension());
        if (!writers.hasNext()) throw new IllegalArgumentException("No ImageWriter for format: " + extension.getExtension());
        ImageWriter writer = writers.next();

        // Set up ImageWriter
        writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed() && quality >= 0f && quality <= 1f) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }

        // Write the image
        writer.write(null, new IIOImage(image, null, null), param);

        // Clean up
        writer.dispose();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        // Return the byte array
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Converts a BufferedImage to a Base64-encoded string using default quality.
     *
     * @param image     the image to encode
     * @param extension the image format (e.g., PNG, JPEG)
     * @return the Base64-encoded data URI string
     * @throws IOException if encoding fails
     */
    public static String toBase64(BufferedImage image, Extension extension) throws IOException {
        return String.format("data:image/%s;base64,%s", extension.getExtension().toLowerCase(), Base64.getEncoder().encodeToString(encode(image, extension)));
    }

    /**
     * Converts a BufferedImage to a Base64-encoded string with specified quality.
     *
     * @param image     the image to encode
     * @param extension the image format (e.g., PNG, JPEG)
     * @param quality   the compression quality (0.0 to 1.0)
     * @return the Base64-encoded data URI string
     * @throws IOException if encoding fails
     */
    public static String toBase64(BufferedImage image, Extension extension, float quality) throws IOException {
        return String.format("data:image/%s;base64,%s", extension.getExtension().toLowerCase(), Base64.getEncoder().encodeToString(encode(image, extension, quality)));
    }
}