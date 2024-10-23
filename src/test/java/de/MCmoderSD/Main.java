package de.MCmoderSD;

import de.MCmoderSD.imageloader.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
        try {

            // Initialize ImageLoader
            ImageLoader imageLoader = new ImageLoader();

            // Path to images
            String path = "/samples/sample."; // Removed leading slash
            String[] extensions = {"bmp", "gif", "hdr", "jpeg", "jpg", "png", "tiff", "webp"};

            System.out.println("Loading images...");
            for (String extension : extensions) {

                // Debug
                System.out.println("Loading image: " + path + extension);

                // Load image
                BufferedImage image = imageLoader.load(path + extension);

                // Show image
                showImage(image, extension);
            }

            System.out.println("Loaded all images.");
            System.out.println("Loading images from absolute path...");

            String absolute = "C:/Users/MCmoderSD/IdeaProjects/Packages/ImageLoader/src/test/resources"; // Removed leading slash
            for (String extension : extensions) {

                // Debug
                System.out.println("Loading image: " + absolute + path + extension);

                // Load image
                BufferedImage image = imageLoader.load(absolute + path + extension, true);

                // Show image
                showImage(image, extension);
            }

            System.out.println("Loaded all images from absolute path.");
            System.out.println("Loading images from URL...");

            String url = "https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample."; // Removed leading slash
            for (String extension : extensions) {

                // Debug
                System.out.println("Loading image: " + url + extension);

                // Load image
                BufferedImage image = imageLoader.load(url + extension, false);

                // Show image
                showImage(image, extension);
            }
            System.out.println("Loaded all images from URL.");
        } catch (IOException | URISyntaxException e) {
            System.err.println("An error occurred while loading images: " + e.getMessage());
        }
    }

    // Show image
    public static void showImage(BufferedImage image, String extension) {

        // Create frame
        JFrame frame = new JFrame("Image: " + extension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(1200, 900);
        frame.setResizable(false);
        frame.setIconImage(image);

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        // Add panel to frame
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }
}