package com.project.Live_Flow.request_service.controller;

import com.project.Live_Flow.request_service.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/schedule")
    public ResponseEntity<Void> scheduleReminder(@RequestBody Map<String, Object> payload) {
        reminderService.scheduleReminder(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-now")
    public ResponseEntity<Void> sendReminderNow(@RequestBody Map<String, Object> payload) {
        reminderService.sendReminderNow(payload);
        return ResponseEntity.ok().build();
    }
}
