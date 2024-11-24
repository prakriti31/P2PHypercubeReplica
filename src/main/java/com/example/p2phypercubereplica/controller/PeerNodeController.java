package com.example.p2phypercubereplica.controller;

import com.example.p2phypercubereplica.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peer")
public class PeerNodeController {

    @Autowired
    private PeerNodeService peerNodeService;

    @Autowired
    private FailureDetectionService failureDetectionService;

    @Autowired
    private EventLoggerService eventLoggerService;

    @PostMapping("/PublishMessageToTopic")
    public ResponseEntity<String> publishMessageToTopic(@RequestParam String topicName, @RequestParam String message) {
        return ResponseEntity.ok(peerNodeService.publishMessage(topicName, message));
    }

    @PostMapping("/InitializePeerNode")
    public ResponseEntity<String> initializePeerNode(@RequestParam String peerId) {
        peerNodeService.initializePeer(peerId);
        return ResponseEntity.ok("Peer node initialized successfully.");
    }

    @PostMapping("/CreateTopic")
    public ResponseEntity<String> createTopic(@RequestParam String topicName) {
        return ResponseEntity.ok(peerNodeService.createTopic(topicName));
    }

    @GetMapping("/SubscribeTopic")
    public ResponseEntity<String> subscribeTopic(@RequestParam String topicName) {
        return ResponseEntity.ok(peerNodeService.subscribeToTopic(topicName));
    }

    @GetMapping("/PullMessages")
    public ResponseEntity<List<String>> pullMessages(@RequestParam String topicName) {
        return ResponseEntity.ok(peerNodeService.pullMessages(topicName));
    }

    @PostMapping("/RegisterPeerWithIndexingServer")
    public ResponseEntity<String> registerPeerWithIndexingServer(@RequestParam String peerId) {
        return ResponseEntity.ok(peerNodeService.registerWithIndexing(peerId));
    }

    @PostMapping("/ReportMetrics")
    public ResponseEntity<String> reportMetrics() {
        return ResponseEntity.ok(peerNodeService.reportMetrics());
    }

    @GetMapping("/GetMetrics")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok(peerNodeService.getMetrics());
    }

    @GetMapping("/EventLogGETAPI")
    public ResponseEntity<List<String>> getEventLogs() {
        return ResponseEntity.ok(eventLoggerService.getEventLogs());
    }

    // NEW FUNCTIONALITY: Create a replica of a node
    @PostMapping("/CreateReplica")
    public ResponseEntity<String> createReplica(@RequestParam String nodeName, @RequestParam String replicaName) {
        return ResponseEntity.ok(peerNodeService.createReplica(nodeName, replicaName));
    }

    // NEW FUNCTIONALITY: View replicas of a node
    @GetMapping("/ViewReplicas")
    public ResponseEntity<List<String>> viewReplicas(@RequestParam String nodeName) {
        return ResponseEntity.ok(peerNodeService.viewReplicas(nodeName));
    }

    // NEW FUNCTIONALITY: Fail a node
    @PostMapping("/FailNode")
    public ResponseEntity<String> failNode(@RequestParam String nodeName) {
        return ResponseEntity.ok(peerNodeService.failNode(nodeName));
    }

    // NEW FUNCTIONALITY: Check the status of a node
    @GetMapping("/CheckNodeStatus")
    public ResponseEntity<String> checkNodeStatus(@RequestParam String nodeName) {
        return ResponseEntity.ok(peerNodeService.checkNodeStatus(nodeName));
    }
}
