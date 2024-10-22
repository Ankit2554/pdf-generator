package com.example.pdf.Controller;

import com.example.pdf.Model.InvoiceRequest;
import com.example.pdf.Service.PdfService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<String> generatePdf(@RequestBody InvoiceRequest request) {
        try {
            String filePath = pdfService.generatePdf(request);
            return ResponseEntity.ok("PDF generated successfully: " + filePath);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body("Error generating PDF: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String fileName) {
        try {
            Resource pdfResource = pdfService.getPdf(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(pdfResource);
        } catch (IOException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}