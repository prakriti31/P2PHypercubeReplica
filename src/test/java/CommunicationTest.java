import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

public class CommunicationTest {
    private static final String BASE_URL = "http://localhost:%d"; // Base URL for all peers, will use dynamic port.
    private static final int NUM_PEERS = 8;
    private static final int[] PEER_PORTS = {8080, 8081, 8082, 8083, 8084, 8085, 8086, 8087};

    public static void main(String[] args) throws InterruptedException {
        // CountDownLatch to make sure all threads finish before printing final output
        CountDownLatch latch = new CountDownLatch(NUM_PEERS * 30);

        // Create and start threads for each peer operation
        for (int i = 0; i < NUM_PEERS; i++) {
            int port = PEER_PORTS[i];
            new Thread(() -> runTestForPeer(port, latch)).start();
        }

        // Wait for all threads to complete
        latch.await();
        System.out.println("All operations completed successfully.");
    }

    private static void runTestForPeer(int port, CountDownLatch latch) {
        try {
            // Perform API operations sequentially but on different threads
            createTopic(port, latch);
            pushMessage(port, latch);
            subscribeToTopic(port, latch);
            pullMessages(port, latch);
            createReplica(port, latch);
            viewReplica(port, latch);
            failNode(port, latch);
            checkNodeStatus(port, latch);
            unregisterPeer(port, latch);
            registerPeer(port, latch);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void createTopic(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/createTopic", port);
        RestTemplate restTemplate = new RestTemplate();
        String topicName = "topic" + port;
        HttpEntity<String> request = new HttpEntity<>(topicName);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Topic created under node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void pushMessage(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/pushMessage", port);
        RestTemplate restTemplate = new RestTemplate();
        String message = "Message from node " + port;
        HttpEntity<String> request = new HttpEntity<>(message);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Message pushed to topic under node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void subscribeToTopic(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/subscribe", port);
        RestTemplate restTemplate = new RestTemplate();
        String topicName = "topic" + port;
        HttpEntity<String> request = new HttpEntity<>(topicName);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Subscribed to topic " + topicName + " under node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void pullMessages(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/pullMessages?topicName=topic" + port, port);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println("Messages pulled from topic under node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void createReplica(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/createReplica", port);
        RestTemplate restTemplate = new RestTemplate();
        String topicName = "topic" + port;
        HttpEntity<String> request = new HttpEntity<>(topicName);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Replica created for node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void viewReplica(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/viewReplica?replicaId=topic" + port + "_replica", port);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println("Viewed replica for topic " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void failNode(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/failNode?nodeId=node" + port, port);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        System.out.println("Node " + port + " failed");
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void checkNodeStatus(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/peer/checkNodeStatus?nodeId=node" + port, port);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println("Checked status of node " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void unregisterPeer(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/indexing/unregister", port);
        RestTemplate restTemplate = new RestTemplate();
        String peerId = "peer" + port;
        HttpEntity<String> request = new HttpEntity<>(peerId);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Unregistered peer " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }

    private static void registerPeer(int port, CountDownLatch latch) throws InterruptedException {
        String url = String.format(BASE_URL + "/indexing/register", port);
        RestTemplate restTemplate = new RestTemplate();
        String peerInfo = "peer" + port;
        HttpEntity<String> request = new HttpEntity<>(peerInfo);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Registered peer " + port);
        latch.countDown();
        Thread.sleep(100); // Simulate small delay
    }
}
