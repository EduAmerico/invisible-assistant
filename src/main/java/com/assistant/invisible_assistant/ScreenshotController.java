package com.assistant.invisible_assistant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ScreenshotController {

    @Autowired
    private ScreenshotService screenshotService;

    @GetMapping("/screenshot")
    public String takeScreenshot() {
        File file = screenshotService.captureScreen();
        return file != null ? "Saved: " + file.getAbsolutePath(): "Failed to take screenshot";
    }
}
