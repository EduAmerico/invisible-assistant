package com.assistant.invisible_assistant.controller;

import com.assistant.invisible_assistant.model.Session;
import com.assistant.invisible_assistant.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionViewController {
    
    private final SessionService sessionService;

    public SessionViewController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/view")
public String viewSession(Model model) {
    Session refreshed = sessionService.getCurrentSessionWithScreenshots();
    System.out.println("Screenshots count in controller: " + refreshed.getScreenshots().size());
    model.addAttribute("session", refreshed);
    return "home";
}
}