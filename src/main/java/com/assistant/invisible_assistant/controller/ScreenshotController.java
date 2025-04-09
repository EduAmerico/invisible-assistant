package com.assistant.invisible_assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.assistant.invisible_assistant.service.ScreenshotService;

import java.io.File;

@RestController
public class ScreenshotController {

    private final ScreenshotService screenshotService;

    public ScreenshotController(ScreenshotService screenshotService) {
        this.screenshotService = screenshotService;
    }

    @GetMapping("/screenshot")
    public String takeScreenshot() {
        try {
            File file = screenshotService.captureAndSaveScreenshot();
            return file != null ? "Saved: " + file.getAbsolutePath() : "Failed to take screenshot";
        } catch (Exception e) {
            e.printStackTrace(); // pode substituir por log futuramente
            return "Error taking screenshot: " + e.getMessage();
        }
    }
}
