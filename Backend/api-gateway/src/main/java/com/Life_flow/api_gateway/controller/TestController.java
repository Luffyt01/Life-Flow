package com.Life_flow.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/index.html")
    public String hello(){
        return "WELCOME TO LIFE-FLOW";
    }
}