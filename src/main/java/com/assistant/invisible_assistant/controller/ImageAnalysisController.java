package com.assistant.invisible_assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assistant.invisible_assistant.service.GPTClientService;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

@RestController
public class ImageAnalysisController {

    private final GPTClientService gptClientService;

    public ImageAnalysisController(GPTClientService gptClientService) {
        this.gptClientService = gptClientService;
    }

    @GetMapping("/analyze-latest")
    public String analyzeLatestScreenshot() {
        File screenshotsDir = new File("screenshots");

        File[] files = screenshotsDir.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null || files.length == 0) {
            return "Nenhuma imagem encontrada na pasta /screenshots.";
        }

        File latest = Arrays.stream(files)
                .max(Comparator.comparing(File::lastModified))
                .orElse(null);

        if (latest == null) {
            return "Erro ao encontrar a imagem mais recente.";
        }

        return gptClientService.analyzeImage(latest);
    }
}
