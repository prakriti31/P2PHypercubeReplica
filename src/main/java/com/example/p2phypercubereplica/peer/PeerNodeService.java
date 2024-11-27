package com.example.p2phypercubereplica.peer;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeerNodeService {

    private List<String> eventLogs = new ArrayList<>();
    private Map<String, List<String>> topics = new HashMap<>();
    private Map<String, String> nodeStatus = new HashMap<>();

    public String createTopic(String topicName) {
        topics.put(topicName, new ArrayList<>());
        logEvent("Topic created: " + topicName);
        return "Topic created: " + topicName;
    }

    public String pushMessage(String message, String topicName) {
        // Check if topic exists, if not create it
        if (!topics.containsKey(topicName)) {
            topics.put(topicName, new ArrayList<>());
        }
        topics.get(topicName).add(message);
        logEvent("Message pushed to " + topicName + ": " + message);
        return "Message pushed to " + topicName;
    }

    public String subscribeToTopic(String topicName) {
        // Implement subscription logic (for now it's just logging)
        logEvent("Subscribed to topic: " + topicName);
        return "Subscribed to topic: " + topicName;
    }

    public String pullMessages(String topicName) {
        // Pull messages logic
        return "Messages from topic " + topicName + ": " + topics.get(topicName).toString();
    }

    public String getEventLogs() {
        return String.join("\n", eventLogs);
    }

    public String createReplica(String topicName) {
        // Create a replica of the topic
        logEvent("Replica created for topic: " + topicName);
        return "Replica created for topic: " + topicName;
    }

    public String viewReplica(String replicaId) {
        // View a specific replica
        return "Viewing replica: " + replicaId;
    }

    public String failNode(String nodeId) {
        nodeStatus.put(nodeId, "failed");
        logEvent("Node failed: " + nodeId);
        return "Node failed: " + nodeId;
    }

    public String checkNodeStatus(String nodeId) {
        return nodeStatus.getOrDefault(nodeId, "Node status unknown");
    }

    private void logEvent(String event) {
        eventLogs.add(event + " at " + System.currentTimeMillis());
    }
}
