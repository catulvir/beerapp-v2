package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.FlavourRepository;
import ee.beerappv2.api.service.model.Flavour;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlavourService {

    private final FlavourRepository flavourRepository;

    public FlavourService(FlavourRepository flavourRepository) {
        this.flavourRepository = flavourRepository;
    }
    public List<Flavour> getAllFlavours() {
        return flavourRepository.getAllFlavours();
    }
}
