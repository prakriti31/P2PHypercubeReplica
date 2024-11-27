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