import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenCapture1 extends JFrame {
    private Robot robot;
    private String filePath = "C:\\Users\\cheng\\Documents\\icon.png"; // Replace with your desired file path
    private int fileCounter = 1;
    private TrayIcon trayIcon;
    private SystemTray systemTray;
    private boolean isMinimized = false;
    private JMenuItem openMenuItem;
    private JMenuItem exitMenuItem;

    public ScreenCapture1() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setVisible(false);
        setLocationRelativeTo(null);
        setResizable(false);
        requestFocus();
        JLabel instruction = new JLabel("Press F12 key to take a screenshot");
        instruction.setHorizontalAlignment(JLabel.CENTER); // Center align the label text
        add(instruction, BorderLayout.CENTER); // Add the instruction label to the center of the frame

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F12) {
                    takeScreenshot();
                    minimizeToTray();
                }
            }
        });

        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png"); // Replace with your icon image path
            PopupMenu popupMenu = new PopupMenu();
            MenuItem openMenuItem = new MenuItem("Open");
            MenuItem exitMenuItem = new MenuItem("Exit");

            openMenuItem.addActionListener(e -> {
                restoreFromTray();
            });

            exitMenuItem.addActionListener(e -> {
                exitApplication();
            });

            popupMenu.add(openMenuItem);
            popupMenu.addSeparator();
            popupMenu.add(exitMenuItem);

            trayIcon = new TrayIcon(image, "ScreenCapture", popupMenu);
            trayIcon.setImageAutoSize(true);

            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private void takeScreenshot() {
        try {
            // Capture the screen
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

            // Save the screenshot to the specified file path
            File file = new File(filePath);

            while (file.exists()) {
                // Modify the file name
                filePath = "C:\\Users\\cheng\\Documents\\icon" + fileCounter + ".png";
                file = new File(filePath);
                fileCounter++;
            }
            ImageIO.write(screenFullImage, "png", file);

            System.out.println("Screenshot captured and saved to: " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void minimizeToTray() {
        setVisible(false);
        isMinimized = true;
    }

    private void restoreFromTray() {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
        isMinimized = false;
        toFront();
    }

    private void exitApplication() {
        systemTray.remove(trayIcon);
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScreenCapture1::new);
    }
}