package com.example.p2phypercubereplica.peer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventLoggerService {
    private List<String> eventLogs = new ArrayList<>();

    public void logEvent(String event) {
        eventLogs.add(event + " at " + System.currentTimeMillis());
    }

    public List<String> getEventLogs() {
        return eventLogs;
    }

    public String getFormattedEventLogs() {
        return String.join("\n", eventLogs);
    }
}
