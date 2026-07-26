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
    <version>1.4.2</version>
</dependency>
```


## Usage Example

### ImageLoader

```java
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
```

### AnimationLoader

```java
import de.MCmoderSD.imageloader.core.AnimationLoader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

void main() {

    // Initialize AnimationLoader
    var animationLoader = AnimationLoader.getInstance();

    // Load from resources
    var animation = animationLoader.loadResource("/animations/apple.gif");
    showAnimation(animation, "Resource");

    // Load from path
    animation = animationLoader.loadFile("src/test/resources/animations/apple.gif");
    showAnimation(animation, "Path");

    // Load from URL
    animation = animationLoader.loadURL("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/animations/apple.gif");
    showAnimation(animation, "URL");
}

// Show image
void showAnimation(ImageIcon animation, String title) {

    // Create frame
    var frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(140, 140);
    frame.setResizable(false);
    frame.setIconImage(animation.getImage());

    // Create panel
    var panel = new JPanel() {
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
    var dim = Toolkit.getDefaultToolkit().getScreenSize();
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
```

### Resizing and Encoding Images

```java
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
```