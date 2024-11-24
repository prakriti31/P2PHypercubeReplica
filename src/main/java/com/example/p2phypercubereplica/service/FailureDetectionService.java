package com.example.p2phypercubereplica.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FailureDetectionService {

    private final Map<String, Boolean> nodeStatus = new ConcurrentHashMap<>();

    public void markNodeActive(String nodeId) {
        nodeStatus.put(nodeId, true);
        System.out.println("Node '" + nodeId + "' marked as active.");
    }

    public void markNodeInactive(String nodeId) {
        nodeStatus.put(nodeId, false);
        System.out.println("Node '" + nodeId + "' marked as inactive.");
    }

    public boolean isNodeActive(String nodeId) {
        return nodeStatus.getOrDefault(nodeId, false);
    }
}