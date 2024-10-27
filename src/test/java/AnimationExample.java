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
    public static void showAnimation(ImageIcon animation, String extension) {

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