import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenCapture extends JFrame {
    private Robot robot;
    private String filePath = "C:\\Users\\cheng\\Documents\\icon.png"; // Replace with your desired file path
    private int fileCounter = 1;
    private JLabel instruction = new JLabel("Press F12 key to screenshot");

    public ScreenCapture() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        requestFocus();
        instruction.setHorizontalAlignment(JLabel.CENTER); // Center align the label text
        add(instruction, BorderLayout.CENTER); // Add the instruction label to the center of the frame

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F12) {
                    dispose();
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            captureAndClose();
                            ((Timer) e.getSource()).stop(); // Stop the timer after executing once
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });
    }

    private void captureAndClose() {
        try {
            // Capture the screen
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

            // Save the screenshot to the specified file path
            File file = new File(filePath);

            while (file.exists()) {
                // Modify the file name
                filePath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "icon" + fileCounter + ".png";
                file = new File(filePath);
                fileCounter++;
            }
            ImageIO.write(screenFullImage, "png", file);

            System.out.println("Screenshot captured and saved to: " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Close the GUI and exit the program
            setVisible(false);
            dispose(); // Release any resources held by the window
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScreenCapture::new);
    }
}