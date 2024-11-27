package com.example.p2phypercubereplica.peer;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/peer")
public class PeerNodeController {

    private final PeerNodeService peerNodeService;

    public PeerNodeController(PeerNodeService peerNodeService) {
        this.peerNodeService = peerNodeService;
    }

    @PostMapping("/createTopic")
    public String createTopic(@RequestBody String topicName) {
        return peerNodeService.createTopic(topicName);
    }

    @PostMapping("/pushMessage")
    public String pushMessage(@RequestBody MessageRequest messageRequest) {
        return peerNodeService.pushMessage(messageRequest.getMessage(), messageRequest.getTopicName());
    }

    @PostMapping("/subscribe")
    public String subscribeToTopic(@RequestBody String topicName) {
        return peerNodeService.subscribeToTopic(topicName);
    }

    @GetMapping("/pullMessages")
    public String pullMessages(@RequestParam String topicName) {
        return peerNodeService.pullMessages(topicName);
    }

    @GetMapping("/eventLogs")
    public String getEventLogs() {
        return peerNodeService.getEventLogs();
    }

    @PostMapping("/createReplica")
    public String createReplica(@RequestBody String topicName) {
        return peerNodeService.createReplica(topicName);
    }

    @GetMapping("/viewReplica")
    public String viewReplica(@RequestParam String replicaId) {
        return peerNodeService.viewReplica(replicaId);
    }

    @PostMapping("/failNode")
    public String failNode(@RequestParam String nodeId) {
        return peerNodeService.failNode(nodeId);
    }

    @GetMapping("/checkNodeStatus")
    public String checkNodeStatus(@RequestParam String nodeId) {
        return peerNodeService.checkNodeStatus(nodeId);
    }
}
