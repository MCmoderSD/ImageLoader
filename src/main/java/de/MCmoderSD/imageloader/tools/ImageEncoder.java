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

@SuppressWarnings("unused")
public class ImageEncoder {

    public static byte[] encode(BufferedImage image, Extension extension) {
        return encode(image, extension, -1f);
    }

    public static byte[] encode(BufferedImage image, Extension extension, float quality) {

        // Validate input
        if (image == null) throw new IllegalArgumentException("Image cannot be null");
        if (extension == null) throw new IllegalArgumentException("Extension cannot be null");

        // Create ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Validate image and extension
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension.getExtension());
        if (!writers.hasNext()) throw new IllegalArgumentException("No ImageWriter for format: " + extension.getExtension());
        ImageWriter writer = writers.next();

        // Write image to ByteArrayOutputStream
        try (var outputStream = ImageIO.createImageOutputStream(byteArrayOutputStream)) {

            // Set up ImageWriter
            writer.setOutput(outputStream);
            ImageWriteParam param = writer.getDefaultWriteParam();

            // Set compression quality if supported and valid
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

            // Return the encoded image as a byte array
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to encode image: " + e.getMessage(), e);
        }
    }

    public static String toBase64(BufferedImage image, Extension extension) {
        return String.format("data:image/%s;base64,%s", extension.getExtension().toLowerCase(), Base64.getEncoder().encodeToString(encode(image, extension)));
    }

    public static String toBase64(BufferedImage image, Extension extension, float quality) {
        return String.format("data:image/%s;base64,%s", extension.getExtension().toLowerCase(), Base64.getEncoder().encodeToString(encode(image, extension, quality)));
    }
}