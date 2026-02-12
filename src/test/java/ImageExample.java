import de.MCmoderSD.imageloader.core.ImageLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

void main() {

    // Initialize ImageLoader
    ImageLoader imageLoader = ImageLoader.getInstance();

    // Path to images
    String resourcePath = "/samples/sample.";
    String[] extensions = { "jpeg", "jpg", "png", "bmp", "tiff", "gif", "webp" };

    IO.println("Loading images from resources...");
    for (String extension : extensions) {

        // Debug
        IO.println("Loading image: " + resourcePath + extension);

        // Load image
        BufferedImage image = imageLoader.loadResource(resourcePath + extension);

        // Show image
        showImage(image, extension);
    }

    IO.println("Loaded all images.");
    IO.println("\nLoading images from path...");

    String path = "src/test/resources" + resourcePath;
    for (String extension : extensions) {

        // Debug
        IO.println("Loading image: " + path + extension);

        // Load image
        BufferedImage image = imageLoader.loadFile(path + extension);

        // Show image
        showImage(image, extension);
    }

    IO.println("Loaded all images.");
    IO.println("\nLoading images from URL...");

    String url = "https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample.";
    for (String extension : extensions) {

        // Debug
        IO.println("Loading image: " + url + extension);

        // Load image
        BufferedImage image = imageLoader.loadURL(url + extension);

        // Show image
        showImage(image, extension);
    }
    IO.println("Loaded all images from URL.");
}

// Show image
private static void showImage(BufferedImage image, String extension) {

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