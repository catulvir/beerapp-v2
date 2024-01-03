package ee.beerappv2.api.controller;

import ee.beerappv2.api.service.BeerTypeService;
import ee.beerappv2.api.service.model.BeerType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/beerTypes")
public class BeerTypeController {
    private final BeerTypeService beerTypeService;

    public BeerTypeController(BeerTypeService beerTypeService) {
        this.beerTypeService = beerTypeService;
    }

    @GetMapping
    public ResponseEntity<List<BeerType>> getAllBeerTypes() {
        return ResponseEntity.ok(beerTypeService.getAllBeerTypes());
    }
}