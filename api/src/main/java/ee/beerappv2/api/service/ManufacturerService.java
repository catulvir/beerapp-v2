package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.ManufacturerRepository;
import ee.beerappv2.api.service.model.Manufacturer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.getAllManufacturers();
    }
}