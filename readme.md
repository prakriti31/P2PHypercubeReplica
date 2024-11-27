# Replicated Topics and Dynamic Topology Configuration

## Overview

In this project, we build upon the concepts implemented in **PA3** to add new features aimed at enhancing the system's **performance optimization** and **fault tolerance**. The key additions include:

1. **Replicated Topics** for improved performance and fault tolerance.
2. **Dynamic Topology Configuration**, allowing nodes to be added and removed during runtime.

These improvements ensure that the system can handle **multiple requests simultaneously**, optimize **data access latency**, and recover from **node failures** efficiently.

## Features

### 1. **Replicated Topics**
Replicating topics allows us to achieve two main objectives:
- **Performance Optimization**: Data is replicated to nearby servers to reduce access latency.
- **Fault Tolerance**: Ensures that if a node fails, topics and their data can be accessed through replicas on other nodes.

#### Performance Optimization
- **Replica Placement**: To optimize performance, replicas are placed on the **nearest** servers to clients. This minimizes latency when accessing data.
- **Consistency Model**: We explore the impact of different consistency models (such as eventual consistency vs. strong consistency) and their trade-offs in replication. In scenarios where replication overhead introduces performance penalties, we compare this with the baseline (PA3) code to understand the system's performance under different latency conditions.

#### Fault Tolerance
- **Node Failures**: In this project, we assume that nodes can fail. The system should:
    1. **Detect failed nodes**: Other nodes in the system should detect the failure of a node and not forward topic access to it.
    2. **Forward topic access to replicas**: In case of node failure, topic access requests should be redirected to nodes that hold replicas of the topic stored on the failed node.

This ensures that topic availability is maintained even when nodes fail.

### 2. **Dynamic Topology Configuration**
- **Dynamic Node Management**: The system allows nodes to be **added** and **removed** during runtime. This feature provides flexibility in managing the system's topology without disrupting the ongoing operations.

- **Topic Creation and Access**: If the "right" node (as determined by a hash function) is unavailable when a new topic is created, the system will create the topic on another node. When the failed node returns, it will:
    1. Detect the previously created topics that belong to it.
    2. Fetch the topics from other nodes where replicas exist.

- **Handling Offline Nodes**: When a node goes offline, the system continues to operate by redirecting topic access requests to other nodes that have replicas. Once the node is back online, it will synchronize with the rest of the network to retrieve its topics.

### 3. **Simultaneous Handling of Multiple Requests**
- **Concurrency**: The system is designed to handle multiple requests simultaneously, ensuring that operations like topic creation, topic access, and message publishing are processed concurrently across different nodes. This can be achieved by using threads or other concurrency mechanisms.
- **Interaction with Multiple Topics**: A single peer node can host multiple topics at the same time while simultaneously interacting with other topics. This ensures that nodes can scale effectively and handle multiple operations without blocking each other.

## Key Considerations

### 1. **Latency and Replication Overhead**
- The replication process introduces some overhead due to synchronization between nodes. You need to evaluate how this affects the system's performance, especially under varying network latencies. The goal is to find the right balance between replication (for performance optimization) and the associated overhead.

### 2. **Consistency Models**
- The system must decide when and where to place replicas based on the chosen consistency model. Different models have trade-offs between **availability**, **consistency**, and **partition tolerance** (CAP theorem).
- Evaluating different consistency models is critical in understanding the system's trade-offs between performance and reliability.

### 3. **Node Failures**
- The system needs to detect failed nodes efficiently and ensure that the failure does not impact topic availability. This requires a robust failure detection and recovery mechanism, ensuring that nodes can continue to access replicated data when needed.

## Implementation Requirements

### 1. **Simultaneous Requests**
- The system must be able to handle simultaneous requests from multiple clients. This can be achieved by:
    - **Multi-threading**: Each request to create topics, push messages, or subscribe to topics should be processed in separate threads to prevent blocking.
    - **Concurrency Control**: Ensure that multiple threads can access and modify shared data structures (such as topic lists and replicas) without causing race conditions.

