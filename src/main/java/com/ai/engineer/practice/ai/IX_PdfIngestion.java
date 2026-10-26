package com.ai.engineer.practice.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class IX_PdfIngestion {

    @GetMapping("/api/extractPdf")
    public ResponseEntity<PdfExtractionResponse> extractClasspathPdf(){
        Resource resource = new ClassPathResource("resume.pdf");
        return ResponseEntity.ok(processPdf(resource));
    }

    @PostMapping("/api/uploadPdf")
    public ResponseEntity<PdfExtractionResponse> extractUploadPdf(@RequestParam("file") MultipartFile file){
        Resource resource = file.getResource();
        return ResponseEntity.ok(processPdf(resource));
    }

    private PdfExtractionResponse processPdf(Resource resource){
        var config = PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(1)
                .build();

        var reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.read();

        List<DocumentDto> documentDtos = documents.stream()
                .map(doc -> new DocumentDto(
                        doc.getId(),
                        doc.getMetadata(),
                        doc.getText()
                )).toList();

        return new PdfExtractionResponse(documentDtos.size(), documentDtos);
    }

    public record PdfExtractionResponse(int totalDocuments, List<DocumentDto> documents){}

    public record DocumentDto(String id, Map<String, Object> metadata, String text){}
}
