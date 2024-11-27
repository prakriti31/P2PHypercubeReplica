package com.example.p2phypercubereplica.peer;

public class PeerNode {
    private String id;
    private boolean isActive;

    public PeerNode(String id) {
        this.id = id;
        this.isActive = true; // Initially active
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "PeerNode{id='" + id + "', isActive=" + isActive + "}";
    }
}