### 2. **Replicated Topic Management**
- Each peer node should maintain a list of replicas for each topic and handle synchronization of data between replicas. When topics are created or updated, their replicas should be properly managed to ensure that all nodes have up-to-date data.

### 3. **Node Failure and Recovery**
- Implement a fault tolerance mechanism that allows the system to detect when a node goes down and reroute topic access requests to other nodes that contain replicas of the affected topics.
- Once the failed node comes back online, it should be able to synchronize with the rest of the system to retrieve its lost topics and data.

## Conclusion

This project enhances the **fault tolerance** and **performance optimization** of the system by adding **replicated topics** and enabling **dynamic topology configurations**. By handling node failures gracefully and ensuring efficient data access through replicas, the system remains robust and performant even in the face of node failures or topology changes.

The successful implementation of these features will significantly improve the system’s scalability, reliability, and responsiveness in a real-world distributed environment.


### cURL Commands for Each API

Below are the cURL commands for all the provided APIs. Each cURL command is constructed based on the RESTful endpoints defined in the controllers.

#### 1. **Indexing Server APIs**

- **Register Peer**
    ```bash
    curl -X POST http://localhost:8080/indexing/register \
    -H "Content-Type: application/json" \
    -d '{"peerInfo": "peer-1"}'
    ```
  **Explanation**: Registers a new peer with the provided `peerInfo` (e.g., `"peer-1"`).

- **Unregister Peer**
    ```bash
    curl -X POST http://localhost:8080/indexing/unregister \
    -H "Content-Type: application/json" \
    -d '{"peerId": "peer-1"}'
    ```
  **Explanation**: Unregisters a peer with the provided `peerId` (e.g., `"peer-1"`).

- **Update Topic**
    ```bash
    curl -X POST http://localhost:8080/indexing/updateTopic \
    -H "Content-Type: application/json" \
    -d '{"topicUpdateInfo": "new-topic-info"}'
    ```
  **Explanation**: Updates a topic with new information (e.g., `"new-topic-info"`).

- **Query Topic**
    ```bash
    curl -X GET "http://localhost:8080/indexing/queryTopic?topicName=myTopic"
    ```
  **Explanation**: Queries the system for a topic's details by `topicName` (e.g., `"myTopic"`).

---

#### 2. **Peer Node APIs**

- **Create Topic**
    ```bash
    curl -X POST http://localhost:8080/peer/createTopic \
    -H "Content-Type: application/json" \
    -d '{"topicName": "topic1"}'
    ```
  **Explanation**: Creates a new topic with the specified name (e.g., `"topic1"`).

- **Push Message to Topic**
    ```bash
    curl -X POST http://localhost:8080/peer/pushMessage \
    -H "Content-Type: application/json" \
    -d '{"message": "Hello World"}'
    ```
  **Explanation**: Pushes a message (e.g., `"Hello World"`) to the topic.

- **Subscribe to Topic**
    ```bash
    curl -X POST http://localhost:8080/peer/subscribe \
    -H "Content-Type: application/json" \
    -d '{"topicName": "topic1"}'
    ```
  **Explanation**: Subscribes the peer to the specified topic (e.g., `"topic1"`).

- **Pull Messages from Topic**
    ```bash
    curl -X GET "http://localhost:8080/peer/pullMessages?topicName=topic1"
    ```
  **Explanation**: Pulls messages from the specified topic (e.g., `"topic1"`).

- **Get Event Logs**
    ```bash
    curl -X GET http://localhost:8080/peer/eventLogs
    ```
  **Explanation**: Retrieves the event logs from the peer.

- **Create Replica**
    ```bash
    curl -X POST http://localhost:8080/peer/createReplica \
    -H "Content-Type: application/json" \
    -d '{"topicName": "topic1"}'
    ```
  **Explanation**: Creates a replica for the specified topic (e.g., `"topic1"`).

