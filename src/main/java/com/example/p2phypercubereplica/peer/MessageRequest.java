package com.example.p2phypercubereplica.peer;

public class MessageRequest {
    private String message;
    private String topicName;

    public MessageRequest(String message, String topicName) {
        this.message = message;
        this.topicName = topicName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
