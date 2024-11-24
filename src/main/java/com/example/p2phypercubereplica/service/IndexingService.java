package com.example.p2phypercubereplica.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IndexingService {

    private final ConcurrentHashMap<String, String> peerTopics = new ConcurrentHashMap<>();
    private final List<String> registeredPeers = new ArrayList<>();

    public String registerPeer(String peerId) {
        registeredPeers.add(peerId);
        return "Peer '" + peerId + "' registered successfully.";
    }

    public String unregisterPeer(String peerId) {
        registeredPeers.remove(peerId);
        return "Peer '" + peerId + "' unregistered successfully.";
    }

    public String updateTopicOnNode(String topicName, String peerId) {
        peerTopics.put(topicName, peerId);
        return "Topic '" + topicName + "' updated on peer '" + peerId + "'.";
    }

    public List<String> getAvailableTopics() {
        return new ArrayList<>(peerTopics.keySet());
    }
}
