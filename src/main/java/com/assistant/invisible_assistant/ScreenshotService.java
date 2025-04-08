package com.assistant.invisible_assistant;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ScreenshotService {

    public File captureScreen() {
        try {

            File diretory = new File("screenshots");
            if (!diretory.exists()) {
                diretory.mkdir();
            }

            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            String filename = "Screenshot_" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss")) + ".png";

            File file = new File(diretory, filename);
            ImageIO.write(screenshot, "png", file);

            System.out.println("Screenshot saved: " + file.getAbsolutePath());
            return file;
            
        } catch (AWTException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
