package com.example.pdf.Service;

import com.example.pdf.Model.InvoiceRequest;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;

@Service
public class PdfService {

    @Value("${pdf.storage.dir}")
    public String pdfStorageDir;

    private final SpringTemplateEngine templateEngine;

    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generatePdf(InvoiceRequest request) throws IOException, DocumentException {
        String fileName = hashRequest(request) + ".pdf";
        String filePath = pdfStorageDir + File.separator + fileName;
        File pdfFile = new File(filePath);

        if (!pdfFile.exists()) {
            Context context = new Context();
            context.setVariable("invoicce", request);
            String htmlContent = templateEngine.process("invoice", context);

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, outputStream);
                document.open();

                InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
                com.lowagie.text.html.simpleparser.HTMLWorker htmlWorker = new com.lowagie.text.html.simpleparser.HTMLWorker(document);
                htmlWorker.parse(new InputStreamReader(inputStream));

                document.close();
            }
        }

        return filePath;
    }

    public Resource getPdf(String fileName) throws IOException {
        String filePath = pdfStorageDir + File.separator + fileName;
        if (new File(filePath).exists()) {
            return new FileSystemResource(filePath);
        } else {
            throw new FileNotFoundException("PDF not found.");
        }
    }

    private String hashRequest(InvoiceRequest request) {
        return Integer.toHexString((request.getBuyer() + request.getSeller()).hashCode());
    }
}