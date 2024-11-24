package com.example.p2phypercubereplica.controller;

import com.example.p2phypercubereplica.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexingServerController {

    @Autowired
    private IndexingService indexingService;

    @PostMapping("/RegisteringANewPeer")
    public String registerNewPeer(@RequestParam String peerId) {
        return indexingService.registerPeer(peerId);
    }

    @PostMapping("/UnregisterAPeerNode")
    public String unregisterPeerNode(@RequestParam String peerId) {
        return indexingService.unregisterPeer(peerId);
    }

    @PostMapping("/UpdateTopicOnANode")
    public String updateTopicOnNode(@RequestParam String topicName, @RequestParam String peerId) {
        return indexingService.updateTopicOnNode(topicName, peerId);
    }

    @GetMapping("/QueryTopics")
    public List<String> queryTopics() {
        return indexingService.getAvailableTopics();
    }
}
