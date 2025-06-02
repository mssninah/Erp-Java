package com.example.demo.service.RH;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImportService {

    @Value("${erpnext.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ImportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls the reset_data API endpoint to clear all data.
     *
     * @param sid Session ID for authentication
     * @throws Exception if the API call fails
     */
    public void resetData(String sid) throws Exception {
        // URL mise à jour pour correspondre à celle utilisée dans Postman
        String url = baseUrl + "/api/method/erpnext.api.data.reset_data";
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "sid=" + sid); // Ajoute le cookie d'authentification
        headers.set("Content-Type", "application/json");
    
        HttpEntity<String> entity = new HttpEntity<>(headers);
    
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
            );
    
            // Gestion de la réponse
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Data reset successfully!");
                System.out.println("Response: " + response.getBody());
            } else {
                throw new Exception("Failed to reset data. HTTP Status: " + response.getStatusCode());
            }
    
        } catch (Exception e) {
            System.err.println("Error calling reset_data API: " + e.getMessage());
            throw new Exception("Data reset failed. Reason: " + e.getMessage(), e);
        }
    }
    
   
}
