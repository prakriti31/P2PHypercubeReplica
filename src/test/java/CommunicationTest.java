import java.util.concurrent.*;
import org.springframework.web.client.RestTemplate;
import com.example.p2phypercubereplica.peer.MessageRequest;

public class CommunicationTest {

    private static final String[] PEER_URLS = {
            "http://localhost:8080", "http://localhost:8081", "http://localhost:8082", "http://localhost:8083",
            "http://localhost:8084", "http://localhost:8085", "http://localhost:8086", "http://localhost:8087"
    };

    private static final String[] PEER_IDS = {
            "peer-1", "peer-2", "peer-3", "peer-4", "peer-5", "peer-6", "peer-7", "peer-8"
    };

    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        // Register peers
        for (int i = 0; i < PEER_IDS.length; i++) {
            final int peerIndex = i;
            executorService.submit(() -> registerPeer(peerIndex));
        }

        // Wait for a moment to ensure all peers are registered
        TimeUnit.SECONDS.sleep(2);

        // Create topics and simulate interactions
        for (int i = 0; i < PEER_IDS.length; i++) {
            final int peerIndex = i;
            executorService.submit(() -> {
                createTopic(peerIndex, "topic-" + peerIndex);
                subscribeToTopic(peerIndex, "topic-" + peerIndex);
                pushMessage(peerIndex, "topic-" + peerIndex, "Message from peer-" + peerIndex);
            });
        }

        // Wait for topic creation and subscriptions to complete
        TimeUnit.SECONDS.sleep(2);

        // Create replicas
        for (int i = 0; i < PEER_IDS.length; i++) {
            final int peerIndex = i;
            executorService.submit(() -> createReplica(peerIndex, "topic-" + peerIndex));
        }

        // Wait for replicas to be created
        TimeUnit.SECONDS.sleep(2);

        // Pull messages and view replicas
        for (int i = 0; i < PEER_IDS.length; i++) {
            final int peerIndex = i;
            executorService.submit(() -> {
                pullMessages(peerIndex, "topic-" + peerIndex);
                viewReplica(peerIndex, "topic-" + peerIndex + "_replica");
            });
        }

        // Fail nodes and check node statuses
        for (int i = 0; i < PEER_IDS.length; i++) {
            final int peerIndex = i;
            executorService.submit(() -> {
                failNode(peerIndex);
                checkNodeStatus(peerIndex);
            });
        }

        // Wait for all threads to complete
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    // API Call Methods
    private static void registerPeer(int peerIndex) {
        String url = PEER_URLS[peerIndex] + "/indexing/register";
        String response = restTemplate.postForObject(url, "{\"peerInfo\": \"" + PEER_IDS[peerIndex] + "\"}", String.class);
        System.out.println("Peer registered: " + PEER_IDS[peerIndex]);
    }

    private static void createTopic(int peerIndex, String topicName) {
        String url = PEER_URLS[peerIndex] + "/peer/createTopic";
        restTemplate.postForObject(url, "{\"topicName\": \"" + topicName + "\"}", String.class);
        System.out.println("Topic created under node " + PEER_IDS[peerIndex]);
    }

    private static void subscribeToTopic(int peerIndex, String topicName) {
        String url = PEER_URLS[peerIndex] + "/peer/subscribe";
        restTemplate.postForObject(url, "{\"topicName\": \"" + topicName + "\"}", String.class);
        System.out.println("Subscribed to topic " + topicName + " under node " + PEER_IDS[peerIndex]);
    }

    private static void pushMessage(int peerIndex, String topicName, String message) {
        String url = PEER_URLS[peerIndex] + "/peer/pushMessage";
        MessageRequest request = new MessageRequest(message, topicName);
        restTemplate.postForObject(url, request, String.class);
        System.out.println("Message pushed to topic " + topicName + " under node " + PEER_IDS[peerIndex]);
    }

    private static void createReplica(int peerIndex, String topicName) {
        String url = PEER_URLS[peerIndex] + "/peer/createReplica";
        restTemplate.postForObject(url, "{\"topicName\": \"" + topicName + "\"}", String.class);
        System.out.println("Replica created for node " + PEER_IDS[peerIndex]);
    }

    private static void pullMessages(int peerIndex, String topicName) {
        String url = PEER_URLS[peerIndex] + "/peer/pullMessages?topicName=" + topicName;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Messages pulled from topic " + topicName + ": " + response);
    }

    private static void viewReplica(int peerIndex, String replicaId) {
        String url = PEER_URLS[peerIndex] + "/peer/viewReplica?replicaId=" + replicaId;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Replica viewed under node " + PEER_IDS[peerIndex] + ": " + response);
    }

    private static void failNode(int peerIndex) {
        String url = PEER_URLS[peerIndex] + "/peer/failNode?nodeId=" + PEER_IDS[peerIndex];
        restTemplate.postForObject(url, null, String.class);
        System.out.println("Node " + PEER_IDS[peerIndex] + " failed");
    }

    private static void checkNodeStatus(int peerIndex) {
        String url = PEER_URLS[peerIndex] + "/peer/checkNodeStatus?nodeId=" + PEER_IDS[peerIndex];
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Checked status of node " + PEER_IDS[peerIndex] + ": " + response);
    }
}