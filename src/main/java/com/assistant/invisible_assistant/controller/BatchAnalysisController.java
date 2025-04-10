package com.assistant.invisible_assistant.controller;

import java.io.File;
import java.util.List;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assistant.invisible_assistant.model.GPTResponse;
import com.assistant.invisible_assistant.model.Screenshot;
import com.assistant.invisible_assistant.model.Session;
import com.assistant.invisible_assistant.repository.GPTResponseRepository;
import com.assistant.invisible_assistant.repository.ScreenshotRepository;
import com.assistant.invisible_assistant.service.GPTClientService;
import com.assistant.invisible_assistant.service.SessionService;

@RestController
public class BatchAnalysisController {
    
    private final ScreenshotRepository screenshotRepository;
    private final SessionService sessionService;
    private final GPTClientService gptClientService;
    private final GPTResponseRepository gptResponseRepository;
    
    public BatchAnalysisController(
            ScreenshotRepository screenshotRepository,
            SessionService sessionService,
            GPTClientService gptClientService,
            GPTResponseRepository gptResponseRepository) {
        this.screenshotRepository = screenshotRepository;
        this.sessionService = sessionService;
        this.gptClientService = gptClientService;
        this.gptResponseRepository = gptResponseRepository;
    }
    
    @GetMapping("/analyze-batch")
    public ResponseEntity<String> analyzeUnprocessedScreenshots() {
        Session session = sessionService.getCurrentSessionWithScreenshots();
        List<Screenshot> unanalyzedScreenshots = screenshotRepository.findUnanalyzedBySession(session);
        
        if (unanalyzedScreenshots.isEmpty()) {
            return ResponseEntity.ok("Nenhuma nova screenshot para analisar.");
        }
        
        // Formatar as informações sobre as screenshots não analisadas
        StringBuilder analysisResult = new StringBuilder();
        analysisResult.append("Análise de " + unanalyzedScreenshots.size() + " screenshots:\n\n");
        
        // Para cada screenshot não analisada
        for (Screenshot screenshot : unanalyzedScreenshots) {
            File imageFile = new File("screenshots", screenshot.getFilePath());
            if (imageFile.exists()) {
                // Analisar a imagem
                String gptResponseJson = gptClientService.analyzeImage(imageFile);
                
                try {
                    // Extrair o conteúdo da resposta da API
                    JSONObject jsonResponse = new JSONObject(gptResponseJson);
                    String content = jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getJSONArray("content")
                            .getString(0);
                    
                    // Adicionar à análise completa
                    analysisResult.append("Screenshot ")
                                 .append(screenshot.getFilePath())
                                 .append(" (")
                                 .append(screenshot.getCapturedAt())
                                 .append("):\n")
                                 .append(content)
                                 .append("\n\n---\n\n");
                    
                    // Marcar a screenshot como analisada
                    screenshot.setAnalyzed(true);
                    screenshotRepository.save(screenshot);
                } catch (Exception e) {
                    analysisResult.append("Erro ao analisar ")
                                 .append(screenshot.getFilePath())
                                 .append(": ")
                                 .append(e.getMessage())
                                 .append("\n\n");
                }
            } else {
                analysisResult.append("Arquivo não encontrado: ")
                             .append(screenshot.getFilePath())
                             .append("\n\n");
            }
        }
        
        // Salvar o resultado completo como uma resposta GPT
        GPTResponse response = new GPTResponse();
        response.setContent(analysisResult.toString());
        response.setSession(session);
        
        // Se já existe uma resposta, substitui; caso contrário, cria uma nova
        if (session.getResponse() != null) {
            response.setId(session.getResponse().getId());
        }
        
        session.setResponse(response);
        gptResponseRepository.save(response);
        
        return ResponseEntity.ok("Análise concluída para " + unanalyzedScreenshots.size() + " screenshots.");
    }
}