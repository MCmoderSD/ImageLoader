# ImageLoader

## Description
This is a simple ImageLoader that can load images from the resources folder, absolute paths or URL's.
Base64 encoded images that follow the format `data:image/<extension>;base64,<data>` are also supported.

For Static Images or GIFs', you can use the `ImageLoader` class.
For Animated GIF's, you have to use the `AnimationLoader` class.

Supported image formats:

- [x] JPEG/JPG
- [x] PNG
- [x] BMP
- [x] TIFF
- [x] GIF (animated and static)
- [x] WEBP

## Usage

### Maven
Make sure you have my Sonatype Nexus OSS repository added to your `pom.xml` file:
```xml
<repositories>
    <repository>
        <id>Nexus</id>
        <name>Sonatype Nexus</name>
        <url>https://mcmodersd.de/nexus/repository/maven-releases/</url>
    </repository>
</repositories>
```
Add the dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>de.MCmoderSD</groupId>
    <artifactId>ImageLoader</artifactId>
    <version>1.3.0</version>
</dependency>
```


## Usage Example

### ImageLoader

```java
import de.MCmoderSD.imageloader.core.ImageLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.IOException;

import java.net.URISyntaxException;

public class ImageExample {

    public static void main(String[] args) {
        try {

            // Initialize ImageLoader
            ImageLoader imageLoader = ImageLoader.getInstance();

            // Path to images
            String path = "/samples/sample.";
            String[] extensions = {"jpeg", "jpg", "png", "bmp", "tiff", "gif", "webp"};

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

            String absolute = "C:/Users/MCmoderSD/IdeaProjects/Packages/ImageLoader/src/test/resources";
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

            String url = "https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample.";
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
}
```

### AnimationLoader

```java
import de.MCmoderSD.imageloader.core.AnimationLoader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import java.io.IOException;

import java.net.URISyntaxException;

@SuppressWarnings("ALL")
public class AnimationExample {

    public static void main(String[] args) {
        try {

            // Initialize AnimationLoader
            AnimationLoader animationLoader = AnimationLoader.getInstance();

            // Load from resources
            ImageIcon animation = animationLoader.load("/animations/apple.gif");
            showAnimation(animation, "gif");

            // Load from absolute path
            animation = animationLoader.load("C:/Users/MCmoderSD/IdeaProjects/Packages/ImageLoader/src/test/resources/animations/apple.gif", true);
            showAnimation(animation, "gif");

            // Load from URL
            animation = animationLoader.load("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/animations/apple.gif");
            showAnimation(animation, "gif");

        } catch (IOException | URISyntaxException e) {
            System.err.println("An error occurred while loading images: " + e.getMessage());
        }
    }

    // Show image
    private static void showAnimation(ImageIcon animation, String extension) {

        // Create frame
        JFrame frame = new JFrame("Image: " + extension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(140, 140);
        frame.setResizable(false);
        frame.setIconImage(animation.getImage());

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics;
                g.drawImage(animation.getImage(), 0, 0, null);
            }
        };

        panel.setPreferredSize(new Dimension(140, 140));

        // Add panel to frame
        frame.add(panel);

        // Center frame
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);

        new Thread(() -> {
            while (frame.isVisible()) {
                panel.repaint();
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000 / 60); // 60 FPS
                } catch (InterruptedException e) {
                    System.err.println("An error occurred while showing animation: " + e.getMessage());
                }
            }
        }).start();
    }
}
```

### Resizing and Encoding Images

```java
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
```