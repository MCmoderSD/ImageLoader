import de.MCmoderSD.imageloader.core.ImageLoader;
import de.MCmoderSD.imageloader.enums.Extension;
import de.MCmoderSD.imageloader.tools.Encoder;
import de.MCmoderSD.imageloader.tools.Resizer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.IOException;

import java.net.URISyntaxException;

@SuppressWarnings("ALL")
public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Initialize ImageLoader
        ImageLoader imageLoader = ImageLoader.getInstance();

        BufferedImage originalImage = imageLoader.load("/samples/sample.png");          // Load from resources
        String base64Image = Encoder.toBase64(originalImage, Extension.JPG, 0.25f);     // Convert to Base64 with compression
        BufferedImage compressedImage = imageLoader.load(base64Image);                  // Load from Base64 string
        BufferedImage resizedImage = Resizer.scale(compressedImage, 0.5f);              // Resize image to 50% of original size
        showImage(resizedImage, "jpg");                                                 // Show resized image
    }

    // Show image
    private static void showImage(BufferedImage image, String extension) {

        // Create frame
        JFrame frame = new JFrame("Image: " + extension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(image.getWidth(), image.getHeight());
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