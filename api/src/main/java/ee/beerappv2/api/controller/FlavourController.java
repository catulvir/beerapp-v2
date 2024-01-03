package ee.beerappv2.api.controller;

import ee.beerappv2.api.service.FlavourService;
import ee.beerappv2.api.service.model.Flavour;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flavours")
public class FlavourController {
    private final FlavourService flavourService;

    public FlavourController(FlavourService flavourService) {
        this.flavourService = flavourService;
    }

    @GetMapping
    public ResponseEntity<List<Flavour>> getAllFlavours() {
        return ResponseEntity.ok(flavourService.getAllFlavours());
    }
}
