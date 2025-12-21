package com.project.inventory_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class ExpiryManagementController {

    @GetMapping("/expiry-alert")
    public ResponseEntity<List<>> getExpiryAlert(){
        return ResponseEntity.ok();
    }
}
