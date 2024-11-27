package com.example.p2phypercubereplica.peer;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<String> messages;

    public Topic(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return "Topic{name='" + name + "', messages=" + messages + "}";
    }
}
