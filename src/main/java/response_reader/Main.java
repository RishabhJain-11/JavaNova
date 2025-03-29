package response_reader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the API endpoint URL:");
            String apiUrl = scanner.nextLine();

            ApiResponseHandler apiHandler = new ApiResponseHandler(apiUrl);
            apiHandler.makeRequest();

            PdfGenerator pdfGenerator = new PdfGenerator();
            pdfGenerator.generatePdf(apiHandler);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
