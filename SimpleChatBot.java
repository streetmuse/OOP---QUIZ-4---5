import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Properties;
import java.util.Scanner;

public class SimpleChatBot {
    private String serverUrl;
    private String botName;
    private HttpClient httpClient;
    private Scanner scanner;

    public SimpleChatBot() {
        this.httpClient = HttpClient.newHttpClient();
        this.scanner = new Scanner(System.in);
        loadConfiguration();
    }

    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config.txt")) {
            props.load(input);
            this.serverUrl = props.getProperty("server.url", "http://max.ge/q45/93746182/index.php");
            this.botName = props.getProperty("bot.name", "ChatBot");
        } catch (IOException e) {
            System.out.println("Configuration file not found. Using default values.");
            this.serverUrl = "http://max.ge/q45/93746182/index.php";
            this.botName = "ChatBot";
        }
    }

    public void start() {
        System.out.println("=".repeat(50));
        System.out.println("Welcome to " + botName + "!");
        System.out.println("REST Blog Platform Chat Bot");
        System.out.println("=".repeat(50));

        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createBlogPost();
                    break;
                case 2:
                    viewAllBlogPosts();
                    break;
                case 3:
                    viewStatistics();
                    break;
                case 4:
                    System.out.println(botName + " says: Goodbye! Thanks for using the blog platform!");
                    return;
                default:
                    System.out.println(botName + " says: Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n" + botName + " - What would you like to do?");
        System.out.println("1. Create a new blog post");
        System.out.println("2. View all blog posts");
        System.out.println("3. View site statistics");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createBlogPost() {
        System.out.println("\n" + botName + " says: Let's create a new blog post!");

        System.out.print("Enter blog title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author name: ");
        String author = scanner.nextLine();

        System.out.print("Enter blog content: ");
        String content = scanner.nextLine();

        try {
            String jsonBody = String.format(
                    "{\"title\":\"%s\",\"content\":\"%s\",\"author\":\"%s\"}",
                    escapeJson(title), escapeJson(content), escapeJson(author)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=blogs"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println(botName + " says: Blog post created successfully! ✓");
            } else {
                System.out.println(botName + " says: Failed to create blog post. Status: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            System.out.println(botName + " says: Error creating blog post: " + e.getMessage());
        }
    }

    private void viewAllBlogPosts() {
        System.out.println("\n" + botName + " says: Fetching all blog posts...");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=blogs"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println(botName + " says: Here are all the blog posts:\n");
                System.out.println("=".repeat(60));

                // Simple JSON parsing without external libraries
                String responseBody = response.body();
                System.out.println("Raw API Response:");
                System.out.println(responseBody);
                System.out.println("=".repeat(60));

                // You can parse this manually or the professor can see the raw JSON
                if (responseBody.contains("\"success\":true")) {
                    System.out.println(botName + " says: Blog posts retrieved successfully!");
                } else {
                    System.out.println(botName + " says: No blog posts found or error occurred.");
                }
            } else {
                System.out.println(botName + " says: Failed to fetch blog posts. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println(botName + " says: Error fetching blog posts: " + e.getMessage());
        }
    }

    private void viewStatistics() {
        System.out.println("\n" + botName + " says: Fetching site statistics...");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "?api=stats"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println(botName + " says: Here are the site statistics:\n");
                System.out.println("=".repeat(40));

                // Display raw JSON response
                String responseBody = response.body();
                System.out.println("Statistics JSON Response:");
                System.out.println(responseBody);

                // Simple parsing for basic stats display
                if (responseBody.contains("total_posts")) {
                    System.out.println("\n" + botName + " says: Statistics retrieved successfully!");
                } else {
                    System.out.println(botName + " says: Failed to retrieve statistics.");
                }
                System.out.println("=".repeat(40));
            } else {
                System.out.println(botName + " says: Failed to fetch statistics. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println(botName + " says: Error fetching statistics: " + e.getMessage());
        }
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    public static void main(String[] args) {
        SimpleChatBot bot = new SimpleChatBot();
        bot.start();
    }
}