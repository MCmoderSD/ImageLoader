# ImageLoader

## Description
This is a simple ImageLoader that can load images from the resources folder, absolute paths or URL's.

For Static Images or GIFs', you can use the `ImageLoader` class.
For Animated GIF's, you have to use the `AnimationLoader` class.

Supported image formats:

- [x] bmp
- [x] gif (static and animated)
- [x] hdr 
- [x] jpeg 
- [x] jpg 
- [x] png 
- [x] tiff 
- [x] webp
- [x] base64 encoded images

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
    <version>1.2.0</version>
</dependency>
```


## Usage Example

### ImageLoader
```java
import de.MCmoderSD.imageloader.ImageLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.IOException;

import java.net.URISyntaxException;

@SuppressWarnings("ALL")
public class ImageExample {

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
import de.MCmoderSD.imageloader.AnimationLoader;

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
            AnimationLoader animationLoader = new AnimationLoader();

            // Load from resources
            ImageIcon animation = animationLoader.load("/animations/apple.gif");
            showAnimation(animation, "gif");

            // Load from absolute path
            animation = animationLoader.load("C:/Users/MCmoderSD/IdeaProjects/Packages/ImageLoader/src/test/resources/animations/apple.gif", true);
            showAnimation(animation, "gif");

            // Load from URL
            animation = animationLoader.load("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/animations/apple.gif");
            showAnimation(animation, "gif");

            // Static calls
            animation = AnimationLoader.loadAnimation("/animations/apple.gif", false);
            animation = AnimationLoader.loadAnimation("C:/Users/MCmoderSD/IdeaProjects/Packages/ImageLoader/src/test/resources/animations/apple.gif", true);
            animation = AnimationLoader.loadAnimation("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/animations/apple.gif", false);

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
                    Thread.sleep(1000 / 30);
                } catch (InterruptedException e) {
                    System.err.println("An error occurred while showing animation: " + e.getMessage());
                }
            }
        }).start();
    }
}
```