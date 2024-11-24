Here is the cURL commands for each API, followed by the flow to test all functionalities, categorized by functionality and the order in which to call them:

cURL Commands for Each API
IndexingServer APIs
Register a new peer

bash
Copy code
curl -X POST "http://localhost:8080/index/RegisteringANewPeer" -d "peerId=<peerId>"
Unregister a peer node

bash
Copy code
curl -X POST "http://localhost:8080/index/UnregisterAPeerNode" -d "peerId=<peerId>"
Update a topic on a node

bash
Copy code
curl -X POST "http://localhost:8080/index/UpdateTopicOnANode" -d "topicName=<topicName>" -d "peerId=<peerId>"
Query available topics

bash
Copy code
curl -X GET "http://localhost:8080/index/QueryTopics"
PeerNodeController APIs
Publish a message to a topic

bash
Copy code
curl -X POST "http://localhost:8080/peer/PublishMessageToTopic" -d "topicName=<topicName>" -d "message=<message>"
Initialize a peer node

bash
Copy code
curl -X POST "http://localhost:8080/peer/InitializePeerNode" -d "peerId=<peerId>"
Create a topic

bash
Copy code
curl -X POST "http://localhost:8080/peer/CreateTopic" -d "topicName=<topicName>"
Subscribe to a topic

bash
Copy code
curl -X GET "http://localhost:8080/peer/SubscribeTopic" -d "topicName=<topicName>"
Pull messages from a topic

bash
Copy code
curl -X GET "http://localhost:8080/peer/PullMessages" -d "topicName=<topicName>"
Register a peer with the indexing server

bash
Copy code
curl -X POST "http://localhost:8080/peer/RegisterPeerWithIndexingServer" -d "peerId=<peerId>"
Report metrics

bash
Copy code
curl -X POST "http://localhost:8080/peer/ReportMetrics"
Get metrics

bash
Copy code
curl -X GET "http://localhost:8080/peer/GetMetrics"
Get event logs

bash
Copy code
curl -X GET "http://localhost:8080/peer/EventLogGETAPI"
Create a replica for a node

bash
Copy code
curl -X POST "http://localhost:8080/peer/CreateReplica" -d "nodeName=<nodeName>" -d "replicaName=<replicaName>"
View replicas of a node

bash
Copy code
curl -X GET "http://localhost:8080/peer/ViewReplicas" -d "nodeName=<nodeName>"
Fail a node

bash
Copy code
curl -X POST "http://localhost:8080/peer/FailNode" -d "nodeName=<nodeName>"
Check the status of a node

bash
Copy code
curl -X GET "http://localhost:8080/peer/CheckNodeStatus" -d "nodeName=<nodeName>"
Testing Flow and Verification
Here’s the order to test and verify each required functionality:

1. Basic Functionality: Topic Management
   Goal: Verify topics can be created, published to, and messages can be retrieved.

Steps:

Initialize a peer node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/InitializePeerNode" -d "peerId=node1"
Create a topic on the node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/CreateTopic" -d "topicName=topic1"
Publish a message to the topic:
bash
Copy code
curl -X POST "http://localhost:8080/peer/PublishMessageToTopic" -d "topicName=topic1" -d "message=HelloWorld"
Pull the messages from the topic:
bash
Copy code
curl -X GET "http://localhost:8080/peer/PullMessages" -d "topicName=topic1"
2. Replicated Topics: Performance Optimization & Fault Tolerance
   Goal: Verify replicas can be created, viewed, and used for fault tolerance.

Steps:

Create a replica for a node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/CreateReplica" -d "nodeName=node1" -d "replicaName=node1-replica"
View the replicas of the node:
bash
Copy code
curl -X GET "http://localhost:8080/peer/ViewReplicas" -d "nodeName=node1"
Fail the original node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/FailNode" -d "nodeName=node1"
Check the status of the failed node:
bash
Copy code
curl -X GET "http://localhost:8080/peer/CheckNodeStatus" -d "nodeName=node1"
Publish to the replica and retrieve messages:
Publish:
bash
Copy code
curl -X POST "http://localhost:8080/peer/PublishMessageToTopic" -d "topicName=topic1" -d "message=ReplicaMessage"
Pull from replica:
bash
Copy code
curl -X GET "http://localhost:8080/peer/PullMessages" -d "topicName=topic1"
3. Dynamic Topology Configuration
   Goal: Verify topics can move to the correct nodes dynamically.

Steps:

Fail the node where the topic is stored:
bash
Copy code
curl -X POST "http://localhost:8080/peer/FailNode" -d "nodeName=node1"
Create the topic elsewhere if it does not exist:
bash
Copy code
curl -X POST "http://localhost:8080/peer/CreateTopic" -d "topicName=topic1"
Bring back the original node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/InitializePeerNode" -d "peerId=node1"
Verify the original node retrieves topics it missed:
bash
Copy code
curl -X GET "http://localhost:8080/peer/PullMessages" -d "topicName=topic1"
4. Failure Detection
   Goal: Ensure failed nodes are detected, and replicas are used.

Steps:

Fail a node:
bash
Copy code
curl -X POST "http://localhost:8080/peer/FailNode" -d "nodeName=node2"
Check the node’s status:
bash
Copy code
curl -X GET "http://localhost:8080/peer/CheckNodeStatus" -d "nodeName=node2"
View available replicas for the failed node:
bash
Copy code
curl -X GET "http://localhost:8080/peer/ViewReplicas" -d "nodeName=node2"
This testing flow ensures all functionalities (replicated topics, dynamic topology, and failure handling) are verified step-by-step. Let me know if you need further clarification!

java -jar p2phypercubereplica-1.0-SNAPSHOT.jar --server.port=8080





