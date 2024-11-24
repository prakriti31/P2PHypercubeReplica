package com.example.p2phypercubereplica.model;

public class Replica {
    private Topic topic;
    private PeerNode replicaLocation;

    public Replica(Topic topic, PeerNode replicaLocation) {
        this.topic = topic;
        this.replicaLocation = replicaLocation;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public PeerNode getReplicaLocation() {
        return replicaLocation;
    }

    public void setReplicaLocation(PeerNode replicaLocation) {
        this.replicaLocation = replicaLocation;
    }

    @Override
    public String toString() {
        return "Replica{topic=" + topic + ", replicaLocation=" + replicaLocation + '}';
    }
}
