package de.MCmoderSD.imageloader.core;

import de.MCmoderSD.imageloader.enums.Extension;
import de.MCmoderSD.tools.GZIP;

import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import static de.MCmoderSD.imageloader.enums.Extension.GIF;

@SuppressWarnings("unused")
public class AnimationLoader {

    // Singleton instance
    private static AnimationLoader instance;

    // Attributes
    private final ConcurrentHashMap<String, byte[]> cache;
    private final Base64.Decoder base64Decoder;

    // Constructor
    private AnimationLoader() {
        cache = new ConcurrentHashMap<>();
        base64Decoder = Base64.getDecoder();
    }

    // Get Singleton Instance
    public static AnimationLoader getInstance() {
        if (instance == null) instance = new AnimationLoader();
        return instance;
    }

    // Helper Methods
    private static byte[] deflate(ImageIcon image) {
        try {
            return GZIP.deflateObject(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deflate image", e);
        }
    }

    private static ImageIcon inflate(byte[] compressedData) {
        try {
            return (ImageIcon) GZIP.inflateObject(compressedData);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to inflate image", e);
        }
    }

    // Read Methods
    private ImageIcon readResource(String resourcePath) {

        // Load image from resource
        try (var resource = ImageLoader.class.getResourceAsStream(resourcePath)) {

            // Check if resource exists
            if (resource == null) throw new IOException("Resource not found: " + resourcePath);

            // Read all data from the resource stream
            byte[] imageData = resource.readAllBytes();

            // Validate that data was read
            if (imageData.length == 0) throw new IOException("Resource is empty: " + resourcePath);

            // Parse and return image
            return new ImageIcon(imageData);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load image from resource: " + resourcePath, e);
        }
    }

    private ImageIcon readURL(String url) {

        // Load image from URL
        try {
            return new ImageIcon(new URI(url).toURL());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to load image from URL: " + url, e);
        }
    }

    private ImageIcon readFile(String filePath) {

        // Load image from File
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) throw new IllegalArgumentException("File not found: " + filePath);

        // Return image
        return new ImageIcon(filePath);
    }

    private ImageIcon readBase64(byte[] data) {

        // Validate Base64 data
        if (data == null || data.length == 0) throw new IllegalArgumentException("Base64 data cannot be null or empty");

        // Load image from Base64 data
        return new ImageIcon(data);
    }

    public ImageIcon loadResource(String resourcePath) {

        // Check Parameters
        if (resourcePath == null || resourcePath.isBlank()) throw new IllegalArgumentException("Resource path cannot be null or blank");

        // Validate image extension
        if (Extension.fromString(resourcePath.substring(resourcePath.lastIndexOf(".") + 1)) != GIF) throw new IllegalArgumentException("Unsupported image format: " + resourcePath);

        // Check Cache
        if (cache.containsKey(resourcePath)) return inflate(cache.get(resourcePath));

        // Load image and cache it
        ImageIcon image = readResource(resourcePath);

        // Cache
        cache.put(resourcePath, deflate(image));

        // Return image
        return image;
    }

    public ImageIcon loadURL(String url) {

        // Check Parameters
        if (url == null || url.isBlank()) throw new IllegalArgumentException("URL cannot be null or blank");

        // Validate image extension
        if (Extension.fromString(url.substring(url.lastIndexOf(".") + 1)) != GIF) throw new IllegalArgumentException("Unsupported image format: " + url);

        // Check Cache
        if (cache.containsKey(url)) return inflate(cache.get(url));

        // Load image and cache it
        ImageIcon image = readURL(url);

        // Cache
        cache.put(url, deflate(image));

        // Return image
        return image;
    }

    public ImageIcon loadFile(String filePath) {

        // Check Parameters
        if (filePath == null || filePath.isBlank()) throw new IllegalArgumentException("File path cannot be null or blank");

        // Validate image extension
        if (Extension.fromString(filePath.substring(filePath.lastIndexOf(".") + 1)) != GIF)  throw new IllegalArgumentException("Unsupported image format: " + filePath);

        // Check Cache
        if (cache.containsKey(filePath)) return inflate(cache.get(filePath));

        // Load image and cache it
        ImageIcon image = readFile(filePath);

        // Cache
        cache.put(filePath, deflate(image));

        // Return image
        return image;
    }

    public ImageIcon loadBase64(String base64) {

        // Check Parameters
        if (base64 == null || base64.isBlank()) throw new IllegalArgumentException("Base64 string cannot be null or blank");

        // Validate Base64 format
        if (!base64.startsWith("data:image/") || !base64.contains(";base64,")) throw new IllegalArgumentException("Invalid Base64 image format - expected format: data:image/{extension};base64,{data}");

        // Validate image extension
        String extensionPart = base64.substring("data:image/".length(), base64.indexOf(";base64")).toLowerCase();
        if (Extension.fromString(extensionPart) != GIF) throw new IllegalArgumentException("Unsupported image format in Base64 string: " + extensionPart);

        // Extract the actual Base64 data
        String base64Data = base64.substring(base64.indexOf(",") + 1);

        // Check Cache
        if (cache.containsKey(base64)) return inflate(cache.get(base64));

        // Load image and cache it
        ImageIcon image = readBase64(base64Decoder.decode(base64Data));

        // Cache
        cache.put(base64, deflate(image));

        // Return image
        return image;
    }

    // Reload Methods
    public ImageIcon reloadResource(String resourcePath) {
        cache.remove(resourcePath);
        return loadResource(resourcePath);
    }

    public ImageIcon reloadURL(String url) {
        cache.remove(url);
        return loadURL(url);
    }

    public ImageIcon reloadFile(String filePath) {
        cache.remove(filePath);
        return loadFile(filePath);
    }

    public ImageIcon reloadBase64(String base64) {
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