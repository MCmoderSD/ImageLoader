package de.MCmoderSD.imageloader.core;

import de.MCmoderSD.imageloader.enums.Extension;

import javax.swing.ImageIcon;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Singleton class for loading and caching animated images (e.g., GIFs).
 */
@SuppressWarnings("ALL")
public class AnimationLoader {

    // Singleton instance
    private static AnimationLoader instance;

    // Cache for storing loaded ImageIcons
    private final HashMap<String, ImageIcon> cache;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private AnimationLoader() {
        cache = new HashMap<>();
    }

    /**
     * Returns the singleton instance of AnimationLoader.
     *
     * @return AnimationLoader instance
     */
    public static AnimationLoader getInstance() {
        if (instance == null) instance = new AnimationLoader();
        return instance;
    }

    /**
     * Loads an image from the given path using a relative path.
     *
     * @param path the path to the image
     * @return the loaded ImageIcon
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public ImageIcon load(String path) throws IOException, URISyntaxException {
        return load(path, false);
    }

    /**
     * Loads an image from the given path.
     *
     * @param path the path to the image
     * @param isAbsolute whether the path is absolute
     * @return the loaded ImageIcon
     * @throws IOException if the image cannot be loaded or extension is unsupported
     * @throws URISyntaxException if the path is a malformed URI
     */
    public ImageIcon load(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Check Path
        if (path.isBlank()) throw new IllegalArgumentException("Path cannot be blank");

        // Check if image is already in cache
        if (cache.containsKey(path)) return cache.get(path);

        // Reload the image
        ImageIcon image = reload(path, isAbsolute);

        // Check if image is null
        if (image == null) throw new IOException("Image could not be loaded: " + path);

        // Put image in cache
        cache.put(path, image);

        // Return the loaded image
        return image;
    }

    /**
     * Reloads an image from the given path using a relative path.
     *
     * @param path the path to the image
     * @return the reloaded ImageIcon
     * @throws IOException if the image cannot be loaded
     * @throws URISyntaxException if the path is a malformed URI
     */
    public ImageIcon reload(String path) throws IOException, URISyntaxException {
        return reload(path, false);
    }

    /**
     * Reloads an image from the given path.
     *
     * @param path the path to the image
     * @param isAbsolute whether the path is absolute
     * @return the reloaded ImageIcon
     * @throws IOException if the image cannot be loaded or extension is unsupported
     * @throws URISyntaxException if the path is a malformed URI
     */
    public ImageIcon reload(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Check Path
        if (path.isBlank()) throw new IllegalArgumentException("Path cannot be blank");

        // Validate image extension
        boolean isBase64 = path.startsWith("data:image/");
        Extension extension = isBase64 ?
                Extension.fromString(path.substring("data:image/".length(), path.indexOf(";base64")).toLowerCase()) :
                Extension.fromString(path.substring(path.lastIndexOf(".") + 1).toLowerCase());

        // Check if the extension is supported
        if (extension != Extension.GIF)
            throw new IOException("Unsupported image extension: " + extension.getExtension());

        // Load image based on path type
        if (isAbsolute) return new ImageIcon(path);                                                                     // Absolute path
        if (path.startsWith("http://") || path.startsWith("https://")) return new ImageIcon(new URI(path).toURL());     // URL path
        while (path.startsWith("/")) path = path.substring(1);                                                      // Remove leading slash
        return new ImageIcon(Objects.requireNonNull(AnimationLoader.class.getClassLoader().getResource(path)));         // Resource path
    }

    /**
     * Adds an ImageIcon to the cache.
     *
     * @param path the image path key
     * @param image the ImageIcon to store
     * @return the previous ImageIcon associated with the path, or null if none
     */
    public ImageIcon add(String path, ImageIcon image) {
        return cache.put(path, image);
    }

    /**
     * Replaces an existing ImageIcon in the cache.
     *
     * @param path the image path key
     * @param image the new ImageIcon
     * @return the previous ImageIcon associated with the path
     */
    public ImageIcon replace(String path, ImageIcon image) {
        return cache.replace(path, image);
    }

    /**
     * Removes an ImageIcon from the cache by path.
     *
     * @param path the path of the image
     * @return the removed ImageIcon
     */
    public ImageIcon remove(String path) {
        return cache.remove(path);
    }

    /**
     * Removes an ImageIcon from the cache by its value.
     *
     * @param image the ImageIcon to remove
     * @return the removed ImageIcon
     */
    public ImageIcon remove(ImageIcon image) {
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
    public HashMap<String, ImageIcon> get() {
        return cache;
    }

    /**
     * Gets an ImageIcon by its path.
     *
     * @param path the image path
     * @return the corresponding ImageIcon, or null if not found
     */
    public ImageIcon get(String path) {
        return contains(path) ? cache.get(path) : null;
    }

    /**
     * Gets the path of a given ImageIcon in the cache.
     *
     * @param bufferedImage the ImageIcon
     * @return the corresponding path, or null if not found
     */
    public String get(ImageIcon bufferedImage) {
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
     * Checks if the cache contains the given ImageIcon.
     *
     * @param bufferedImage the ImageIcon
     * @return true if present, false otherwise
     */
    public boolean contains(ImageIcon bufferedImage) {
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