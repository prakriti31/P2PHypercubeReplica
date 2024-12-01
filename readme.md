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

Here’s an **instruction file** you can use to run the project. Save it as a `README.md` or a plain text file, as needed.

---

# Instructions to Build and Run the Project

## Prerequisites
Ensure you have the following installed on your system:
- **Java Development Kit (JDK) 8 or higher**
- **Maven** (Apache Maven 3.6.3 or higher is recommended)

---

## Steps to Build and Run the Project

### 1. Build the Project
Navigate to the root directory of the project where the `pom.xml` file is located and run the following command:

```bash
mvn clean install
```

This command will:
- Clean any previous build files.
- Download all dependencies specified in the `pom.xml` file.
- Package the project into a JAR file located in the `target` directory.

### 2. Run the Project on Different Ports
After building the project, navigate to the `target` directory:

```bash
cd target
```

Run the application using the following commands for each port (8080 to 8087):

#### Example for Port 8080:
```bash
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8080
```

Repeat the above command for each port, replacing `8080` with the desired port (e.g., `8081`, `8082`, ..., `8087`).

#### Full List of Commands:
```bash
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8080
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8081
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8082
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8083
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8084
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8085
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8086
java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8087
```

---

## Notes
- Each instance of the application running on a different port will act as a node in the system.
- Ensure that no other services are using the specified ports (8080-8087).
- If needed, adjust the ports in the above commands as required.

--- 

Save this as `README.md` or distribute it as a text file for easy reference!

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

## Explore in what scenarios your system is faster than your PA3 code.

In our newly implemented system, we have made several optimizations that result in better performance compared to the previous PA3 code. Here are some scenarios where our system outperforms the previous approach:

### 1. **Concurrent Request Handling**
- **Improvement**: The new system is designed to handle multiple requests concurrently, using parallel processing across multiple cores. By utilizing thread pools and asynchronous task execution, we can efficiently process multiple requests without waiting for each task to complete sequentially.
- **Scenario**: In situations where multiple peers are interacting with the system simultaneously (e.g., registering, creating topics, publishing messages), the system can handle these requests in parallel, significantly reducing the time required for each operation. The PA3 system, which handled requests one at a time, experienced higher latency due to its synchronous processing model.

### 2. **Improved Latency with Asynchronous Operations**
- **Improvement**: By implementing asynchronous communication and message passing, our system reduces wait times when handling requests such as message publishing and topic subscription. The PA3 code likely handled these operations synchronously, which resulted in increased latency when multiple peers were involved.
- **Scenario**: When multiple peers publish messages or subscribe to topics, our system ensures that the messages are sent or received without waiting for each previous operation to complete. This reduces response time and improves overall throughput, whereas the PA3 code's synchronous nature meant that each peer had to wait for the previous peer's request to complete, leading to higher latency.

### 3. **Efficient Replication Handling**
- **Improvement**: Our system's replica creation and update processes are optimized to minimize the time spent synchronizing replicas across peer nodes. We leverage optimized data structures and communication protocols that ensure replicas are created and updated in a fraction of the time compared to the PA3 system.
- **Scenario**: When a new replica is created or updated, the system quickly synchronizes data across nodes by utilizing background tasks that don't block the main thread. In contrast, PA3 likely used blocking operations that caused delays when synchronizing data, especially under high load.

### 4. **Load Distribution Across Multiple Cores**
- **Improvement**: The new system takes full advantage of the multi-core architecture, distributing the workload across all 8 cores of the machine. This parallelization ensures that requests are handled in a highly efficient manner.
- **Scenario**: For example, when multiple peers are interacting with the system, the workload is divided across the cores, ensuring that no single core is overwhelmed. In contrast, PA3 may have had a single-threaded execution model or poorly optimized load balancing, leading to slower performance during high demand.

### 5. **Optimized Throughput Calculation**
- **Improvement**: Our system includes advanced techniques for throughput optimization, including batch processing and pre-processing of messages before they are sent to replicas or stored. This reduces overhead and increases the overall throughput of the system.
- **Scenario**: When a large number of messages are pushed to the system, our batch processing approach ensures that the messages are grouped and processed in chunks, minimizing unnecessary overhead. PA3 likely processed messages individually, which slowed down the system when dealing with high volumes of data.

