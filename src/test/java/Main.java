import de.MCmoderSD.imageloader.core.ImageLoader;
import de.MCmoderSD.imageloader.enums.Extension;
import de.MCmoderSD.imageloader.tools.ImageEncoder;
import de.MCmoderSD.imageloader.tools.ImageResizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

void main() {

    // Initialize ImageLoader
    var imageLoader = ImageLoader.getInstance();

    // Load, encode, and show image
    var originalImage = imageLoader.loadResource("/samples/sample.png");            // Load from resources
    var base64Image = ImageEncoder.toBase64(originalImage, Extension.JPG, 0.25f);   // Convert to Base64 with compression
    var compressedImage = imageLoader.loadBase64(base64Image);                      // Load from Base64 string
    var resizedImage = ImageResizer.scale(compressedImage, 0.5f);                   // Resize image to 50% of original size
    showImage(resizedImage);                                                        // Show resized image
}

// Show image
void showImage(BufferedImage image) {

    // Create frame
    var frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(image.getWidth(), image.getHeight());
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