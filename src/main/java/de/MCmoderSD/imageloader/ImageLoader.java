package de.MCmoderSD.imageloader;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.HashMap;

/**
 * The {@code AnimationLoader} class provides methods for loading images from various sources
 * (file system, URL, or resources) and caching them to optimize performance.
 * The cache stores loaded images, allowing them to be reused without needing to reload from disk or network.
 */
@SuppressWarnings("ALL")
public class ImageLoader {

    /**
     * A cache storing images that have been loaded, mapped by their file paths.
     */
    private final HashMap<String, BufferedImage> cache = new HashMap<>();

    /**
     * Loads an image from the specified file path or URL. If the image is already cached, it is returned from the cache,
     * avoiding the need for reloading. By default, the path is treated as relative unless specified otherwise.
     *
     * <p>This method provides efficient image loading with caching to improve performance when handling multiple image requests.
     * If the image format is unsupported or the path is invalid, an appropriate exception is thrown.</p>
     *
     * @param path the file path or URL of the image to load. Can be a local file path or a web URL.
     * @return the loaded {@code BufferedImage} object, or a cached version if already loaded.
     * @throws IOException if an error occurs during loading the image, such as a missing file or unsupported format.
     * @throws URISyntaxException if the provided path is an invalid URI format.
     */
    public BufferedImage load(String path) throws IOException, URISyntaxException {
        return load(path, false);
    }

    /**
     * Loads an image from the specified file path or URL, with an option to specify whether the path is absolute or relative.
     * If the image is already cached, it is returned from the cache, bypassing the need for reloading.
     *
     * <p>This method provides efficient image loading by checking the cache first, and it supports absolute and relative paths,
     * as well as URLs. If the image format is unsupported or the path is invalid, an appropriate exception is thrown.</p>
     *
     * @param path the file path or URL of the image to load. Can be a local file path or a web URL.
     * @param isAbsolute a boolean flag indicating whether the provided path is an absolute path. If {@code false}, the path is treated as relative.
     * @return the loaded {@code BufferedImage} object, or a cached version if already loaded.
     * @throws IOException if an error occurs during loading, such as a missing file or unsupported format.
     * @throws URISyntaxException if the provided path is an invalid URI.
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
     * Loads an image from the specified source, which can be a file path, URL, or resource folder.
     * Supports loading from absolute paths, URLs, or relative paths within the classpath resource folder.
     *
     * <p>If the image is loaded from a file system, an absolute or relative path is supported. If the image is loaded
     * from a URL, the path must start with {@code http://} or {@code https://}. If the image is located in the resources folder,
     * the method will search for it within the classpath.</p>
     *
     * @param path the file path, URL, or resource location of the image. Can be a local file, web URL, or resource path.
     * @param isAbsolute a boolean flag indicating if the provided path is absolute. If {@code false}, the path is treated as relative.
     * @return the loaded {@code BufferedImage} object.
     * @throws IOException if an error occurs during loading, such as a missing file, invalid path, or unsupported image format.
     * @throws URISyntaxException if the provided path is an invalid URI format (when loading from a URL).
     */
    public static BufferedImage loadImage(String path, boolean isAbsolute) throws IOException, URISyntaxException {

        // Validates the image path and loads accordingly
        if (path == null || path.isEmpty() || path.isBlank()) throw new IOException("Image path is null or empty.");
        if (path.endsWith(".")) throw new IOException("Image path is missing file extension: " + path);

        // Ensure image format is supported
        if (!Arrays.asList("bmp", "gif", "hdr", "jpeg", "jpg", "png", "tiff", "webp").contains(getExtension(path))) throw new IOException("Unsupported image format: " + getExtension(path));

        // Load image based on path type
        if (isAbsolute) return ImageIO.read(new File(path));
        else if (path.startsWith("http://") || path.startsWith("https://")) return ImageIO.read(new URI(path).toURL());
        else {
            while (path.startsWith("/")) path = path.substring(1);
            return ImageIO.read(ImageLoader.class.getClassLoader().getResource(path));
        }
    }

    /**
     * Extracts and returns the file extension from the provided file path.
     *
     * @param path the file path of the image.
     * @return the file extension in lowercase.
     */
    public static String getExtension(String path) {
        var queryIndex = path.indexOf('?');
        if (queryIndex != -1) path = path.substring(0, queryIndex);
        return path.substring(path.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Adds a {@code BufferedImage} to the cache with the specified path as its key.
     *
     * @param path the file path or key for the image.
     * @param image the {@code BufferedImage} to cache.
     */
    public void add(String path, BufferedImage image) {
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
     * Removes an image from the cache based on its {@code BufferedImage}.
     *
     * @param image the {@code BufferedImage} to be removed from the cache.
     */
    public void remove(BufferedImage image) {
        cache.remove(get(image));
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
        if (contains(path)) return cache.get(path);
        else return null;
    }

    /**
     * Retrieves a specific path from the cache based on its {@code BufferedImage}.
     *
     * @param image the {@code BufferedImage} to search for in the cache.
     * @return the file path or key for the image, or {@code null} if not present.
     */
    public String get(BufferedImage image) {
        if (contains(image)) for (String path : cache.keySet()) if (cache.get(path).equals(image)) return path;
        return null;
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