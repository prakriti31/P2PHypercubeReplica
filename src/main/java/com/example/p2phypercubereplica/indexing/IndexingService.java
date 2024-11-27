package com.example.p2phypercubereplica.indexing;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexingService {

    // Store topics and the corresponding node/port info
    private Map<String, String> topicToNodeMap = new HashMap<>();

    public String registerPeer(String peerInfo) {
        // Register the peer (in this example, peerInfo will include the peer's address and port)
        // For simplicity, let's assume peerInfo contains "nodeId:port"
        String[] parts = peerInfo.split(":");
        String nodeId = parts[0];
        String port = parts[1];

        // Update the topic map for the node
        topicToNodeMap.put("defaultTopic", nodeId + ":" + port); // Replace with actual logic for topic mapping

        return "Peer registered: " + nodeId + " on port " + port;
    }

    public String unregisterPeer(String peerId) {
        // Unregister the peer (just an example, needs actual logic)
        topicToNodeMap.entrySet().removeIf(entry -> entry.getValue().startsWith(peerId));
        return "Peer unregistered: " + peerId;
    }

    public String updateTopic(String topicUpdateInfo) {
        // For simplicity, let's assume topicUpdateInfo is in the format "topicName:nodeId:port"
        String[] parts = topicUpdateInfo.split(":");
        String topicName = parts[0];
        String nodeId = parts[1];
        String port = parts[2];

        // Update the topic mapping
        topicToNodeMap.put(topicName, nodeId + ":" + port);
        return "Topic updated: " + topicName + " on node " + nodeId + " with port " + port;
    }

}
