package com.example.social_media_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomepageController {
    @GetMapping("/")
    public String home() {
        return "Te lutem puno" ;
    }
}
