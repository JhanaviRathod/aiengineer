package com.ai.engineer.practice.ai;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
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

//PdfIngestion with chunking
@RestController
public class IX_PdfIngestion {

    @GetMapping("/api/extractPdf")
    public ResponseEntity<PdfChunkedResponse> extractClasspathPdf(){
        Resource resource = new ClassPathResource("resume.pdf");
        return ResponseEntity.ok(processAndSplitPdf(resource, "resume.pdf"));
    }

    @PostMapping("/api/uploadPdf")
    public ResponseEntity<PdfChunkedResponse> extractUploadPdf(@RequestParam("file") MultipartFile file){
        Resource resource = file.getResource();
        String sourceName = file.getOriginalFilename() != null ? file.getOriginalFilename() :  "uploaded.pdf";
        return ResponseEntity.ok(processAndSplitPdf(resource, sourceName));
    }

    private PdfChunkedResponse processAndSplitPdf(Resource resource, String sourceName){
        // Step 1: Read PDF pages into Documents
        var config = PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(1)
                .build();

        var reader = new PagePdfDocumentReader(resource, config);
        List<Document> documents = reader.read();

        // Step 2: Add source metadata before splitting
        for(Document document : documents){
            document.getMetadata().put("source", sourceName);
        }

        // Step 3: Transform page documents into smaller chunks
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(500).build();

        List<Document> chunks = splitter.split(documents);

        // Step 4: Add chunk_index metadata and map to DTOs
        List<DocumentDto> chunkDto = chunks.stream()
                .map(chunk -> new DocumentDto(
                        chunk.getId(),
                        chunk.getMetadata(),
                        chunk.getText()
                )).toList();

        return new PdfChunkedResponse(documents.size(), chunkDto.size(), chunkDto);
    }

    public record PdfChunkedResponse(int originalDocumentsCount, int totalChunksCount, List<DocumentDto> chunks){}

    public record DocumentDto(String id, Map<String, Object> metadata, String text){}
}
