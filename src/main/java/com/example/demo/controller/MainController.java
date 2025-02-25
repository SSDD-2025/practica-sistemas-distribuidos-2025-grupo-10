package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MainController {
    @GetMapping("/mainPage")
    public String showMainPage(){
        //  Llame algun metodo de inicializar usuario
        return "mainPage";
    }
}
