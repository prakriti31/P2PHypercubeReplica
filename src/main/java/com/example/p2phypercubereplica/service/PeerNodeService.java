package com.example.p2phypercubereplica.service;

import com.example.p2phypercubereplica.model.Topic;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PeerNodeService {

    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final Map<String, List<String>> replicas = new ConcurrentHashMap<>();
    private final Map<String, Boolean> nodeStatus = new ConcurrentHashMap<>();

    public List<String> pullMessages(String topicName) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Topic '" + topicName + "' does not exist.");
        }

        synchronized (topic) {
            List<String> unreadMessages = new ArrayList<>(topic.getMessages());
            topic.getMessages().clear();
            return unreadMessages;
        }
    }

    public String createTopic(String topicName) {
        if (topics.containsKey(topicName)) {
            return "Topic '" + topicName + "' already exists.";
        }

        topics.put(topicName, new Topic(topicName));
        return "Topic '" + topicName + "' created successfully.";
    }

    public String publishMessage(String topicName, String message) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Topic '" + topicName + "' does not exist.");
        }

        topic.addMessage(message);
        return "Message published to topic '" + topicName + "'.";
    }

    public void initializePeer(String peerId) {
        nodeStatus.put(peerId, true);
    }

    public String subscribeToTopic(String topicName) {
        if (!topics.containsKey(topicName)) {
            return "Topic '" + topicName + "' does not exist.";
        }
        return "Subscribed to topic '" + topicName + "'.";
    }

    public String registerWithIndexing(String peerId) {
        return "Peer '" + peerId + "' registered successfully.";
    }

    public String reportMetrics() {
        return "Metrics reported successfully.";
    }

    public String getMetrics() {
        return "Metrics retrieved successfully.";
    }

    // NEW FUNCTIONALITY: Create a replica of a node
    public String createReplica(String nodeName, String replicaName) {
        replicas.computeIfAbsent(nodeName, k -> new ArrayList<>()).add(replicaName);
        return "Replica '" + replicaName + "' created for node '" + nodeName + "'.";
    }

    // NEW FUNCTIONALITY: View replicas of a node
    public List<String> viewReplicas(String nodeName) {
        return replicas.getOrDefault(nodeName, Collections.emptyList());
    }

    // NEW FUNCTIONALITY: Fail a node
    public String failNode(String nodeName) {
        nodeStatus.put(nodeName, false);
        return "Node '" + nodeName + "' marked as failed.";
    }

    // NEW FUNCTIONALITY: Check the status of a node
    public String checkNodeStatus(String nodeName) {
        return nodeStatus.getOrDefault(nodeName, false) ? "Active" : "Failed";
    }
}
