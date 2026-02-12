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
    AnimationLoader animationLoader = AnimationLoader.getInstance();

    // Load from resources
    ImageIcon animation = animationLoader.loadResource("/animations/apple.gif");
    showAnimation(animation, "Resource");

    // Load from path
    animation = animationLoader.loadFile("src/test/resources/animations/apple.gif");
    showAnimation(animation, "Path");

    // Load from URL
    animation = animationLoader.loadURL("https://raw.githubusercontent.com/MCmoderSD/ImageLoader/refs/heads/master/src/test/resources/animations/apple.gif");
    showAnimation(animation, "URL");
}

// Show image
private static void showAnimation(ImageIcon animation, String title) {

    // Create frame
    JFrame frame = new JFrame(title);
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