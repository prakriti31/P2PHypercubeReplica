import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class BenchmarkAPITest implements Runnable {
    private final int peerId;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Random random = new Random();

    public BenchmarkAPITest(int peerId, CountDownLatch startLatch, CountDownLatch doneLatch) {
        this.peerId = peerId;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.restTemplate = new RestTemplate();
        // Assign a port based on peerId, cycling through ports 8080 to 8087
        int port = 8080 + (peerId - 1) % 8;
        this.baseUrl = "http://localhost:" + port;
    }

    @Override
    public void run() {
        String filePath = "benchmarkingapis/api_benchmark_peer_" + peerId + ".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("API,Latency(ms),Timestamp");
            startLatch.await(); // Ensure all peers start together
            System.out.println("Peer " + peerId + " starting API benchmarking...");
            for (int i = 0; i < 30; i++) { // Benchmark each API 30 times
                benchmarkApi(writer);
            }
            System.out.println("Peer " + peerId + " completed all API benchmarking.");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            doneLatch.countDown();
        }
    }

    private void benchmarkApi(PrintWriter writer) {
        int apiChoice = random.nextInt(11); // Choose from 11 possible APIs
        String topicName = "topic_" + peerId + "_" + random.nextInt(10);
        String message = "Message from Peer " + peerId;
        long startTime = System.nanoTime();
        String apiName = switch (apiChoice) {
            case 0 -> "RegisterPeer";
            case 1 -> "UnregisterPeer";
            case 2 -> "UpdateTopic";
            case 3 -> "QueryTopic";
            case 4 -> "CreateTopic";
            case 5 -> "PushMessageToTopic";
            case 6 -> "SubscribeToTopic";
            case 7 -> "PullMessagesFromTopic";
            case 8 -> "GetEventLogs";
            case 9 -> "CreateReplica";
            case 10 -> "ViewReplica";
            default -> "unknown";
        };

        try {
            switch (apiChoice) {
                case 0 -> registerPeer("peer-" + peerId);
                case 1 -> unregisterPeer("peer-" + peerId);
                case 2 -> updateTopic(topicName);
                case 3 -> queryTopic(topicName);
                case 4 -> createTopic(topicName);
                case 5 -> pushMessageToTopic(topicName, message);
                case 6 -> subscribeToTopic(topicName);
                case 7 -> pullMessagesFromTopic(topicName);
                case 8 -> getEventLogs();
                case 9 -> createReplica(topicName);
                case 10 -> viewReplica(topicName + "_replica");
            }
        } catch (Exception e) {
            System.err.println("Error during API call (" + apiName + "): " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long latency = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        long timestamp = System.currentTimeMillis();
        writer.printf("%s,%d,%d%n", apiName, latency, timestamp);
    }

    private void registerPeer(String peerInfo) {
        restTemplate.postForObject(baseUrl + "/indexing/register", "{\"peerInfo\": \"" + peerInfo + "\"}", String.class);
    }

    private void unregisterPeer(String peerId) {
        restTemplate.postForObject(baseUrl + "/indexing/unregister", "{\"peerId\": \"" + peerId + "\"}", String.class);
    }

    private void updateTopic(String topicUpdateInfo) {
        restTemplate.postForObject(baseUrl + "/indexing/updateTopic", "{\"topicUpdateInfo\": \"" + topicUpdateInfo + "\"}", String.class);
    }

    private void queryTopic(String topicName) {
        restTemplate.getForObject(baseUrl + "/indexing/queryTopics?topicName=" + topicName, String.class);
    }

    private void createTopic(String topicName) {
        restTemplate.postForObject(baseUrl + "/peer/createTopic", "{\"topicName\": \"" + topicName + "\"}", String.class);
    }

    private void pushMessageToTopic(String topicName, String message) {
        restTemplate.postForObject(baseUrl + "/peer/pushMessage", "{\"message\": \"" + message + "\", \"topicName\": \"" + topicName + "\"}", String.class);
    }

    private void subscribeToTopic(String topicName) {
        restTemplate.postForObject(baseUrl + "/peer/subscribe", "{\"topicName\": \"" + topicName + "\"}", String.class);
    }

    private void pullMessagesFromTopic(String topicName) {
        restTemplate.getForObject(baseUrl + "/peer/pullMessages?topicName=" + topicName, String.class);
    }

    private void getEventLogs() {
        restTemplate.getForObject(baseUrl + "/peer/eventLogs", String.class);
    }

    private void createReplica(String topicName) {
        restTemplate.postForObject(baseUrl + "/peer/createReplica", "{\"topicName\": \"" + topicName + "\"}", String.class);
    }

    private void viewReplica(String replicaId) {
        restTemplate.getForObject(baseUrl + "/peer/viewReplica?replicaId=" + replicaId, String.class);
    }

    public static void main(String[] args) throws InterruptedException {
        int peerCount = 8;
        CountDownLatch startLatch = new CountDownLatch(1); // To synchronize thread start
        CountDownLatch doneLatch = new CountDownLatch(peerCount); // To wait for all threads to finish

        for (int i = 1; i <= peerCount; i++) {
            new Thread(new BenchmarkAPITest(i, startLatch, doneLatch)).start();
        }

        System.out.println("Starting all peers...");
        startLatch.countDown(); // Signal all peers to start
        doneLatch.await(); // Wait for all peers to finish
        System.out.println("All API benchmarking tasks completed.");

        calculateAndPrintThroughput(peerCount); // Calculate throughput after benchmarking
    }

    private static void calculateAndPrintThroughput(int peerCount) {
        try {
            for (int i = 1; i <= peerCount; i++) {
                String latencyFilePath = "benchmarkingapis/api_benchmark_peer_" + i + ".csv";
                long totalLatency = 0;
                int totalRequests = 0;

                try (BufferedReader reader = new BufferedReader(new FileReader(latencyFilePath))) {
                    reader.readLine(); // Skip header line
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        totalLatency += Long.parseLong(parts[1]); // Add up latencies
                        totalRequests++; // Count total requests for this peer
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue; // Skip to the next peer if there's an error
                }

                long averageLatency = totalRequests > 0 ? totalLatency / totalRequests : 0;
                double throughput = totalRequests > 0 ? (double) totalRequests / (totalLatency / 1000.0) : 0; // requests per second

                try (PrintWriter writer = new PrintWriter(new FileWriter("benchmarkingapis/latency_results_peer_" + i + ".csv"))) {
                    writer.println("Average Latency (ms)");
                    writer.printf("%d%n", averageLatency);
                    System.out.println("Latency results saved for Peer-" + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (PrintWriter writer = new PrintWriter(new FileWriter("benchmarkingapis/throughput_results_peer_" + i + ".csv"))) {
                    writer.println("Throughput (requests/second)");
                    writer.printf("%.2f%n", throughput);
                    System.out.println("Throughput results saved for Peer-" + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}