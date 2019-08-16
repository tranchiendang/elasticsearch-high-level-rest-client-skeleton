package io.dtc.essearch.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.dtc.essearch.helper.ResponseHelper;
import io.dtc.essearch.model.Product;
import io.dtc.essearch.service.SearchService;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private SearchService<Product> searchService;

    public SearchController(SearchService<Product> searchService) {
        this.searchService = searchService;
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", value = "criterial", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "rate", value = "rating", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "price", value = "range pricing, seperate by comma", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "offset", value = "paging, specific page", required = false, dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "paging, limit per page", required = false, dataType = "integer", paramType = "query"),
    })
    @GetMapping("/query")
    public ResponseEntity<?> doSearchAndFilterByPattern(@RequestParam(name = "q") String criterial, @RequestParam(name = "rate") Optional<String> rateOpt, @RequestParam(name = "price") Optional<String> priceOpt, @RequestParam(name = "offset") Optional<Integer> offsetOpt, @RequestParam(name = "limit") Optional<Integer> limitOpt) throws IOException {
        if (criterial == null) {
            return ResponseEntity.ok().body("Kindly check search pattern.");
        }

        Map<String, String> filterMap = new HashMap<>();
        if (rateOpt.isPresent()) {
            filterMap.put("rating", rateOpt.get());
        }

        Map<String, String> priceMap = new HashMap<>();
        if (priceOpt.isPresent() && !priceOpt.get().isEmpty()){
            String[] priceRangeArray = priceOpt.get().split(",");
            priceMap.put("from", priceRangeArray[0]);
            
            if (priceRangeArray.length > 1) {
                priceMap.put("to", priceRangeArray[1]);
            }
        }

        int offset = offsetOpt.orElse(0);
        int limit = limitOpt.orElse(10);

        List<Product> result = this.searchService.doSearchWithFilter(criterial, filterMap, priceMap, offset, limit);
        return ResponseHelper.successResponse(result);
    };
}