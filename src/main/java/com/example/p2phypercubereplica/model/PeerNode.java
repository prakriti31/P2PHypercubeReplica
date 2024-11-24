package com.example.p2phypercubereplica.model;

public class PeerNode {
    private String id;
    private boolean isActive;

    public PeerNode(String id) {
        this.id = id;
        this.isActive = true;
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
        return "PeerNode{id='" + id + "', isActive=" + isActive + '}';
    }
}
