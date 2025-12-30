package com.project.Live_Flow.request_service.controller;

import com.project.Live_Flow.request_service.dto.DonorMatchResultDto;
import com.project.Live_Flow.request_service.services.DonorMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matching")
public class DonorMatchingController {

    @Autowired
    private DonorMatchingService donorMatchingService;

    @PostMapping("/find-donors")
    public ResponseEntity<List<DonorMatchResultDto>> findDonors(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(donorMatchingService.findDonors(payload));
    }

    @PostMapping("/notify-donors")
    public ResponseEntity<Void> notifyDonors(@RequestBody Map<String, Object> payload) {
        donorMatchingService.notifyDonors(payload);
        return ResponseEntity.ok().build();
    }
}
