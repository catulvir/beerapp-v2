package ee.beerappv2.api.controller;

import java.util.List;
import java.util.Map;

import ee.beerappv2.api.controller.json.BeerJson;
import ee.beerappv2.api.service.BeerService;
import ee.beerappv2.api.service.model.Beer;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping
    public ResponseEntity<List<BeerJson>> getBeers(@RequestParam(required = false) Map<String, String> params) {
        return ResponseEntity.ok(beerService.findBeers(params).stream().map((Beer::toBeerJson)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBeer(@PathVariable Long id) {
        var result = beerService.findBeer(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(beerService.findBeer(id).toBeerJson());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BeerJson> createBeer(@RequestBody @Valid BeerJson beer) {
        return ResponseEntity.ok(beerService.saveBeer(beer.toBeer()).toBeerJson());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<HttpStatusCode> editBeer(@RequestBody @Valid BeerJson beer) {
        beerService.updateBeer(beer.toBeer());
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<HttpStatusCode> deleteBeer(@PathVariable Long id) {
        beerService.deleteBeer(id);
        return ResponseEntity.status(200).build();
    }
}

