package s6.eval.first.crm.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InvoiceService {
    
    @Value("${laravel.api.url}")
    private String apiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public List<Invoice> getInvoices(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<InvoiceListResponse> response = restTemplate.exchange(
            apiUrl + "/invoices",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            InvoiceListResponse.class
        );
        return response.getBody().getInvoices();
    }

    public void applyDiscount(String externalId,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(
            apiUrl + "/invoices/remise/apply/" + externalId,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            Void.class
        );
    }

    public void removeDiscount(String externalId,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(
            apiUrl + "/invoices/remise/remove/" + externalId,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            Void.class
        );
    }

}

