package de.MCmoderSD.imageloader.core;

import de.MCmoderSD.imageloader.enums.Extension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public class ImageLoader {

    // Singleton instance
    private static ImageLoader instance;

    // Attributes
    private final ConcurrentHashMap<String, BufferedImage> cache;
    private final Base64.Decoder base64Decoder;

    // Constructor
    private ImageLoader() {
        cache = new ConcurrentHashMap<>();
        base64Decoder = Base64.getDecoder();
    }

    // Get Singleton Instance
    public static ImageLoader getInstance() {
        if (instance == null) instance = new ImageLoader();
        return instance;
    }

    // Helper Methods
    private static boolean isValidImageExtension(String extension) {
        try {
            Extension.fromString(extension);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Read Methods
    private BufferedImage readResource(String resourcePath) {

        // Load image from resource
        try (var resource = ImageLoader.class.getResourceAsStream(resourcePath)) {

            // Check if resource exists
            if (resource == null) throw new IOException("Resource not found: " + resourcePath);

            // Parse and return image
            return ImageIO.read(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from resource: " + resourcePath, e);
        }
    }

    private BufferedImage readURL(String url) {

        // Load image from URL
        try (var stream = new URI(url).toURL().openStream()) {
            return ImageIO.read(stream);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to load image from URL: " + url, e);
        }
    }

    private BufferedImage readFile(String filePath) {

        // Load image from File
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) throw new IllegalArgumentException("File not found: " + filePath);

        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from file: " + filePath, e);
        }
    }

    private BufferedImage readBase64(byte[] data) {

        // Validate Base64 data
        if (data == null || data.length == 0) throw new IllegalArgumentException("Base64 data cannot be null or empty");

        // Load image from Base64 data
        try (var inputStream = new ByteArrayInputStream(data)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from Base64 data", e);
        }
    }

    public BufferedImage loadResource(String resourcePath) {

        // Check Parameters
        if (resourcePath == null || resourcePath.isBlank()) throw new IllegalArgumentException("Resource path cannot be null or blank");

        // Validate image extension
        if (!isValidImageExtension(resourcePath.substring(resourcePath.lastIndexOf(".") + 1))) throw new IllegalArgumentException("Unsupported image format: " + resourcePath);

        // Check Cache
        if (cache.containsKey(resourcePath)) return cache.get(resourcePath);

        // Load image and cache it
        BufferedImage image = readResource(resourcePath);

        // Check if image was loaded successfully
        if (image == null) throw new RuntimeException("Failed to load image from resource: " + resourcePath);

        // Cache
        cache.put(resourcePath, image);

        // Return image
        return image;
    }

    public BufferedImage loadURL(String url) {

        // Check Parameters
        if (url == null || url.isBlank()) throw new IllegalArgumentException("URL cannot be null or blank");

        // Validate image extension
        if (!isValidImageExtension(url.substring(url.lastIndexOf(".") + 1))) throw new IllegalArgumentException("Unsupported image format: " + url);

        // Check Cache
        if (cache.containsKey(url)) return cache.get(url);

        // Load image and cache it
        BufferedImage image = readURL(url);

        // Check if image was loaded successfully
        if (image == null) throw new RuntimeException("Failed to load image from URL: " + url);

        // Cache
        cache.put(url, image);

        // Return image
        return image;
    }

    public BufferedImage loadFile(String filePath) {

        // Check Parameters
        if (filePath == null || filePath.isBlank()) throw new IllegalArgumentException("File path cannot be null or blank");

        // Validate image extension
        if (!isValidImageExtension(filePath.substring(filePath.lastIndexOf(".") + 1))) throw new IllegalArgumentException("Unsupported image format: " + filePath);

        // Check Cache
        if (cache.containsKey(filePath)) return cache.get(filePath);

        // Load image and cache it
        BufferedImage image = readFile(filePath);

        // Check if image was loaded successfully
        if (image == null) throw new RuntimeException("Failed to load image from file: " + filePath);

        // Cache
        cache.put(filePath, image);

        // Return image
        return image;
    }

    public BufferedImage loadBase64(String base64) {

        // Check Parameters
        if (base64 == null || base64.isBlank()) throw new IllegalArgumentException("Base64 string cannot be null or blank");

        // Validate Base64 format
        if (!base64.startsWith("data:image/") || !base64.contains(";base64,")) throw new IllegalArgumentException("Invalid Base64 image format - expected format: data:image/{extension};base64,{data}");

        // Validate image extension
        String extensionPart = base64.substring("data:image/".length(), base64.indexOf(";base64")).toLowerCase();
        if (!isValidImageExtension(extensionPart)) throw new IllegalArgumentException("Unsupported image format in Base64 string: " + extensionPart);

        // Extract the actual Base64 data
        String base64Data = base64.substring(base64.indexOf(",") + 1);

        // Check Cache
        if (cache.containsKey(base64)) return cache.get(base64);

        // Load image and cache it
        BufferedImage image = readBase64(base64Decoder.decode(base64Data));

        // Check if image was loaded successfully
        if (image == null) throw new RuntimeException("Failed to load image from Base64 string");

        // Cache
        cache.put(base64, image);

        // Return image
        return image;
    }

    // Reload Methods
    public BufferedImage reloadResource(String resourcePath) {
        cache.remove(resourcePath);
        return loadResource(resourcePath);
    }

    public BufferedImage reloadURL(String url) {
        cache.remove(url);
        return loadURL(url);
    }

    public BufferedImage reloadFile(String filePath) {
        cache.remove(filePath);
        return loadFile(filePath);
    }

    public BufferedImage reloadBase64(String base64) {
        cache.remove(base64);
        return loadBase64(base64);
    }

    // Setter
    public void clear() {
        cache.clear();
    }

    // Getter
    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }
}