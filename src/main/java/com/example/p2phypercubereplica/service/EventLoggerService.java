package com.example.p2phypercubereplica.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventLoggerService {
    private List<String> eventLogs = new ArrayList<>();

    public void logEvent(String event) {
        eventLogs.add(event);
        System.out.println("Event Logged: " + event);
    }

    public List<String> getEventLogs() {
        return eventLogs;
    }
}
