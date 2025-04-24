package s6.eval.first.crm.setting;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s6.eval.first.crm.setting.DiscountRate;

@Service
public class SettingService {

    @Value("${laravel.url}/api/settings/remise")
    private String discountRateUrl;

    private final RestTemplate restTemplate;

    public SettingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DiscountRate getDiscountRate(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            return restTemplate.exchange(
                discountRateUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                DiscountRate.class
            ).getBody();
            
        } catch (Exception e) {
            //logger.error("Erreur lors de la récupération du taux", e);
            return new DiscountRate();
        }
    }

    public DiscountRate updateDiscountRate(String token, double newRate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Double> requestBody = Map.of("tauxremise", newRate);
            
            return restTemplate.exchange(
                discountRateUrl,
                HttpMethod.PUT,
                new HttpEntity<>(requestBody, headers),
                DiscountRate.class
            ).getBody();
            
        } catch (Exception e) {
            //logger.error("Erreur lors de la mise à jour", e);
            DiscountRate errorResponse = new DiscountRate();
            errorResponse.setMessage("Erreur lors de la mise à jour");
            return errorResponse;
        }
    }
}
