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
import java.util.HashMap;
import java.util.Objects;

/**
 * Singleton class for loading and caching static images in BufferedImage format.
 */
@SuppressWarnings("ALL")
public class ImageLoader {

    // Singleton instance
    private static ImageLoader instance;

    // Cache for storing loaded BufferedImages
    private final HashMap<String, BufferedImage> cache;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ImageLoader() {
        cache = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ImageLoader.
     *
     * @return ImageLoader instance
     */
    public static ImageLoader getInstance() {
        if (instance == null) instance = new ImageLoader();
        return instance;
    }

    /**
     * Loads an image from the given path using a relative path.
     *
     * @param path the path to the image
     * @return the loaded BufferedImage
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public BufferedImage load(String path) throws IOException, URISyntaxException {
        return load(path, false);
    }

    /**
     * Loads an image from the given path.
     *
     * @param path the path to the image
     * @param isAbsolute whether the path is absolute
     * @return the loaded BufferedImage
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public BufferedImage load(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Check Path
        if (path.isBlank()) throw new IllegalArgumentException("Path cannot be blank");

        // Check if image is already in cache
        if (cache.containsKey(path)) return cache.get(path);

        // Reload the image
        BufferedImage image = reload(path, isAbsolute);

        // Check if image is null
        if (image == null) throw new IOException("Image could not be loaded: " + path);

        // Put the image in cache
        cache.put(path, image);

        // Return the loaded image
        return image;
    }

    /**
     * Reloads an image from the given path using a relative path.
     *
     * @param path the path to the image
     * @return the reloaded BufferedImage
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public BufferedImage reload(String path) throws IOException, URISyntaxException {
        return reload(path, false);
    }

    /**
     * Reloads an image from the given path.
     *
     * @param path the path to the image
     * @param isAbsolute whether the path is absolute
     * @return the reloaded BufferedImage
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public BufferedImage reload(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Check Path
        if (path.isBlank()) throw new IllegalArgumentException("Path cannot be blank");

        // Validate image extension
        boolean isBase64 = path.startsWith("data:image/");
        Extension extension = isBase64 ?
                Extension.fromString(path.substring("data:image/".length(), path.indexOf(";base64")).toLowerCase()) :
                Extension.fromString(path.substring(path.lastIndexOf(".") + 1).toLowerCase());

        // Check if extension is supported
        if (extension == null) throw new IOException("Unsupported image format: " + path);

        // Load the image based on the path type
        if (isAbsolute) return ImageIO.read(new File(path));                                                                                        // Absolute path
        if (path.startsWith("http://") || path.startsWith("https://")) return ImageIO.read(new URI(path).toURL());                                  // URL path
        if (isBase64) return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(path.substring(path.indexOf(",") + 1))));         // Base64 encoded image
        while (path.startsWith("/")) path = path.substring(1);                                                                                  // Remove leading slashes
        return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getClassLoader().getResourceAsStream(path)));                                  // Resource path
    }

    /**
     * Adds a BufferedImage to the cache.
     *
     * @param path the image path key
     * @param image the BufferedImage to store
     * @return the previous BufferedImage associated with the path, or null if none
     */
    public BufferedImage add(String path, BufferedImage image) {
        return cache.put(path, image);
    }

    /**
     * Replaces an existing BufferedImage in the cache.
     *
     * @param path the image path key
     * @param image the new BufferedImage
     * @return the previous BufferedImage associated with the path
     */
    public BufferedImage replace(String path, BufferedImage image) {
        return cache.replace(path, image);
    }

    /**
     * Removes a BufferedImage from the cache by path.
     *
     * @param path the path of the image
     * @return the removed BufferedImage
     */
    public BufferedImage remove(String path) {
        return cache.remove(path);
    }

    /**
     * Removes a BufferedImage from the cache by its value.
     *
     * @param image the BufferedImage to remove
     * @return the removed BufferedImage
     */
    public BufferedImage remove(BufferedImage image) {
        return cache.remove(get(image));
    }

    /**
     * Clears the entire image cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Returns the entire cache.
     *
     * @return the image cache
     */
    public HashMap<String, BufferedImage> get() {
        return cache;
    }

    /**
     * Gets a BufferedImage by its path.
     *
     * @param path the image path
     * @return the corresponding BufferedImage, or null if not found
     */
    public BufferedImage get(String path) {
        return contains(path) ? cache.get(path) : null;
    }

    /**
     * Gets the path of a given BufferedImage in the cache.
     *
     * @param bufferedImage the BufferedImage
     * @return the corresponding path, or null if not found
     */
    public String get(BufferedImage bufferedImage) {
        if (contains(bufferedImage))
            for (String path : cache.keySet())
                if (cache.get(path).equals(bufferedImage))
                    return path;
        return null;
    }

    /**
     * Checks if the cache contains an entry for the given path.
     *
     * @param path the image path
     * @return true if present, false otherwise
     */
    public boolean contains(String path) {
        return cache.containsKey(path);
    }

    /**
     * Checks if the cache contains the given BufferedImage.
     *
     * @param bufferedImage the BufferedImage
     * @return true if present, false otherwise
     */
    public boolean contains(BufferedImage bufferedImage) {
        return cache.containsValue(bufferedImage);
    }

    /**
     * Checks if the cache is empty.
     *
     * @return true if the cache is empty, false otherwise
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Returns the number of cached images.
     *
     * @return the size of the cache
     */
    public int size() {
        return cache.size();
    }
}