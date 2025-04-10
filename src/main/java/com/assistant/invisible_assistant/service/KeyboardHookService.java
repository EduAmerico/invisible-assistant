package com.assistant.invisible_assistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class KeyboardHookService implements NativeKeyListener {
    
    private final ScreenshotService screenshotService;
    private final RestTemplate restTemplate;
    
    private boolean winPressed = false;
    private boolean altPressed = false;
    
    @Autowired
    public KeyboardHookService(ScreenshotService screenshotService) {
        this.screenshotService = screenshotService;
        this.restTemplate = new RestTemplate();
    }
    
    @PostConstruct
    public void init() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            System.out.println("Keyboard hook registered successfully!");
        } catch (NativeHookException e) {
            System.err.println("Failed to register keyboard hook: " + e.getMessage());
        }
    }
    
    @PreDestroy
    public void cleanup() {
        try {
            GlobalScreen.unregisterNativeHook();
            GlobalScreen.removeNativeKeyListener(this);
        } catch (NativeHookException e) {
            System.err.println("Failed to unregister keyboard hook: " + e.getMessage());
        }
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_META) {
            winPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = true;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_S && winPressed && altPressed) {
            takeScreenshotViaLocalRequest();
        }
    }
    
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_META) {
            winPressed = false;
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            altPressed = false;
        }
    }
    
    private void takeScreenshotViaLocalRequest() {
        try {
            // Faz uma requisição local para o endpoint /screenshot
            String response = restTemplate.getForObject("http://localhost:8080/screenshot", String.class);
            System.out.println("Screenshot taken via shortcut: " + response);
        } catch (Exception e) {
            System.err.println("Error taking screenshot via shortcut: " + e.getMessage());
        }
    }
}