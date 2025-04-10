package com.assistant.invisible_assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView analyzeLatestScreenshot(RedirectAttributes redirectAttributes) {
        File screenshotsDir = new File("screenshots");

        File[] files = screenshotsDir.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null || files.length == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nenhuma imagem encontrada na pasta /screenshots.");
            return new RedirectView("/view");
        }

        File latest = Arrays.stream(files)
                .max(Comparator.comparing(File::lastModified))
                .orElse(null);

        if (latest == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao encontrar a imagem mais recente.");
            return new RedirectView("/view");
        }

        gptClientService.analyzeImage(latest);
        return new RedirectView("/view");
    }
}