package com.project.Live_Flow.request_service.services;

import java.util.Map;

public interface ReminderService {
    void scheduleReminder(Map<String, Object> payload);
    void sendReminderNow(Map<String, Object> payload);
}
