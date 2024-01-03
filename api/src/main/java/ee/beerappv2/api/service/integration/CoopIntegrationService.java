package ee.beerappv2.api.service.integration;

import ee.beerappv2.api.config.security.AuthTokenFilter;
import ee.beerappv2.api.repository.BeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class CoopIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private final BeerRepository beerRepository;

    RestClient restClient = RestClient.create();

    public CoopIntegrationService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public void requestData() {
        for (int i = 1; i < 9; i++) { // 8 pages of beers in Coop, could be retrieved from request metadata as well
            makeRequest(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void makeRequest(int page) {
        String result = restClient.get()
                .uri("https://api.vandra.ecoop.ee/supermarket/products?category=58&language=et&orderby=base_price&order=asc"
                        + "&page=" + page)
                .accept(APPLICATION_JSON)
                .header("User-Agent", "Collecting" +
                        " Publicly Available Price Information; edrozb@taltech.ee")
                .retrieve()
                .body(String.class);
    }

}
