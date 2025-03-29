package response_reader;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont;
import io.restassured.response.Response;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

public class PdfGenerator {
    private final Document document;
    private final String fileName;
    private static final BaseColor HEADER_COLOR = new BaseColor(44, 62, 80);
    private static final BaseColor TEXT_COLOR = new BaseColor(52, 73, 94);
    private static final float TITLE_SIZE = 20;
    private static final float HEADER_SIZE = 14;
    private static final float CONTENT_SIZE = 11;

    public PdfGenerator() {
        this.fileName = generateFileName();
        this.document = new Document(PageSize.A4, 50, 50, 50, 50);
    }

    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "api_response_" + timestamp + ".pdf";
    }

    public void generatePdf(ApiResponseHandler apiHandler) throws DocumentException {
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            addTitle();
            addUrl(apiHandler.getApiUrl());
            addStatusCode(apiHandler.getResponse());
            addHeaders(apiHandler.getResponse());
            addCookies(apiHandler.getResponse());
            addResponseBody(apiHandler.getResponse());
            
            document.close();
            System.out.println("Response details have been saved to " + fileName);
        } catch (Exception e) {
            throw new DocumentException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private Font createFont(float size, BaseColor color, boolean isBold) throws DocumentException {
        try {
            BaseFont jetBrainsMono = BaseFont.createFont("fonts/JetBrainsMono-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(jetBrainsMono, size, isBold ? Font.BOLD : Font.NORMAL);
            font.setColor(color);
            return font;
        } catch (Exception e) {
            return FontFactory.getFont(FontFactory.COURIER, size, isBold ? Font.BOLD : Font.NORMAL, color);
        }
    }

    private void addTitle() throws DocumentException {
        Font titleFont = createFont(TITLE_SIZE, HEADER_COLOR, true);
        Paragraph title = new Paragraph("API Response Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addUrl(String url) throws DocumentException {
        Font headerFont = createFont(HEADER_SIZE, HEADER_COLOR, true);
        Font contentFont = createFont(CONTENT_SIZE, TEXT_COLOR, false);
        
        Paragraph urlHeader = new Paragraph("URL:", headerFont);
        Paragraph urlContent = new Paragraph(url, contentFont);
        urlContent.setIndentationLeft(20);
        urlContent.setSpacingAfter(15);
        
        document.add(urlHeader);
        document.add(urlContent);
    }

    private void addStatusCode(Response response) throws DocumentException {
        Font headerFont = createFont(HEADER_SIZE, HEADER_COLOR, true);
        Font contentFont = createFont(CONTENT_SIZE, TEXT_COLOR, false);
        
        Paragraph statusHeader = new Paragraph("Status Code:", headerFont);
        Paragraph statusContent = new Paragraph(String.valueOf(response.getStatusCode()), contentFont);
        statusContent.setIndentationLeft(20);
        statusContent.setSpacingAfter(15);
        
        document.add(statusHeader);
        document.add(statusContent);
    }

    private void addHeaders(Response response) throws DocumentException {
        Font headerFont = createFont(HEADER_SIZE, HEADER_COLOR, true);
        Font contentFont = createFont(CONTENT_SIZE, TEXT_COLOR, false);
        
        document.add(new Paragraph("Headers:", headerFont));
        response.getHeaders().forEach((header) -> {
            try {
                Paragraph headerContent = new Paragraph(header.getName() + ": " + header.getValue(), contentFont);
                headerContent.setIndentationLeft(20);
                document.add(headerContent);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
        document.add(new Paragraph("\n"));
    }

    private void addCookies(Response response) throws DocumentException {
        Font headerFont = createFont(HEADER_SIZE, HEADER_COLOR, true);
        Font contentFont = createFont(CONTENT_SIZE, TEXT_COLOR, false);
        
        document.add(new Paragraph("Cookies:", headerFont));
        response.getCookies().forEach((name, value) -> {
            try {
                Paragraph cookieContent = new Paragraph(name + ": " + value, contentFont);
                cookieContent.setIndentationLeft(20);
                document.add(cookieContent);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
        document.add(new Paragraph("\n"));
    }

    private void addResponseBody(Response response) throws DocumentException {
        Font headerFont = createFont(HEADER_SIZE, HEADER_COLOR, true);
        Font contentFont = createFont(CONTENT_SIZE, TEXT_COLOR, false);
        
        document.add(new Paragraph("Response Body:", headerFont));
        Paragraph bodyContent = new Paragraph(response.asString(), contentFont);
        bodyContent.setIndentationLeft(20);
        document.add(bodyContent);
    }
}