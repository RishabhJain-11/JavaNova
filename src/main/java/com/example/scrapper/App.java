package com.example.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter the URL of the Wikipedia page you want to scrap: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String url = scanner.nextLine();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .get();

            String title = doc.title();
            System.out.println(title);

            // Format the body content
            String formattedHtml = doc.body().html();

            // Optionally set pretty printing
            Document formattedDoc = Jsoup.parse(formattedHtml);
            formattedDoc.outputSettings().prettyPrint(true);

            FileWriter writer = new FileWriter("output.csv");
            writer.write("Title: " + title + "\n");
            writer.write("Body - Formatted HTML\n");
            writer.write(formattedDoc.body().html() + "\n");

            writer.close();
            System.out.println("\nFormatted content saved to output.csv");
        } catch (IOException e) {
            System.err.println("Error: Unable to fetch the URL. " + e.getMessage());
        }
    }
}
