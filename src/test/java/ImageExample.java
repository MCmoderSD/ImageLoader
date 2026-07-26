import de.MCmoderSD.imageloader.core.ImageLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import static java.lang.IO.println;

void main() {

    // Initialize ImageLoader
    var imageLoader = ImageLoader.getInstance();

    // Path to images
    var resourcePath = "/samples/sample.";
    String[] extensions = { "jpeg", "jpg", "png", "bmp", "tiff", "gif", "webp" };

    println("Loading images from resources...");
    for (var extension : extensions) {

        // Debug
        println("Loading image: " + resourcePath + extension);

        // Load image
        var image = imageLoader.loadResource(resourcePath + extension);

        // Show image
        showImage(image, extension);
    }

    println("Loaded all images.");
    println("\nLoading images from path...");

    var path = "src/test/resources" + resourcePath;
    for (var extension : extensions) {

        // Debug
        println("Loading image: " + path + extension);

        // Load image
        var image = imageLoader.loadFile(path + extension);

        // Show image
        showImage(image, extension);
    }

    println("Loaded all images.");
    println("\nLoading images from URL...");

    var url = "https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample.";
    for (var extension : extensions) {

        // Debug
        println("Loading image: " + url + extension);

        // Load image
        var image = imageLoader.loadURL(url + extension);

        // Show image
        showImage(image, extension);
    }

    println("Loaded all images from URL.");
}

// Show image
void showImage(BufferedImage image, String extension) {

    // Create frame
    var frame = new JFrame("Image: " + extension);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(1200, 900);
    frame.setResizable(false);
    frame.setIconImage(image);

    // Create panel
    var panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    };

    // Add panel to frame
    frame.add(panel);

    // Center frame
    var dim = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

    // Show frame
    frame.setVisible(true);
}