package io.dtc.essearch.controller;

import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.dtc.essearch.helper.ResponseHelper;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.DocumentService;

@RestController
@RequestMapping("/api/v1/doc")
public class DocumentController {

    private DocumentService<Product> productDocumentService;

    public DocumentController(DocumentService<Product> productDocumentService) {
        this.productDocumentService = productDocumentService;
    }

    @PostMapping("/index/product")
    public ResponseEntity<?> indexProductDoc(@RequestBody Product product) throws IOException {
        long id = this.productDocumentService.indexDocument(product);
        return ResponseHelper.successResponse(id);
    };

}