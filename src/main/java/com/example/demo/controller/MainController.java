package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/mainPage")
    public String showMainPage(){
        return "mainPage";
    }
    @GetMapping("/startProducts")
    public String redirectToProducts(){
        return "redirect:/products";
    }
}
