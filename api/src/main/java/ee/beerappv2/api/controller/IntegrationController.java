package ee.beerappv2.api.controller;

import ee.beerappv2.api.service.IntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;

public class IntegrationController {
    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }
    @PostMapping("/update/data")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateIntegrationData() {
        integrationService.migrateIntegrationData();
        return ResponseEntity.ok().build();
    }
}
