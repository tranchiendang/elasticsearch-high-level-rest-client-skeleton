package io.dtc.essearch.controller;

import java.io.IOException;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.IndexService;

@RestController
@RequestMapping("/api/v1/index")
public class IndexController {

    private IndexService<Product> indexProductService;

    public IndexController(IndexService<Product> indexProductService) {
        this.indexProductService = indexProductService;
    }

    @GetMapping("/product")
    public ResponseEntity<?> createProductIndex()
            throws NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
        this.indexProductService.create(Optional.ofNullable(null), Optional.ofNullable(null));
        return null;
    };
}