package s6.eval.first.crm.payment;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Value("${laravel.api.url}/payments/clients/{clientId}")
    private String clientPaymentsUrl;

    @Value("${laravel.api.url}/payments/{externalId}")
    private String paymentDetailUrl;

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentDetails getClientPayments(Long clientId, String token) {
        String url = clientPaymentsUrl.replace("{clientId}", clientId.toString());
        return getPaymentData(url, token, PaymentDetails.class);
    }

    public Payment getPaymentByExternalId(String externalId, String token) {
        String url = paymentDetailUrl.replace("{externalId}", externalId);
        return getPaymentData(url, token, Payment.class);
    } 

    /* public Payment getPaymentByExternalId(String externalId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            
            ResponseEntity<Payment> response = restTemplate.exchange(
                paymentDetailUrl.replace("{externalId}", externalId),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Payment.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
            
            logger.error("Réponse vide ou code d'erreur: {}", response.getStatusCode());
            return null;
            
        } catch (HttpClientErrorException e) {
            logger.error("Erreur API - Body: {}", e.getResponseBodyAsString());
            return null;
        }
    }
 */
    public ResponseEntity<?> updatePayment(String externalId, PaymentUpdateRequest request, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            return restTemplate.exchange(
                paymentDetailUrl.replace("{externalId}", externalId),
                HttpMethod.PUT,
                new HttpEntity<>(request, headers),
                String.class
            );
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @Value("${laravel.api.url}/payments/delete/{externalId}")
    private String paymentDeleteUrl;

    public ResponseEntity<?> deletePayment(String externalId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            return restTemplate.exchange(
                paymentDeleteUrl.replace("{externalId}", externalId),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
            );
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    private <T> T getPaymentData(String url, String token, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            responseType
        ).getBody();
    }
}