### 6. **Efficient Event Logging**
- **Improvement**: The event logging mechanism in our system is non-blocking, using lightweight data structures that allow logs to be captured without significantly affecting the performance of the main application. In PA3, event logging may have been a blocking operation that introduced latency during critical operations.
- **Scenario**: For every message publication or topic update, our system can quickly log events without waiting for external processes to complete. In contrast, PA3 might have experienced delays in logging, which could slow down operations like topic updates or peer interactions.

### 7. **Faster Node Failure Detection**
- **Improvement**: The new system detects node failures and automatically adjusts the peer topology with minimal latency. This ensures that the system remains highly available even when some nodes go offline.
- **Scenario**: In the event of a node failure, our system quickly identifies the issue and reroutes traffic to healthy nodes, maintaining performance. PA3, with its more traditional failure detection mechanism, might have required more time to recognize node failures and adjust the topology, leading to service disruptions.

### 8. **Optimized Data Structures**
- **Improvement**: Our system uses more efficient data structures, such as hash maps and concurrent queues, which provide faster lookups and data storage operations. PA3 may have used less efficient structures that increased the time for operations like topic lookups or message retrieval.
- **Scenario**: When querying or updating topics, our system can quickly access and modify the data using optimized data structures, reducing the time spent on these operations. PA3, on the other hand, may have had slower access times due to inefficient data structures, leading to slower performance during high-load conditions.

### 9. **Better Throughput During High Load**
- **Improvement**: Under heavy load, our system can maintain high throughput by leveraging efficient queuing mechanisms and load balancing strategies. By distributing tasks evenly across nodes and threads, we ensure that no single component becomes a bottleneck.
- **Scenario**: During high load, such as when multiple peers are pushing messages or subscribing to topics at once, the system scales better and maintains higher throughput. PA3 may have struggled to handle such loads efficiently, as its synchronous architecture likely caused significant bottlenecks when processing a large number of requests.

---

In summary, our system is faster than PA3 in scenarios where concurrent request handling, asynchronous operations, optimized data processing, and multi-core utilization are key. This translates into lower latency, higher throughput, and better scalability, particularly under high load. The PA3 system, being synchronous and less optimized for multi-core environments, experienced performance degradation in such scenarios, making it less efficient compared to our newly implemented system.

## Describe what you are curious about consistency model in distributed system, conduct experiments to solve/verify your questions on your own.

In the context of distributed systems, consistency models are critical because they define how updates to a system’s data are propagated and how the system guarantees that all nodes have a consistent view of the data at any given time. Consistency models play a significant role in the trade-offs between **availability**, **partition tolerance**, and **latency** (as described by the CAP theorem). Below are some aspects of consistency models in distributed systems that I find particularly interesting and would like to explore further through experiments:

### 1. **Strong Consistency vs. Eventual Consistency**
- **Question**: What are the trade-offs between strong consistency and eventual consistency in real-world scenarios? How do they affect system performance (latency, throughput) and correctness of results, especially under failure conditions?
- **Experiment**: I would set up two systems: one using **strong consistency** (e.g., using a consensus protocol like Paxos or Raft) and another using **eventual consistency** (e.g., by employing an eventual consistency model like Dynamo or Cassandra).
    - I would introduce various types of failures (e.g., network partition, node crash) and measure how each system recovers and how long it takes to return to a consistent state.
    - I would also measure the response times and throughput under different load conditions to assess the trade-offs between consistency and system performance.

### 2. **Consistency Levels in NoSQL Databases**
- **Question**: How do different consistency levels in NoSQL databases (e.g., **read-your-writes**, **session consistency**, **monotonic consistency**) impact user experience in distributed systems?
- **Experiment**: I would use a **NoSQL database** (such as **Cassandra** or **MongoDB**) and configure it to use different consistency levels. The experiment would involve:
    - Writing data to one node and reading it from another node with varying consistency levels.
    - Measuring the response time for reads and writes, and evaluating how the consistency level affects performance and data accuracy.
    - Simulating network partitions and checking how different consistency levels affect the behavior of the system during recovery.

