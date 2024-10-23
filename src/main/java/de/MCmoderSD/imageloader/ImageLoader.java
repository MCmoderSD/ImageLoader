/**
 * Package containing utilities for handling image loading and caching.
 */
package de.MCmoderSD.imageloader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The {@code ImageLoader} class provides methods for loading images from various sources
 * (file system, URL, or resources) and caching them to optimize performance. 
 * The cache stores loaded images, allowing them to be reused without needing to reload from disk or network.
 */
@SuppressWarnings("ALL")
public class ImageLoader {

    /**
     * A cache storing images that have been loaded, mapped by their file paths.
     */
    public HashMap<String, BufferedImage> cache = new HashMap<>();

    /**
     * Loads an image from the specified path. If the image is cached, it will be returned from the cache.
     * The method defaults to treating the path as relative unless specified otherwise.
     *
     * @param path the file path or URL of the image to load.
     * @return the loaded {@code BufferedImage} object.
     * @throws IOException if an error occurs during loading the image.
     * @throws URISyntaxException if the path provided is an invalid URI.
     */
    public BufferedImage load(String path) throws IOException, URISyntaxException {
        return load(path, false);
    }

    /**
     * Loads an image from the specified path. If the image is cached, it will be returned from the cache.
     * The path can be absolute or relative based on the {@code isAbsolute} parameter.
     *
     * @param path the file path or URL of the image to load.
     * @param isAbsolute a boolean flag indicating if the path is absolute.
     * @return the loaded {@code BufferedImage} object.
     * @throws IOException if an error occurs during loading the image or if the image format is unsupported.
     * @throws URISyntaxException if the path provided is an invalid URI.
     */
    public BufferedImage load(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Check if image is already in cache
        if (cache.containsKey(path)) return cache.get(path);

        // Get file extension
        String extension = getExtension(path);

        // Load image based on extension
        BufferedImage image = switch (extension) {
            case "bmp", "gif", "hdr", "jpeg", "jpg", "png", "tiff", "webp" -> loadImage(path, isAbsolute);
            default -> throw new IOException("Unsupported image format: " + extension);
        };

        // Check if image is null
        if (image == null) throw new IOException("Image could not be loaded: " + path);

        // Put image in cache
        cache.put(path, image);
        return image;
    }

    /**
     * Loads an image from a file path, URL, or resource folder. This is a static utility method that supports
     * loading from absolute paths, URLs, or relative paths within the resources folder.
     *
     * @param path the file path, URL, or resource location of the image.
     * @param isAbsolute a boolean flag indicating if the path is absolute.
     * @return the loaded {@code BufferedImage} object.
     * @throws IOException if an error occurs during loading the image or if the path is invalid.
     * @throws URISyntaxException if the path is an invalid URI.
     */
    public static BufferedImage loadImage(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Validates the image path and loads accordingly
        if (path == null || path.isEmpty() || path.isBlank()) throw new IOException("Image path is null or empty.");
        if (path.endsWith(".")) throw new IOException("Image path is missing file extension: " + path);

        // Ensure image format is supported
        if (!Arrays.asList("bmp", "gif", "hdr", "jpeg", "jpg", "png", "tiff", "webp").contains(getExtension(path)))
            throw new IOException("Unsupported image format: " + getExtension(path));

        // Load image from different sources based on the path
        if (isAbsolute) {
            File file = new File(path);
            if (!file.exists()) throw new IOException("Image file not found: " + path);
            return ImageIO.read(file);
        } else if (path.startsWith("http://") || path.startsWith("https://")) {
            URI uri = new URI(path);
            return ImageIO.read(uri.toURL());
        } else {
            if (path.startsWith("/")) path = path.substring(1);
            URL resource = ImageLoader.class.getClassLoader().getResource(path);
            if (resource == null) throw new IOException("Image resource not found: " + path);
            return ImageIO.read(resource);
        }
    }

    /**
     * Extracts and returns the file extension from the provided file path.
     *
     * @param path the file path of the image.
     * @return the file extension in lowercase.
     */
    private static String getExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Adds a {@code BufferedImage} to the cache with the specified path as its key.
     *
     * @param path the file path or key for the image.
     * @param image the {@code BufferedImage} to cache.
     */
    public void put(String path, BufferedImage image) {
        cache.put(path, image);
    }

    /**
     * Replaces an existing image in the cache with a new {@code BufferedImage} for the specified path.
     *
     * @param path the file path or key for the image.
     * @param image the new {@code BufferedImage} to replace the old one.
     */
    public void replace(String path, BufferedImage image) {
        cache.replace(path, image);
    }

    /**
     * Removes an image from the cache for the specified path.
     *
     * @param path the file path or key for the image to be removed.
     */
    public void remove(String path) {
        cache.remove(path);
    }

    /**
     * Clears all cached images from the cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves the entire cache of images as a {@code HashMap}.
     *
     * @return a {@code HashMap} containing all cached images.
     */
    public HashMap<String, BufferedImage> get() {
        return cache;
    }

    /**
     * Retrieves a specific image from the cache based on its path.
     *
     * @param path the file path or key for the image.
     * @return the cached {@code BufferedImage}, or {@code null} if not present.
     */
    public BufferedImage get(String path) {
        return cache.get(path);
    }

    /**
     * Checks if an image is present in the cache based on its path.
     *
     * @param path the file path or key for the image.
     * @return {@code true} if the image is present in the cache, {@code false} otherwise.
     */
    public boolean contains(String path) {
        return cache.containsKey(path);
    }

    /**
     * Checks if a specific {@code BufferedImage} is present in the cache.
     *
     * @param image the {@code BufferedImage} to search for in the cache.
     * @return {@code true} if the image is present in the cache, {@code false} otherwise.
     */
    public boolean contains(BufferedImage image) {
        return cache.containsValue(image);
    }

    /**
     * Checks if the cache is empty.
     *
     * @return {@code true} if the cache is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Returns the number of images currently cached.
     *
     * @return the size of the cache.
     */
    public int size() {
        return cache.size();
    }
}