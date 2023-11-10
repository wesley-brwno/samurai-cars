package br.com.project.samuraicars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("/public")
    public String publicEndpoint() {
        return "<h1>Public</h1>";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "<h1>Private</h1>";
    }
}