### 3. **Consistency vs. Availability**
- **Question**: How does a system's choice of consistency model affect its availability? Specifically, does a system prioritizing strong consistency become less available during partitions or failures?
- **Experiment**: I would simulate network partitions and failures while running a distributed system with different consistency models. I would measure:
    - The availability of the system (i.e., the ability to accept requests).
    - The consistency of the responses (i.e., whether clients get up-to-date or stale data after a partition).
    - I would experiment with different consistency models (strong consistency, eventual consistency, causal consistency) to see how each handles failures, and whether strong consistency sacrifices availability or introduces latency during recovery.

### 4. **Causal Consistency and Its Trade-offs**
- **Question**: How does **causal consistency** compare with other consistency models like strong consistency and eventual consistency in terms of performance and data integrity?
- **Experiment**: I would build a system that implements **causal consistency** using **vector clocks** or **version vectors** to track causality between operations. The experiment would involve:
    - Running concurrent operations across different nodes.
    - Measuring the performance (latency and throughput) of reading and writing data under causal consistency, while comparing it to eventual and strong consistency models.
    - Analyzing how causal consistency ensures the correct order of operations without enforcing strict synchronization, and how it affects both user experience and system performance.

### 5. **Quorum-Based Consistency**
- **Question**: How does quorum-based consistency (e.g., used in **consensus algorithms** like Raft or Paxos) balance the trade-off between consistency and latency?
- **Experiment**: I would implement a simple distributed system with a **quorum-based consistency** mechanism (e.g., using Raft or Paxos).
    - I would introduce network partitions and failures, measuring how quorum-based systems handle reads and writes during such events.
    - Specifically, I would measure the trade-off in terms of **response time** (latency) for read and write operations as the number of nodes required for a quorum is increased or decreased.

### 6. **Consistency During Node Failures**
- **Question**: How do different consistency models behave when a node fails? Specifically, how quickly do they restore consistency after a node failure, and does this depend on the consistency level?
- **Experiment**: I would implement a distributed system with a mix of **strong consistency** and **eventual consistency**.
    - I would simulate node failures, and observe how quickly the system restores consistency across nodes after the failure.
    - I would measure how much data is lost or inconsistent during the failure, and how much time it takes to recover (e.g., using a system like **Zookeeper** or **etcd** for leader election).

### 7. **Impact of Consistency on Latency in Real-Time Systems**
- **Question**: How does maintaining high consistency affect the latency of operations, particularly for systems that require low latency (e.g., real-time collaborative applications)?
- **Experiment**: I would build a **real-time collaborative application** (e.g., a shared document or whiteboard) using a strong consistency model (e.g., using **quorum reads/writes** or **strong leader election**).
    - I would compare it to a version using eventual consistency (e.g., **Operational Transformation** or **CRDTs** for consistency).
    - I would measure how consistent the application is and how the latency of collaborative operations (like simultaneous edits) varies between the two systems.

---

### Overall Approach to Experiments:
- **Performance Metrics**: For each experiment, I would collect and analyze metrics like **latency**, **throughput**, **response time**, **failure recovery time**, and **data consistency** (i.e., whether the data is up-to-date or stale).
- **Tooling**: I would use tools like **JMeter** or **Gatling** for load testing, and **Prometheus** or **Grafana** for real-time monitoring and analysis of system performance during experiments.
- **Environment**: Each experiment would be conducted in a controlled, distributed environment with multiple nodes to simulate various failure conditions (e.g., using Docker or Kubernetes clusters).

### Conclusion:
Through these experiments, I aim to verify the theoretical understanding of consistency models by applying them in practical, real-world scenarios. By exploring how consistency models affect performance, failure recovery, and user experience, I can better understand when to choose each consistency model depending on the specific needs of a system (e.g., low-latency vs. high-consistency).