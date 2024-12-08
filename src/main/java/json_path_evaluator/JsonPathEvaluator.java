package json_path_evaluator;

import com.jayway.jsonpath.JsonPath;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Scanner;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class JsonPathEvaluator {
    public static void main(String[] args) throws IOException {
        // Sample JSON document
        String jsonDocument = new String(readAllBytes(get("src/main/java/json_path_evaluator/example.json")));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the JSON Path Evaluator!");

        while (true) {
            System.out.print("Enter a JSON path to evaluate (or type 'exit' to quit): ");
            String jsonPath = scanner.nextLine();

            if ("exit".equalsIgnoreCase(jsonPath)) {
                System.out.println("Exiting the application. Goodbye!");
                break;
            }

            try {
                Object result = JsonPath.read(jsonDocument, jsonPath);
                System.out.println("Value found: " + result);
                System.out.println("🎉 Congratulations! The value was successfully retrieved.");
            } catch (Exception e) {
                System.out.println("Error: Unable to evaluate the JSON path. Please try again.");
                System.out.println("Hint: Ensure your JSON path syntax is correct (e.g., $.store.book[0].author).");
            }
        }

        scanner.close();
    }
}
