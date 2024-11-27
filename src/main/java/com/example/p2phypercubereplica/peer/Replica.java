package com.example.p2phypercubereplica.peer;

public class Replica {
    private String topicName;
    private String replicaId;
    private PeerNode replicaLocation;

    public Replica(String topicName, PeerNode replicaLocation) {
        this.topicName = topicName;
        this.replicaLocation = replicaLocation;
        this.replicaId = generateReplicaId();
    }

    private String generateReplicaId() {
        return topicName + "_" + replicaLocation.getId() + "_replica";
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public PeerNode getReplicaLocation() {
        return replicaLocation;
    }

    public void setReplicaLocation(PeerNode replicaLocation) {
        this.replicaLocation = replicaLocation;
    }

    public String getReplicaId() {
        return replicaId;
    }

    @Override
    public String toString() {
        return "Replica{topicName='" + topicName + "', replicaId='" + replicaId + "', replicaLocation=" + replicaLocation + "}";
    }
}
