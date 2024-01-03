package ee.beerappv2.api.service;

import ee.beerappv2.api.repository.BeerRepository;
import ee.beerappv2.api.service.model.Beer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BeerService {
    private final BeerRepository beerRepository;

    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public List<Beer> findBeers(Map<String, String> params) {
        return beerRepository.findBeers(params);
    }

    @Nullable
    public Beer findBeer(Long id) {
        return beerRepository.findBeer(id);
    }

    public Beer saveBeer(Beer beer) {
        return beerRepository.saveBeer(beer);
    }

    public void updateBeer(Beer beer) {
        beerRepository.updateBeer(beer);
    }

    public void deleteBeer(Long id) {
        beerRepository.deleteBeer(id);
    }

    public List<Beer> findUserRatedBeers(String username) {
        List<Beer> result = beerRepository.findUserRatedBeers(username);
        if (result == null) return Collections.emptyList();
        return result;
    }
}
