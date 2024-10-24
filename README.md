# ImageLoader
This is a simple ImageLoader that can load images from the resources folder, absolute paths or URL's.

Supported image formats:

- [x] bmp
- [x] gif 
- [x] hdr 
- [x] jpeg 
- [x] jpg 
- [x] png 
- [x] tiff 
- [x] webp

## Usage

### Maven
```xml
<dependencies>
    <dependency>
        <groupId>de.MCmoderSD</groupId>
        <artifactId>imageloader</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

## Usage Example

```java
import de.MCmoderSD.imageloader.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

@SuppressWarnings("ALL")
public class Main {

    // Main method
    public static void main(String[] args) {

        // Initialize ImageLoader
        ImageLoader imageLoader = new ImageLoader();
        BufferedImage image;

        // Load image from resources
        try {
            image = imageLoader.load("/samples/sample.png");
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from resources: " + e.getMessage());
        }

        // Load image from absolute path
        try {
            image = imageLoader.load("PATH_TO_YOUR_FILE.ext", true);
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from absolute path: " + e.getMessage());
        }

        // Load image from URL
        try {
            image = imageLoader.load("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/samples/sample.webp");
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error loading image from URL: " + e.getMessage());
        }
    }
}
```