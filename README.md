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
    <version>1.1.0</version>
</dependency>
```


## Usage Example

### ImageLoader
```java
import de.MCmoderSD.imageloader.AnimationLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    // Main method
    public static void main(String[] args) {

        // Initialize AnimationLoader
        AnimationLoader animationLoader = new AnimationLoader();
        BufferedImage image;

        // Load image from resources
        try {
            image = animationLoader.load("/samples/sample.png");
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from resources: " + e.getMessage());
        }

        // Load image from absolute path
        try {
            image = animationLoader.load("PATH_TO_YOUR_FILE.ext", true);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from absolute path: " + e.getMessage());
        }

        // Load image from URL
        try {
            image = animationLoader.load("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample.webp");
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from URL: " + e.getMessage());
        }
    }
}
```

### AnimationLoader
```java
import de.MCmoderSD.imageloader.AnimationLoader;

import javax.swing.ImageIcon;
import java.io.IOException;
import java.net.URISyntaxException;

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
}
```