package response_reader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the API endpoint URL:");
        String apiUrl = scanner.nextLine();

        try {
            // Make API request
            Response response = RestAssured.given()
                    .relaxedHTTPSValidation() // For handling SSL certificates
                    .get(apiUrl);

            // Generate PDF
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "api_response_" + timestamp + ".pdf";
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("API Response Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Add URL
            document.add(new Paragraph("URL: " + apiUrl));
            document.add(new Paragraph("\n"));

            // Add Status Code
            document.add(new Paragraph("Status Code: " + response.getStatusCode()));
            document.add(new Paragraph("\n"));

            // Add Headers
            document.add(new Paragraph("Headers:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            response.getHeaders().forEach((header) -> {
                try {
                    document.add(new Paragraph(header.getName() + ": " + header.getValue()));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            document.add(new Paragraph("\n"));

            // Add Cookies
            document.add(new Paragraph("Cookies:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            response.getCookies().forEach((name, value) -> {
                try {
                    document.add(new Paragraph(name + ": " + value));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            document.add(new Paragraph("\n"));

            // Add Response Body
            document.add(new Paragraph("Response Body:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            String prettyBody = response.asString();
            document.add(new Paragraph(prettyBody));

            document.close();
            System.out.println("Response details have been saved to " + fileName);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
