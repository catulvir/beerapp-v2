package ee.beerappv2.api.controller;

import ee.beerappv2.api.service.ManufacturerService;
import ee.beerappv2.api.service.model.Manufacturer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public ResponseEntity<List<Manufacturer>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerService.getAllManufacturers());
    }
}
