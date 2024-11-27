package com.example.p2phypercubereplica.indexing;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/indexing")
public class IndexingServerController {

    private final IndexingService indexingService;

    public IndexingServerController(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @PostMapping("/register")
    public String registerPeer(@RequestBody String peerInfo) {
        return indexingService.registerPeer(peerInfo);
    }

    @PostMapping("/unregister")
    public String unregisterPeer(@RequestBody String peerId) {
        return indexingService.unregisterPeer(peerId);
    }

    @PostMapping("/updateTopic")
    public String updateTopic(@RequestBody String topicUpdateInfo) {
        return indexingService.updateTopic(topicUpdateInfo);
    }
}
