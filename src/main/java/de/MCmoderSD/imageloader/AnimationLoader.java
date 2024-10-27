package de.MCmoderSD.imageloader;

import javax.swing.ImageIcon;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Utility class for loading and managing animations as {@link ImageIcon} objects.
 * It supports loading from local resources or external URLs with caching functionality.
 * Only "gif" format is currently supported for animations.
 */
@SuppressWarnings("ALL")
public class AnimationLoader {

    private final HashMap<String, ImageIcon> cache = new HashMap<>();

    /**
     * Loads an animation from the specified path, using caching if available.
     *
     * @param path the path to the animation resource
     * @return the loaded {@link ImageIcon} object
     * @throws IOException if an I/O error occurs or the animation format is unsupported
     * @throws URISyntaxException if the path is malformed
     */
    public ImageIcon load(String path) throws IOException, URISyntaxException {
        return load(path, false);
    }

    /**
     * Loads an animation from the specified path, with an option to load an absolute path.
     *
     * @param path       the path to the animation resource
     * @param isAbsolute true if the path is an absolute file path; false for relative paths or URLs
     * @return the loaded {@link ImageIcon} object
     * @throws IOException if an I/O error occurs or the animation format is unsupported
     * @throws URISyntaxException if the path is malformed
     */
    public ImageIcon load(String path, boolean isAbsolute) throws IOException, URISyntaxException {
        if (cache.containsKey(path)) return cache.get(path);

        String extension = getExtension(path);
        ImageIcon animation = switch (extension) {
            case "gif" -> loadAnimation(path, isAbsolute);
            default -> throw new IOException("Unsupported animation format: " + extension);
        };

        if (animation == null) throw new IOException("Animation could not be loaded: " + path);

        cache.put(path, animation);
        return animation;
    }

    /**
     * Loads an animation from the specified path.
     *
     * @param path       the path to the animation resource
     * @param isAbsolute true if the path is an absolute file path; false for relative paths or URLs
     * @return the loaded {@link ImageIcon} object
     * @throws IOException if the path is invalid or unsupported
     * @throws URISyntaxException if the path is malformed
     */
    public static ImageIcon loadAnimation(String path, boolean isAbsolute) throws IOException, URISyntaxException {
        if (path == null || path.isEmpty() || path.isBlank()) throw new IOException("Animation path is null or empty.");
        if (path.endsWith(".")) throw new IOException("Animation path is missing file extension: " + path);

        if (!Arrays.asList("gif").contains(getExtension(path))) throw new IOException("Unsupported animation format: " + getExtension(path));

        if (isAbsolute) return new ImageIcon(path);
        else if (path.startsWith("http://") || path.startsWith("https://")) return new ImageIcon(new URI(path).toURL());
        else {
            while (path.startsWith("/")) path = path.substring(1);
            return new ImageIcon(Objects.requireNonNull(AnimationLoader.class.getClassLoader().getResource(path)));
        }
    }

    /**
     * Retrieves the file extension from the provided path.
     *
     * @param path the path to extract the extension from
     * @return the file extension as a lowercase string
     */
    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Adds an animation to the cache with the specified path as its key.
     *
     * @param path      the path key for the animation
     * @param animation the {@link ImageIcon} animation to add
     */
    public void add(String path, ImageIcon animation) {
        cache.put(path, animation);
    }

    /**
     * Replaces an existing animation in the cache with a new one.
     *
     * @param path      the path key of the animation to replace
     * @param animation the new {@link ImageIcon} animation
     */
    public void replace(String path, ImageIcon animation) {
        cache.replace(path, animation);
    }

    /**
     * Removes an animation from the cache based on the specified path.
     *
     * @param path the path key of the animation to remove
     */
    public void remove(String path) {
        cache.remove(path);
    }

    /**
     * Removes an animation from the cache based on the {@link ImageIcon} object.
     *
     * @param animation the {@link ImageIcon} object to remove
     */
    public void remove(ImageIcon animation) {
        cache.remove(get(animation));
    }

    /**
     * Clears all entries from the animation cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Retrieves the entire animation cache.
     *
     * @return the animation cache as a {@link HashMap}
     */
    public HashMap<String, ImageIcon> get() {
        return cache;
    }

    /**
     * Retrieves an animation from the cache based on the specified path.
     *
     * @param path the path key of the animation to retrieve
     * @return the cached {@link ImageIcon} object, or null if not found
     */
    public ImageIcon get(String path) {
        return cache.get(path);
    }

    /**
     * Retrieves the path key for a given {@link ImageIcon} animation in the cache.
     *
     * @param animation the {@link ImageIcon} object to search for
     * @return the path key of the animation, or null if not found
     */
    public String get(ImageIcon animation) {
        if (contains(animation)) for (String path : cache.keySet()) if (cache.get(path).equals(animation)) return path;
        return null;
    }

    /**
     * Checks if an animation is present in the cache based on its path.
     *
     * @param path the path key to check
     * @return true if the cache contains the path; false otherwise
     */
    public boolean contains(String path) {
        return cache.containsKey(path);
    }

    /**
     * Checks if an animation is present in the cache based on its {@link ImageIcon} object.
     *
     * @param animation the {@link ImageIcon} object to check
     * @return true if the cache contains the animation; false otherwise
     */
    public boolean contains(ImageIcon animation) {
        return cache.containsValue(animation);
    }

    /**
     * Checks if the animation cache is empty.
     *
     * @return true if the cache is empty; false otherwise
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Returns the number of animations in the cache.
     *
     * @return the size of the cache
     */
    public int size() {
        return cache.size();
    }
}