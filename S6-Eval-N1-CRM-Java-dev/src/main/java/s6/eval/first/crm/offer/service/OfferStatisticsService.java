package s6.eval.first.crm.offer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import s6.eval.first.crm.offer.ClientOfferStatistics;
import s6.eval.first.crm.offer.OfferStatistics;
import s6.eval.first.crm.offer.UserOfferStatistics;

@Service
public class OfferStatisticsService {
    
    @Value("${laravel.url}/api/offers/stat")
    private String offersStatsUrl;

    @Value("${laravel.url}/api/offers/stat/clients")
    private String clientsStatsUrl;

    @Value("${laravel.url}/api/offers/stat/users")
    private String usersStatsUrl;

    private final RestTemplate restTemplate;

    public OfferStatisticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OfferStatistics getGeneralStatistics(String token) {
        return getStatistics(offersStatsUrl, token, OfferStatistics.class);
    }

    public ClientOfferStatistics getClientStatistics(String token) {
        return getStatistics(clientsStatsUrl, token, ClientOfferStatistics.class);
    }

    public UserOfferStatistics getUserStatistics(String token) {
        return getStatistics(usersStatsUrl, token, UserOfferStatistics.class);
    }

    private <T> T getStatistics(String url, String token, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
            ).getBody();
            
        } catch (Exception e) {
            // Loguer l'erreur
            return null;
        }
    }
}