- **View Replica**
    ```bash
    curl -X GET "http://localhost:8080/peer/viewReplica?replicaId=topic1_replica"
    ```
  **Explanation**: Views the details of a replica using the `replicaId` (e.g., `"topic1_replica"`).

- **Fail Node**
    ```bash
    curl -X POST "http://localhost:8080/peer/failNode?nodeId=node1"
    ```
  **Explanation**: Marks a node as failed with the specified `nodeId` (e.g., `"node1"`).

- **Check Node Status**
    ```bash
    curl -X GET "http://localhost:8080/peer/checkNodeStatus?nodeId=node1"
    ```
  **Explanation**: Checks the status of a node using the `nodeId` (e.g., `"node1"`).

---

### Flow to Hit the APIs and Verify All Flows

To verify the entire flow of the system, you can follow this order to simulate registering nodes, creating topics, handling failures, and managing replicas.

#### **Step-by-Step Flow**

1. **Step 1: Register Peers**
    - Use the `POST /indexing/register` to register one or more peers.
    - Example:
        ```bash
        curl -X POST http://localhost:8080/indexing/register -H "Content-Type: application/json" -d '{"peerInfo": "peer-1"}'
        curl -X POST http://localhost:8080/indexing/register -H "Content-Type: application/json" -d '{"peerInfo": "peer-2"}'
        ```

2. **Step 2: Create Topics**
    - Use `POST /peer/createTopic` to create topics on a peer.
    - Example:
        ```bash
        curl -X POST http://localhost:8080/peer/createTopic -H "Content-Type: application/json" -d '{"topicName": "topic1"}'
        ```

3. **Step 3: Push Messages to Topics**
    - Use `POST /peer/pushMessage` to add messages to a topic.
    - Example:
        ```bash
        curl -X POST http://localhost:8080/peer/pushMessage -H "Content-Type: application/json" -d '{"message": "First message for topic1"}'
        ```

4. **Step 4: Subscribe to Topics**
    - Use `POST /peer/subscribe` to subscribe a peer to a topic.
    - Example:
        ```bash
        curl -X POST http://localhost:8080/peer/subscribe -H "Content-Type: application/json" -d '{"topicName": "topic1"}'
        ```

5. **Step 5: Pull Messages**
    - Use `GET /peer/pullMessages` to retrieve the messages from the subscribed topic.
    - Example:
        ```bash
        curl -X GET "http://localhost:8080/peer/pullMessages?topicName=topic1"
        ```

6. **Step 6: Create Replica for Topic**
    - Use `POST /peer/createReplica` to create replicas of a topic.
    - Example:
        ```bash
        curl -X POST http://localhost:8080/peer/createReplica -H "Content-Type: application/json" -d '{"topicName": "topic1"}'
        ```

7. **Step 7: View Replica**
    - Use `GET /peer/viewReplica` to view the details of the created replica.
    - Example:
        ```bash
        curl -X GET "http://localhost:8080/peer/viewReplica?replicaId=topic1_replica"
        ```

8. **Step 8: Simulate Node Failure**
    - Use `POST /peer/failNode` to simulate a node failure.
    - Example:
        ```bash
        curl -X POST "http://localhost:8080/peer/failNode?nodeId=node1"
        ```

9. **Step 9: Check Node Status**
    - Use `GET /peer/checkNodeStatus` to check the status of a node (whether it's active or failed).
    - Example:
        ```bash
        curl -X GET "http://localhost:8080/peer/checkNodeStatus?nodeId=node1"
        ```

10. **Step 10: View Event Logs**
- Use `GET /peer/eventLogs` to retrieve the event logs and confirm the actions taken (e.g., node failure, topic creation, etc.).
- Example:
    ```bash
    curl -X GET http://localhost:8080/peer/eventLogs
    ```

---

### Additional Considerations

- **Error Handling**: Be sure to test edge cases, such as:
    - Trying to unregister a non-existing peer.
    - Failing to create a topic if the peer is down.
    - Verifying if the correct replicas are returned after node failures.

By following this flow, you can test the entire system from peer registration to handling failures and managing topic replication effectively.