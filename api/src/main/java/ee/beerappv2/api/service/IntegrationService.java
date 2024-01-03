package ee.beerappv2.api.service;

import ee.beerappv2.api.config.security.AuthTokenFilter;
import ee.beerappv2.api.service.integration.CoopIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {
    private final CoopIntegrationService coopIntegrationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public IntegrationService(CoopIntegrationService coopIntegrationService) {
        this.coopIntegrationService = coopIntegrationService;
    }

    public void migrateIntegrationData() {
        logger.info("Starting integration update.");
        coopIntegrationService.requestData();
    }
}
