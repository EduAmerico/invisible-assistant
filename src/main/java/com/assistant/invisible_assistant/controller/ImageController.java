package com.assistant.invisible_assistant.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/screenshots")
public class ImageController {

    private final Path screenshotDir = Paths.get("screenshots");

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path file = screenshotDir.resolve(filename).normalize();

        // Verifica se o arquivo existe e é seguro
        if (!Files.exists(file) || !file.toAbsolutePath().startsWith(screenshotDir.toAbsolutePath())) {
            return ResponseEntity.notFound().build();
        }

        // Carrega o arquivo como recurso
        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // ou usar Files.probeContentType(file)
                .body(resource);
    }
}
