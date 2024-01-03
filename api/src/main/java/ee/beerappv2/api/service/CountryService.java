package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.CountryRepository;
import ee.beerappv2.api.service.model.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    public List<Country> getAllCountries() {
        return countryRepository.getAllCountries();
    }
}
