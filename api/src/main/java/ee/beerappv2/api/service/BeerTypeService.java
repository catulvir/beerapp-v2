package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.BeerTypeRepository;
import ee.beerappv2.api.service.model.BeerType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerTypeService {

    private final BeerTypeRepository beerTypeRepository;

    public BeerTypeService(BeerTypeRepository beerTypeRepository) {
        this.beerTypeRepository = beerTypeRepository;
    }
    public List<BeerType> getAllBeerTypes() {
        return beerTypeRepository.getAllBeerTypes();
    }
}