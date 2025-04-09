package com.assistant.invisible_assistant.service;

import com.assistant.invisible_assistant.model.Screenshot;
import com.assistant.invisible_assistant.model.Session;
import com.assistant.invisible_assistant.repository.ScreenshotRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

@Service
public class ScreenshotService {

    private final ScreenshotRepository screenshotRepository;
    private final SessionService sessionService;

    public ScreenshotService(ScreenshotRepository screenshotRepository, SessionService sessionService) {
        this.screenshotRepository = screenshotRepository;
        this.sessionService = sessionService;
    }

    public File captureAndSaveScreenshot() throws AWTException, IOException {

        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = robot.createScreenCapture(screenRect);


        String filename = "Screenshot_" + LocalDateTime.now().toString().replaceAll("[:.]", "") + ".png";
        File dir = new File("screenshots");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, filename);
        ImageIO.write(screenshot, "png", file);


        Session session = sessionService.getCurrentSession();

        Screenshot shot = new Screenshot();
        shot.setFilePath(file.getAbsolutePath());
        shot.setCapturedAt(LocalDateTime.now());
        shot.setSession(session);

        screenshotRepository.save(shot);

        return file;
    }
}
